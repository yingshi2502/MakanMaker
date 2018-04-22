/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MealKitEntity;
import entity.ReviewEntity;
import entity.TagEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.GeneralException;
import util.exception.MealKitExistException;
import util.exception.MealKitNotFoundException;

/**
 *
 * @author Summer
 */
@Stateless
public class MealKitController implements MealKitControllerLocal {

    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;


    public MealKitController() {
    }
    
   
    @Override
    public MealKitEntity createNewMealKit(MealKitEntity mealKit) throws MealKitExistException, GeneralException {
        try {
            em.persist(mealKit);
            em.flush();
            return mealKit;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) {
                throw new MealKitExistException("MealKit with same name already exist");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public MealKitEntity updateMealKit(MealKitEntity mealKit) throws MealKitExistException,GeneralException {
        try {
            MealKitEntity mkToUpdate = em.find(MealKitEntity.class, mealKit.getMealKitId());
            mkToUpdate.setDescription(mealKit.getDescription());
            mkToUpdate.setDifficulty(mealKit.getDifficulty());
            mkToUpdate.setImagePath(mealKit.getImagePath());
            mkToUpdate.setIngredients(mealKit.getIngredients());
            mkToUpdate.setName(mealKit.getName());
            mkToUpdate.setNutrition(mealKit.getNutrition());
            mkToUpdate.setPrice(mealKit.getPrice());
            mkToUpdate.setRecipe(mealKit.getRecipe());
            mkToUpdate.setTime(mealKit.getTime());
            return em.merge(mealKit);
        } catch (PersistenceException ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) {
                throw new MealKitExistException("MealKit with same name already exist");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public MealKitEntity retrieveMealKitById(Long mealKitId, boolean detach) throws MealKitNotFoundException {
        MealKitEntity mealKit = em.find(MealKitEntity.class, mealKitId);
        if (mealKit != null){
            //mealKit.getReviews().size();
            if (detach){
                em.detach(mealKit);
                mealKit.setOrders(null);
                mealKit.setReviews(null);
                mealKit.setTags(null);
            }
            return mealKit;
        }else{
            throw new MealKitNotFoundException("MealKit ID "+ mealKitId+" does not exists!");
        }
    }

    @Override
    public void deleteMealKit(MealKitEntity mealKit){
        MealKitEntity mk = em.find(MealKitEntity.class, mealKit.getMealKitId());
        mk.setIsAvailable(false);
    }
    
    @Override
    public List<MealKitEntity> searchMealKits(String keywords, boolean detach){
        Query query = em.createQuery("SELECT mk FROM MealKitEntity mk WHERE lower(mk.name) LIKE lower(concat('%', :keywords,'%'))");
        query.setParameter("keywords", keywords);
        
        List<MealKitEntity> mks = query.getResultList();
        
        if (detach) {
            for (MealKitEntity mk : mks) {
                em.detach(mk);
                mk.getOrders().clear();
                mk.getReviews().clear();
                for (TagEntity t : mk.getTags()) {
                    em.detach(t);
                    t.setMealKits(null);
                }
            }
        }
        
        return mks;
    }
            
    
    @Override
    public List<MealKitEntity> retrieveAllMealKits() {
        Query query = em.createQuery("SELECT mk FROM MealKitEntity mk");
        List<MealKitEntity> mealKits = query.getResultList();
        for (MealKitEntity mk : mealKits) {
            mk.getReviews().size();
            mk.getTags().size();
        }
        return mealKits;
    }
    
    @Override
    public List<MealKitEntity> retrieveAvailableMealKits(boolean detach) {
        Query query = em.createQuery("SELECT mk FROM MealKitEntity mk WHERE mk.isAvailable = TRUE");
        List<MealKitEntity> mealKits = query.getResultList();
        for (MealKitEntity mk : mealKits) {
            if (detach){
                em.detach(mk);
                mk.setReviews(null);
                mk.setOrders(null);
                mk.setTags(null);
            }else{
                mk.getReviews().size();
                mk.getTags().size();
            }
            
        }
        return mealKits;
    }
}
