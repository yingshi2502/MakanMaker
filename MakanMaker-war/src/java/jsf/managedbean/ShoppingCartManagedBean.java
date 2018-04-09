/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.MealKitControllerLocal;
import ejb.session.stateless.ShoppingCartControllerLocal;
import entity.CustomerEntity;
import entity.MealKitEntity;
import entity.ShoppingCartEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Summer
 */
@Named(value = "shoppingCartManagedBean")
@ViewScoped
public class ShoppingCartManagedBean implements Serializable{
    @EJB
    private CustomerControllerLocal customerController;

    @EJB
    private ShoppingCartControllerLocal shoppingCartController;
    
    private List<MealKitEntity> mealKits;
    private CustomerEntity currentCustomer;
    private ShoppingCartEntity shoppingCart;
    private List<CartItemWrapper> items;
    
    public ShoppingCartManagedBean() {
        mealKits = new ArrayList<>();
        items = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct(){
        currentCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
        
        if (currentCustomer==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Login!", null));
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AddressManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        System.err.println("Customer "+currentCustomer.getFullName());
        shoppingCart = customerController.retrieveShoppingCartByCustomerId(currentCustomer.getCustomerId());
        
        
        //to be deleted after shopping cart code up.
        //System.err.println("shopping cart "+shoppingCart.sho);
        //shoppingCart = shoppingCartController.addItem(1l, 2, shoppingCart.getShoppingCartId());
        mealKits = shoppingCartController.retrieveMealKitsByCustomerId(shoppingCart.getShoppingCartId());
        
        int i=0;
        List<Integer> qs = shoppingCart.getQuantity();
        for (MealKitEntity m: mealKits){
            items.add(new CartItemWrapper(qs.get(i),m));
            i++;
        }
    }

    public List<MealKitEntity> getMealKits() {
        return mealKits;
    }

    public void setMealKits(List<MealKitEntity> mealKits) {
        this.mealKits = mealKits;
    }

    public CustomerEntity getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(CustomerEntity currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    /**
     * @return the items
     */
    public List<CartItemWrapper> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<CartItemWrapper> items) {
        this.items = items;
    }
    
}


class CartItemWrapper{
    int quantity;
    MealKitEntity mk;
    public CartItemWrapper(){
        
    }
    public CartItemWrapper(int q, MealKitEntity mk){
        this.quantity = q;
        this.mk = mk;
    }
}