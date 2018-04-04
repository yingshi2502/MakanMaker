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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author yingshi
 */
@Stateless
public class WishListController implements WishListControllerLocal {

    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;

    
    @Override
    public List<MealKitEntity> getWishListByCustomerId(Long customerId){
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        List<Long> mealKitsIds = customer.getWishList();
        List<MealKitEntity> wishlist = new ArrayList<>();
        if (mealKitsIds == null || mealKitsIds.isEmpty()) return wishlist;
        System.err.println("*****"+ mealKitsIds.size()+" "+mealKitsIds.get(0));
        
        for (Long id: mealKitsIds){
            System.err.println("***** id:"+ id);
            MealKitEntity me = em.find(MealKitEntity.class, id);
            if (me.isIsAvailable()){
                wishlist.add(me);
            }
        }
        return wishlist;
    }

    @Override
    public void addToWishList(Long customerId, Long mealKitId){
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        customer.getWishList().add(mealKitId);
    }
    
    @Override
    public void deleteFromWishList(Long customerId, Long mealKitId){
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        customer.getWishList().remove(mealKitId);
    }
    
    @Override
    public void addWishListItemToShoppingCart(Long customerId, Long mealKitId){
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        customer.getWishList().remove(mealKitId);
        
        ShoppingCartEntity shoppingCart = customer.getShoppingCart();
        shoppingCart.getMealKits().add(mealKitId);
        shoppingCart.getQuantity().add(1);
    }
}
