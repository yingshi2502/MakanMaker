/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.WishListControllerLocal;
import entity.CustomerEntity;
import entity.MealKitEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author yingshi
 */
@Named(value = "wishListManagedBean")
@RequestScoped
public class WishListManagedBean {

    @EJB(name = "WishListControllerLocal")
    private WishListControllerLocal wishListControllerLocal;

    private List<MealKitEntity> wishListMealKits;
    private Long customerId;
    
    
    public WishListManagedBean() {
        wishListMealKits = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct(){
        CustomerEntity currCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
        customerId = currCustomer.getCustomerId();
        
        wishListMealKits = wishListControllerLocal.getWishListByCustomerId(getCustomerId());
    }
    
    public void deleteFromWishList(ActionEvent event){
        Long toDelete = (Long)event.getComponent().getAttributes().get("toDelete");
        wishListControllerLocal.deleteFromWishList(customerId, toDelete);
        wishListMealKits.remove(toDelete);
    }
    
    public void addToShoppingCart(ActionEvent event){
        Long toAdd = (Long)event.getComponent().getAttributes().get("toAdd");
        wishListControllerLocal.addWishListItemToShoppingCart(customerId, toAdd);
        wishListMealKits.remove(toAdd);
    }
    

    /**
     * @return the wishListMealKits
     */
    public List<MealKitEntity> getWishListMealKits() {
        return wishListMealKits;
    }

    /**
     * @param wishListMealKits the wishListMealKits to set
     */
    public void setWishListMealKits(List<MealKitEntity> wishListMealKits) {
        this.wishListMealKits = wishListMealKits;
    }

    /**
     * @return the customerId
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    
}
