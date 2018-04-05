/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MealKitEntity;
import entity.TagEntity;
import java.util.ArrayList;
import java.util.Arrays;
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

        try {
            em.persist(newTag);
            em.flush();
            return newTag;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) {
                throw new TagExisitException("Same Tag name already exists");
            } else {
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
    public void linkTagAndMealKit(Long tagId, Long mealKitId) {
        TagEntity tag = em.find(TagEntity.class, tagId);
        MealKitEntity mealKit = em.find(MealKitEntity.class, mealKitId);
        tag.getMealKits().add(mealKit);
        mealKit.getTags().add(tag);
        em.flush();
    }

    @Override
    public List<MealKitEntity> retrieveMealKitsByTags(List<String> selectedTags) {
        System.err.println("*****Selected Tag Size: " + selectedTags.size());
        List<MealKitEntity> allMealKits = mealKitControllerLocal.retrieveAllMealKits();
        if (selectedTags.size()==0) return allMealKits;
        
        List<MealKitEntity> selectedMKs = new ArrayList<>();
        for (MealKitEntity mk : allMealKits) {
            
            if (containsAll(mk.getTags(), selectedTags)){
                System.err.println("***Tag Controller: TRUE");
                selectedMKs.add(mk);
            }

        }
        System.err.println("***Tag Controller: mk size" + selectedMKs.size());
        return selectedMKs;
    }

    private boolean containsAll(List<TagEntity> tags, List<String> selectedTags) {
        if (tags.size() < selectedTags.size()) {
            return false;
        }
        
        for (String s: selectedTags){
            boolean containsThis = false;
            
            for (TagEntity t: tags){
                if (t.getName().equals(s)){
                    containsThis = true;
                    break;
                }
            }
            if (!containsThis) return false;
        }
        return true;
    }

    @Override
    public List<TagEntity> retrieveAllTags() {
        Query query = em.createQuery("SELECT t FROM TagEntity t");
        return query.getResultList();
    }
    
    @Override
     public Long retrieveTagIdByTagName(String tagName){
        Query query = em.createQuery("SELECT tm FROM TagEntity tm WHERE tm.name = :nameOfTag");
        query.setParameter("nameOfTag", tagName);
        TagEntity tag = (TagEntity) query.getSingleResult();
        return tag.getTagId();
    }
}
