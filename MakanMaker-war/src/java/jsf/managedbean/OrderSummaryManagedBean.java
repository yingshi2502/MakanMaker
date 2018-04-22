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
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.helperClass.CartItemWrapper;

/**
 *
 * @author yingshi
 */
@Named(value = "orderSummaryManagedBean")
@ViewScoped
public class OrderSummaryManagedBean implements Serializable{

    @EJB
    private CustomerControllerLocal customerController;

    @EJB
    private ShoppingCartControllerLocal shoppingCartController;

    private ShoppingCartEntity shoppingCart;
    private List<CartItemWrapper> items;
    private List<MealKitEntity> mealKits;
    private CustomerEntity currentCustomer;
    
    private double subTotal;

    
    public OrderSummaryManagedBean() {
        mealKits = new ArrayList<>();
        items = new ArrayList<>();
        
    }

    
    @PostConstruct
    public void postConstruct(){
        setCurrentCustomer((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity"));
        
        if (getCurrentCustomer()==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Login!", null));
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AddressManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        setShoppingCart(customerController.retrieveShoppingCartByCustomerId(getCurrentCustomer().getCustomerId(),false));
        setSubTotal(shoppingCartController.calculatePriceByCartId(shoppingCart.getShoppingCartId()));
        mealKits = shoppingCartController.retrieveMealKitsByCustomerId(getShoppingCart().getShoppingCartId(),false);
        
        int i=0;
        List<Integer> qs = getShoppingCart().getQuantity();
        items = new ArrayList<>();
        for (MealKitEntity m: mealKits){
            items.add(new CartItemWrapper(qs.get(i),m));
            i++;
        }
        System.err.println("Customer "+getCurrentCustomer().getFullName());
        
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
     * @return the mealKits
     */
    public List<MealKitEntity> getMealKits() {
        return mealKits;
    }

    /**
     * @param mealKits the mealKits to set
     */
    public void setMealKits(List<MealKitEntity> mealKits) {
        this.mealKits = mealKits;
    }

    /**
     * @return the currentCustomer
     */
    public CustomerEntity getCurrentCustomer() {
        return currentCustomer;
    }

    /**
     * @param currentCustomer the currentCustomer to set
     */
    public void setCurrentCustomer(CustomerEntity currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    /**
     * @return the subTotal
     */
    public double getSubTotal() {
        return subTotal;
    }

    /**
     * @param subTotal the subTotal to set
     */
    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
    
}
