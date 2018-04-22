/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EmailControllerLocal;
import entity.AddressEntity;
import entity.CustomerEntity;
import entity.OrderEntity;
import entity.TransactionEntity;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
 * @author yingshi
 */
@Named(value = "paymentConfirmManagedBean")
@ViewScoped
public class PaymentConfirmManagedBean implements Serializable{

    @EJB(name = "EmailControllerLocal")
    private EmailControllerLocal emailControllerLocal;
    
    private List<OrderEntity> orders;
    private List<TransactionEntity> transactions;
    private CustomerEntity currentCustomer;
    private AddressEntity deliveryAdd;
    private double totalPrice;
    private double subTotal;
    
    /**
     * Creates a new instance of PaymentConfirmManagedBean
     */
    public PaymentConfirmManagedBean() {
        orders = new ArrayList<>();
        transactions = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct(){
        
        this.setDeliveryAdd((AddressEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedAddressEntity"));
        this.setCurrentCustomer((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity"));
        this.setOrders((List<OrderEntity>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("paidOrders"));
        this.setTransactions((List<TransactionEntity>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("transactions"));
        subTotal = calculateSubTotal();
        totalPrice = subTotal + deliveryAdd.getShippingFee();
        sendEmailMsg();
    }   
    
    private double calculateSubTotal(){
        double sum = 0.0;
        System.err.println("******** order size"+orders.size());
        for (OrderEntity o: orders){
            System.err.println("*****"+o.getOrderStatus());
            sum += o.getTotalAmount();
        }
        return sum;
    }
    
    public String getDeliveryDate(Date date) {
        SimpleDateFormat ft1 = new SimpleDateFormat("dd-MMM-yyyy");
        return ft1.format(date);
    }
    
    private void sendEmailMsg(){
        FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "An email is sent~", "Please Check your email box"));
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
     * @return the totalPrice
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
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
