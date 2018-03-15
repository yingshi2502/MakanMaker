/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MealKitEntity;
import entity.TagEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.GeneralException;
import util.exception.TagExisitException;

/**
 *
 * @author yingshi
 */
@Stateless
public class TagController implements TagControllerLocal {

    @EJB(name = "MealKitControllerLocal")
    private MealKitControllerLocal mealKitControllerLocal;

    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;

    
    public TagController() {
    }

    @Override
    public TagEntity createNewTag(TagEntity newTag) throws TagExisitException, GeneralException {
        
        try{
            em.persist(newTag);
            em.flush();
            return newTag;
        }catch (PersistenceException ex){
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) {
                throw new TagExisitException("Same Tag name already exists");
            }else{
                throw new GeneralException("Unexpected error");
            }
        }
    }
    
    /**
     *
     * @param tagId
     * @param mealKitId
     */
    @Override
    public void linkTagAndMealKit(Long tagId, Long mealKitId){
        TagEntity tag = em.find(TagEntity.class, tagId);
        MealKitEntity mealKit = em.find(MealKitEntity.class, mealKitId);
        tag.getMealKits().add(mealKit);
        mealKit.getTags().add(tag);
        em.flush();
    }
    @Override
    public List<MealKitEntity> retrieveMealKitsByTags(List<TagEntity> tags){
        List<MealKitEntity> allMealKits = mealKitControllerLocal.retrieveAllMealKits();
        List<MealKitEntity> selectedMKs = null;
        for (MealKitEntity mk:allMealKits){
            if (mk.getTags().containsAll(tags)){
                selectedMKs.add(mk);
            }
        }
        return selectedMKs;
    }
   
}
