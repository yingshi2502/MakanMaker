/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AddressControllerLocal;
import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.MealKitControllerLocal;
import ejb.session.stateless.OrderControllerLocal;
import entity.AddressEntity;
import entity.CustomerEntity;
import entity.MealKitEntity;
import entity.OrderEntity;
import entity.ShoppingCartEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import util.exception.MealKitNotFoundException;

/**
 *
 * @author Summer
 */
@Named(value = "paymentManagedBean")
@ViewScoped
public class PaymentManagedBean implements Serializable{

    @EJB(name = "MealKitControllerLocal")
    private MealKitControllerLocal mealKitController;

    /**
     * Creates a new instance of PaymentManagedBean
     */
    
    @EJB(name = "CustomerControllerLocal")
    private CustomerControllerLocal customerControllerLocal;

    @EJB(name = "OrderControllerLocal")
    private OrderControllerLocal orderControllerLocal;

    @EJB(name = "AddressControllerLocal")
    private AddressControllerLocal addressControllerLocal;
    
    

    private CustomerEntity currentCustomer;
    private List<AddressEntity> allAddresses;
    private AddressEntity defaultAddress;
    private AddressEntity selectedAddress;
    private List<OrderEntity> currOrders;
    private ShoppingCartEntity shoppingCart;
    private double subTotalPrice;
    private double shippingFees;
    private double totalPrice;
    
    public PaymentManagedBean() {
        allAddresses = new ArrayList<>();
        currOrders = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct(){
        setAllAddresses();
        
        this.setCurrentCustomer((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity"));
        
        if (currentCustomer==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Login!", null));
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AddressManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.setShoppingCart(currentCustomer.getShoppingCart());
        try {
            this.setCurrOrders(shoppingCart);
        } catch (MealKitNotFoundException ex) {
            Logger.getLogger(PaymentManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setSubTotalPrice();
        this.shippingFees=0;
        this.setTotalPrice();
        
    }
    
    public CustomerEntity getCurrentCustomer() {
        return currentCustomer;
    }

    /**
     * @param currentCustomer the currentCustomer to set
     */
    public void setCurrentCustomer(CustomerEntity currentCustomer) {
        this.currentCustomer = currentCustomer;
    }
    
    public AddressEntity getDefaultAddress() {
        return defaultAddress;
    }

    /**
     * @param defaultAddress the defaultAddress to set
     */
    public void setDefaultAddress(AddressEntity defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public List<AddressEntity> getAllAddresses() {
        return allAddresses;
    }

//    public void setAllAddresses(List<AddressEntity> allAddresses) {
//        this.allAddresses = allAddresses;
//    }
    
    public void setAllAddresses() {
        AddressEntity address1 = new AddressEntity("111111", "Siglap", "07-12",Boolean.TRUE, Boolean.FALSE, "","");
        AddressEntity address2 = new AddressEntity("222222", "NUS", "07-12",Boolean.FALSE, Boolean.FALSE, "","");
        AddressEntity address3 = new AddressEntity("333333", "NTU", "07-12",Boolean.FALSE, Boolean.FALSE, "","");
        this.allAddresses.add(address1);
        this.allAddresses.add(address2);
        this.allAddresses.add(address3);
        
    }

    public AddressEntity getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(ActionEvent event) {
        //this.selectedAddress = selectedAddress;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Address added successfully (Product ID: " + selectedAddress.getPostalCode() + ")", null));
        this.setShippingFees();
    }
    
    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Address Selected", ((AddressEntity) event.getObject()).getPostalCode());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
 
    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage("Address Unselected", ((AddressEntity) event.getObject()).getPostalCode());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<OrderEntity> getCurrOrders() {
        return currOrders;
    }

    public void setCurrOrders(ShoppingCartEntity shoppingCart) throws MealKitNotFoundException {
        
        int size = shoppingCart.getMealKits().size();
        double unitPrice;
        MealKitEntity mealKit;
        int mealKitQuantity;
        for (int i=0; i<size; i++){
            mealKit = mealKitController.retrieveMealKitById(shoppingCart.getMealKits().get(i));
            mealKitQuantity = shoppingCart.getQuantity().get(i);
            unitPrice = mealKit.getPrice();
            OrderEntity newOrder = new OrderEntity();
            newOrder.setCustomer(currentCustomer);
            newOrder.setMealKit(mealKit);
            newOrder.setQuantity(mealKitQuantity);
            newOrder.setTotalAmount(unitPrice*mealKitQuantity);
            currOrders.add(newOrder);
        }
    }

    public double getSubTotalPrice() {
        return subTotalPrice;
    }

    public void setSubTotalPrice() {
        int size = currOrders.size();
        double price =0;
        for (int i=0; i<size; i++){
            price += currOrders.get(i).getTotalAmount();
        }
        this.subTotalPrice = price;
    }

    public double getShippingFees() {
        return shippingFees;
    }

    public void setShippingFees() {
        this.shippingFees = ((int) (Math.random()*10));
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice() {
        this.totalPrice = this.shippingFees+this.subTotalPrice;
    }

    public ShoppingCartEntity getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCartEntity shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
    
    
}
