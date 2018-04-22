/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReviewEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.EmptyListException;

/**
 *
 * @author yingshi
 */
@Local
public interface ReviewControllerLocal {

    ReviewEntity createNewReiview(String customerUsername, Long orderId,Long mealKitId, String reviewContent, Integer ratings);

    List<ReviewEntity> retrieveReviewByMealKitId(Long mealKitId, boolean detach)throws EmptyListException;
    
}
