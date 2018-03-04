/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReviewEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @Override
    public ReviewEntity createNewReiview(Long customerId, Long mealKitId, String reviewContent, Integer ratings) {
        return null;
    }

    @Override
    public List<ReviewEntity> retrieveReviewByMealKitId(Long mealKitId) throws EmptyListException{
        return null;
    }

    

    
}
