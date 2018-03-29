/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MealKitEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author yingshi
 */
@Local
public interface WishListControllerLocal {

    public List<MealKitEntity> getWishListByCustomerId(Long customerId);

    public void addToWishList(Long customerId, Long mealKitId);

    public void deleteFromWishList(Long customerId, Long mealKitId);

    public void addWishListItemToShoppingCart(Long customerId, Long mealKitId);
    
}
