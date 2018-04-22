/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerControllerLocal;
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
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.helperClass.CartItemWrapper;

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
    private int newValue;
    private boolean noItem;
    
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
        refresh();
        System.err.println("Customer "+currentCustomer.getFullName());
        
    }
    
    public void changeQtyAjax(CartItemWrapper c){
        shoppingCartController.updateQty(currentCustomer.getCustomerId(), c.getMk().getMealKitId(), newValue);
        
        refresh();
    }
    
    public void updateSpinnerQty(ValueChangeEvent event){
        System.err.println("****value change listern of spinner");
        newValue = (int) event.getNewValue();
    }
    
    
    
    private void refresh(){
        setShoppingCart(customerController.retrieveShoppingCartByCustomerId(currentCustomer.getCustomerId(),false));
        
        mealKits = shoppingCartController.retrieveMealKitsByCustomerId(getShoppingCart().getShoppingCartId(),false);
        noItem = mealKits.isEmpty();
        int i=0;
        List<Integer> qs = getShoppingCart().getQuantity();
        items = new ArrayList<>();
        for (MealKitEntity m: mealKits){
            items.add(new CartItemWrapper(qs.get(i),m));
            i++;
        }
    }
    public void deleteItem(ActionEvent event){
        CartItemWrapper itemToDelete = (CartItemWrapper)event.getComponent().getAttributes().get("itemToDelete");
        
        shoppingCartController.deleteIten(currentCustomer.getCustomerId(), itemToDelete.getMk().getMealKitId());
        refresh();
        
        //delete function in controller, 
        //remove item from mb list
        //
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

    /**
     * @return the shoppingCart
     */
    public ShoppingCartEntity getShoppingCart() {
        return shoppingCart;
    }

    /**
     * @param shoppingCart the shoppingCart to set
     */
    public void setShoppingCart(ShoppingCartEntity shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    /**
     * @return the newValue
     */
    public int getNewValue() {
        return newValue;
    }

    /**
     * @param newValue the newValue to set
     */
    public void setNewValue(int newValue) {
        this.newValue = newValue;
    }

    /**
     * @return the noItem
     */
    public boolean isNoItem() {
        return noItem;
    }

    /**
     * @param noItem the noItem to set
     */
    public void setNoItem(boolean noItem) {
        this.noItem = noItem;
    }
    
}

