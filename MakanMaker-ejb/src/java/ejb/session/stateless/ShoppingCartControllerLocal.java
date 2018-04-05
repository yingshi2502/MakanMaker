/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ShoppingCartEntity;
import javax.ejb.Local;

/**
 *
 * @author yingshi
 */
@Local
public interface ShoppingCartControllerLocal {

    public double calculatePriceByCartId(Long shoppingCartId);

    public ShoppingCartEntity addItem(Long mealKitId, Integer qty, Long cartId);
    
}
