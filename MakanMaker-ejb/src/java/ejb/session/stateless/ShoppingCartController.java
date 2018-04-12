/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.MealKitEntity;
import entity.ShoppingCartEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author yingshi
 */
@Stateless
public class ShoppingCartController implements ShoppingCartControllerLocal {

    @EJB
    private CustomerControllerLocal customerController;

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
    
    @Override
    public void updateQty(Long customerId, Long mealKitId, Integer newQty){
        ShoppingCartEntity cart = customerController.retrieveShoppingCartByCustomerId(customerId,false);
        int i = cart.getMealKits().indexOf(mealKitId);
        cart.getQuantity().set(i, newQty);
    }
    
    @Override
    public boolean checkItemExistence(Long customerId, Long mealKitId){
        ShoppingCartEntity cart = customerController.retrieveShoppingCartByCustomerId(customerId,false);
        return cart.getMealKits().contains(mealKitId);
    }
    
    @Override
    public List<MealKitEntity> retrieveMealKitsByCustomerId(Long shoppingCartId, boolean detach) {
        ShoppingCartEntity shoppingCart = em.find(ShoppingCartEntity.class, shoppingCartId);
        List<MealKitEntity> mealKits = new ArrayList<>(); 
        System.err.println("*****"+shoppingCart.getMealKits().size());
        for (Long mkId : shoppingCart.getMealKits()) {
            System.err.println("**** inside loop"+mkId);
            MealKitEntity mk = em.find(MealKitEntity.class, mkId);
            if (detach){
                em.detach(mk);
                mk.setOrders(null);
                mk.setReviews(null);
                mk.setTags(null);
            }
            mealKits.add(mk);
        }
        return mealKits;
    }
    
    @Override
    public void deleteIten(Long customerId, Long mealKitId){
        ShoppingCartEntity cart = customerController.retrieveShoppingCartByCustomerId(customerId,false);
        int i = cart.getMealKits().indexOf(mealKitId);
        cart.getMealKits().remove(i);
        cart.getQuantity().remove(i);
    }
    
    /**
     *
     * @param customerId
     */
    @Override
    public void clearShoppingCart(Long customerId){
        System.err.println("*****Clear Shopping Cart");
        ShoppingCartEntity spc = customerController.retrieveShoppingCartByCustomerId(customerId,false);
        spc.getMealKits().clear();
        spc.getQuantity().clear();
    }
}
