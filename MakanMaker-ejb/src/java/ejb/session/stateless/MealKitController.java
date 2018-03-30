/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MealKitEntity;
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
            mealKit.getMealKitId();
            return mealKit;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) {
                throw new MealKitExistException("Meal Kit with same name already exist");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public MealKitEntity updateMealKit(MealKitEntity mealKit) throws MealKitExistException,GeneralException {
        try {
            return em.merge(mealKit);
        } catch (PersistenceException ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) {
                throw new MealKitExistException("Meal Kit with same name already exist");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public MealKitEntity retrieveMealKitById(Long mealKitId) throws MealKitNotFoundException {
        MealKitEntity mealKit = em.find(MealKitEntity.class, mealKitId);
        if (mealKit != null){
            //mealKit.getReviews().size();
            return mealKit;
        }else{
            throw new MealKitNotFoundException("Meal Kit ID "+ mealKitId+" does not exists!");
        }
    }

    @Override
    public void deleteMealKit(MealKitEntity mealKit){
        if (mealKit.getReviews().isEmpty()) {
            em.remove(mealKit);
            em.flush();
        } else {
            mealKit.setIsAvailable(false);
        };
    }
    

    @Override
    public List<MealKitEntity> retrieveAllMealKits() {
        Query query = em.createQuery("SELECT mk FROM MealKitEntity mk");
        List<MealKitEntity> mealKits = query.getResultList();
        for (MealKitEntity mk : mealKits) {
            mk.getReviews().size();
        }
        return mealKits;
    }
}
