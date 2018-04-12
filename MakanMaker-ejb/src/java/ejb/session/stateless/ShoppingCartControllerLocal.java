/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MealKitEntity;
import entity.ShoppingCartEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author yingshi
 */
@Local
public interface ShoppingCartControllerLocal {

    public double calculatePriceByCartId(Long shoppingCartId);

    public ShoppingCartEntity addItem(Long mealKitId, Integer qty, Long cartId);

    public boolean checkItemExistence(Long customerId, Long mealKitId);

    public List<MealKitEntity> retrieveMealKitsByCustomerId(Long shoppingCartId, boolean detach);

    public void clearShoppingCart(Long customerId);

    public void deleteIten(Long customerId, Long mealKitId);

    public void updateQty(Long customerId, Long mealKitId, Integer newQty);
    
}
