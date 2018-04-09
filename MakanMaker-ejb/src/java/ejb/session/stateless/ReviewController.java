/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MealKitEntity;
import entity.OrderEntity;
import entity.ReviewEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmptyListException;

/**
 *
 * @author yingshi
 */
@Stateless
public class ReviewController implements ReviewControllerLocal {

    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;

    public ReviewController() {
    }

    /**
     *
     * @param customerUsername
     * @param mealKitId
     * @param reviewContent
     * @param ratings
     * @return
     */
    @Override
    public ReviewEntity createNewReiview(String customerUsername, Long orderId,Long mealKitId, String reviewContent, Integer ratings) {
        MealKitEntity targetMealKit = em.find(MealKitEntity.class, mealKitId);
        
        ReviewEntity newReview = new ReviewEntity(customerUsername, ratings, reviewContent);
        em.persist(newReview);
        
        newReview.setMealKit(targetMealKit);
        targetMealKit.getReviews().add(newReview);
        
        OrderEntity o = em.find(OrderEntity.class, orderId);
        o.setIsReviewed(true);
        
        em.flush();
        em.refresh(newReview);
        return newReview;
    }

    /**
     *
     * @param mealKitId
     * @return
     * @throws EmptyListException
     */
    @Override
    public List<ReviewEntity> retrieveReviewByMealKitId(Long mealKitId) throws EmptyListException{
        Query query = em.createQuery("SELECT r FROM ReviewEntity r WHERE r.mealKit.mealKitId =:id");
        query.setParameter("id", mealKitId);
        List<ReviewEntity> list = query.getResultList();
        if (list.isEmpty()){
            throw new EmptyListException("No reviews added for the mealKid");
        }
        return list;
    }

    

    
}
