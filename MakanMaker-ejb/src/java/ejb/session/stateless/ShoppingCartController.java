/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MealKitEntity;
import entity.ShoppingCartEntity;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author yingshi
 */
@Stateless
public class ShoppingCartController implements ShoppingCartControllerLocal {

    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;

    @Override
    public double calculatePriceByCartId(Long shoppingCartId){
        ShoppingCartEntity shoppingCart = em.find(ShoppingCartEntity.class, shoppingCartId);
        double totalAmount = 0.0;
        List<Long> mealKits = shoppingCart.getMealKits();
        List<Integer> quantities = shoppingCart.getQuantity();
        for (int i=0;i<shoppingCart.getMealKits().size();i++){
            MealKitEntity m = (MealKitEntity) em.find(MealKitEntity.class, mealKits.get(i));
            totalAmount += m.getPrice() * quantities.get(i);
        }
        return totalAmount;
    }
    
   

    @Override
    public ShoppingCartEntity addItem(Long mealKitId, Integer qty, Long cartId){
        ShoppingCartEntity shoppingCart = em.find(ShoppingCartEntity.class, cartId);
        shoppingCart.getMealKits().add(mealKitId);
        shoppingCart.getQuantity().add(qty);
        return shoppingCart;
    }
}
