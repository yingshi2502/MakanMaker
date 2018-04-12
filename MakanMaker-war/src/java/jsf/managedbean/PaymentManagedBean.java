/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.EmailControllerLocal;
import ejb.session.stateless.OrderControllerLocal;
import ejb.session.stateless.ShoppingCartControllerLocal;
import entity.AddressEntity;
import entity.CustomerEntity;
import entity.OrderEntity;
import entity.ShoppingCartEntity;
import entity.TransactionEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.enumeration.PaymentTypeEnum;
import util.exception.OrderNotFoundException;
import util.helperClass.CreditCardWrapper;


@Named(value = "paymentManagedBean")
@ViewScoped
public class PaymentManagedBean implements Serializable {

    @EJB(name = "EmailControllerLocal")
    private EmailControllerLocal emailControllerLocal;

    @EJB(name = "ShoppingCartControllerLocal")
    private ShoppingCartControllerLocal shoppingCartController;

    

    @EJB(name = "OrderControllerLocal")
    private OrderControllerLocal orderControllerLocal;


    private CustomerEntity currentCustomer;

    private List<OrderEntity> orders;
    private List<TransactionEntity> transactions;
    
    private ShoppingCartEntity shoppingCart;
    private AddressEntity deliveryAdd;
    private CreditCardWrapper creditCard;

    public PaymentManagedBean() {
        orders = new ArrayList<>();
        creditCard = new CreditCardWrapper();
        transactions = new ArrayList<>();
        System.err.println("***** Constructor payment management bean********");

    }

    @PostConstruct
    public void postConstruct() {
        System.err.println("***** payment management bean********");
        this.setCurrentCustomer((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity"));
        this.setDeliveryAdd((AddressEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedAddressEntity"));
        this.setOrders((List<OrderEntity>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("newOrders"));
        System.err.println("***** order size in payment management bean"+orders.size());
    }

    
    public void paypal(ActionEvent event){
        System.err.println("****PAY PAL");

        payForOrder(PaymentTypeEnum.PAYPAL);
    }
    
    public void creditCard(ActionEvent event){
        payForOrder(PaymentTypeEnum.CREDITCARD);
    }
    
    public void cash(ActionEvent event){
        payForOrder(PaymentTypeEnum.CASHONDELIVERY);
    }
    
    private void payForOrder(PaymentTypeEnum pt){
        try {
            System.err.println("****just in Pay For Order "+pt);
            TransactionEntity t;
            List<OrderEntity> newStatusOrders = new ArrayList<>();
            for (OrderEntity o: orders){
                System.err.println("***Pay for order "+pt+" "+o.getOrderId());
                try {
                    t = orderControllerLocal.payForOrder(o.getOrderId(), pt);
                    System.err.println("******transaction"+t.getTransactionCode());
                    newStatusOrders.add(orderControllerLocal.retrieveOrderById(o.getOrderId()));
                    getTransactions().add(t);
                    
                } catch (OrderNotFoundException ex) {
                }
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().replace("newOrders", null);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paidOrders", newStatusOrders);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("transactions", getTransactions());
            System.err.println("****just before clear shopping cart "+currentCustomer.getCustomerId());
            shoppingCartController.clearShoppingCart(currentCustomer.getCustomerId());
            emailControllerLocal.emailSummary(shoppingCartController.calculatePriceByCartId(currentCustomer.getShoppingCart().getShoppingCartId()), "MakanMaker@mm.com", currentCustomer.getEmail());
            redirect();
        } catch (InterruptedException ex) {
            Logger.getLogger(PaymentManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void redirect(){
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(context.getApplicationContextPath() + "/checkout/payConfirmed.xhtml");
        } catch (IOException ex) {
            System.err.println("***** redirect failed");
            Logger.getLogger(PaymentManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    /**
     * @return the orders
     */
    public List<OrderEntity> getOrders() {
        return orders;
    }

    /**
     * @param orders the orders to set
     */
    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
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
     * @return the deliveryAdd
     */
    public AddressEntity getDeliveryAdd() {
        return deliveryAdd;
    }

    /**
     * @param deliveryAdd the deliveryAdd to set
     */
    public void setDeliveryAdd(AddressEntity deliveryAdd) {
        this.deliveryAdd = deliveryAdd;
    }

    /**
     * @return the creditCard
     */
    public CreditCardWrapper getCreditCard() {
        return creditCard;
    }

    /**
     * @param creditCard the creditCard to set
     */
    public void setCreditCard(CreditCardWrapper creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * @return the transactions
     */
    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    /**
     * @param transactions the transactions to set
     */
    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }


}
