/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OrderControllerLocal;
import ejb.session.stateless.ReviewControllerLocal;
import entity.CustomerEntity;
import entity.MealKitEntity;
import entity.OrderEntity;
import entity.ReviewEntity;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import util.enumeration.OrderStatusEnum;
import util.enumeration.PaymentTypeEnum;
import util.exception.OrderNotFoundException;

/**
 *
 * @author yingshi
 */
@Named(value = "orderDetailManagedBean")
@ViewScoped
public class OrderDetailManagedBean implements Serializable {

    @EJB(name = "ReviewControllerLocal")
    private ReviewControllerLocal reviewControllerLocal;

    @EJB(name = "OrderControllerLocal")
    private OrderControllerLocal orderControllerLocal;

    private OrderEntity currOrder;
    private CustomerEntity currCustomer;
    private ReviewEntity newReview;
    private MealKitEntity mealKit;
    private boolean isAnomy;
    private String refundDescription;
    private String refundPaymentMethod;
    private boolean CheckedRefundPolicy;
    private Long orderId;
    private boolean canRefund;

    public OrderDetailManagedBean() {
        newReview = new ReviewEntity();
        newReview.setRating(0);
    }

    @PostConstruct
    public void postConstruct() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String value = request.getParameter("id");
        orderId = Long.valueOf(value);
        String order = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isAuthorized", false);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("isAuthorized", false);
        CheckedRefundPolicy = false;
        System.err.println("***id" + orderId + " " + value + " " + order);
        
        
        try {
            currOrder = orderControllerLocal.retrieveOrderById(orderId);
            currCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
            mealKit = currOrder.getMealKit();
            
            canRefund = currOrder.getOrderStatus().equals(OrderStatusEnum.PREPARING);
            
            System.err.println("***adfasdfasdfasdfads");
            if (currCustomer==null){
                redirectTo404();
            }else{
            if (currOrder.getCustomer().getCustomerId().equals(currCustomer.getCustomerId())) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isAuthorized", true);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("isAuthorized", true);
                System.err.println("***"+currOrder.getCustomer().getCustomerId()+" "+currCustomer.getCustomerId());
            } else {
                redirectTo404();
            }
            }
        } catch (OrderNotFoundException ex) {
            try {        
                redirectTo404();
            } catch (IOException ex1) {
                Logger.getLogger(OrderDetailManagedBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ex) {
            Logger.getLogger(OrderDetailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void submitReview(ActionEvent event){
        if (isAnomy){
            newReview.setReviewer("Anonymous");
        }else{
            newReview.setReviewer(currCustomer.getUserName());
        }
        reviewControllerLocal.createNewReiview(newReview.getReviewer(),currOrder.getOrderId(), currOrder.getMealKit().getMealKitId(), newReview.getReview(), newReview.getRating());
        currOrder.setIsReviewed(true);
        newReview = new ReviewEntity();
        
        FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Review Submitted", null));

    }
    
    
    public void submitRefund(ActionEvent event){
        if (CheckedRefundPolicy){
            orderControllerLocal.refundOrder(currOrder.getOrderId(), refundDescription,getPaymentMethodEnum(refundPaymentMethod));
            canRefund = false;
        }else{
            FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please accept the refund policy", null));
        }
    }

    public void confirmReceipt(ActionEvent event){
        currOrder.setOrderStatus(OrderStatusEnum.RECEIVED);
        orderControllerLocal.updateOrder(currOrder);
    }
    
    private PaymentTypeEnum getPaymentMethodEnum(String value){
        switch(value){
            case "1": return PaymentTypeEnum.PAYPAL;
            case "2": return PaymentTypeEnum.CREDITCARD;
        }
        return null;
    }
    
    public String getRefundDescription() {
        return refundDescription;
    }

    public void setRefundDescription(String refundDescription) {
        this.refundDescription = refundDescription;
    }
    
   
    public double getSubtotal(){
        double price = mealKit.getPrice();
        return price * currOrder.getQuantity();
    }
    
    public String getPurchasingDate(){
        SimpleDateFormat ft1= new SimpleDateFormat("dd-MMM-yyyy");
        return ft1.format(currOrder.getPurchasingDate());
    }
    
    public String getDeliveryDate(){
        SimpleDateFormat ft1= new SimpleDateFormat("dd-MMM-yyyy");
        return ft1.format(currOrder.getDeliveryDate());
    }
    
    public String getLastReturnDate(){
        SimpleDateFormat ft1= new SimpleDateFormat("dd-MMM-yyyy");
        return ft1.format(currOrder.getDeliveryDate());
    }
    
    
    public Integer getCurrOrderStatusStep() {
        int activeIndex = -1;
        switch (currOrder.getOrderStatus().toString()) {
            case "PREPARING":
                activeIndex = 0;
                break;
            case "DELIVERING":
                activeIndex = 1;
                break;
            case "DELIVERED":
                activeIndex = 2;
                break;
            case "RECEIVED":
                activeIndex = 3;
                break;
            default:
                break;

        }
        return activeIndex;
    }

    public void setCurrOrder(OrderEntity currOrder) {
        this.currOrder = currOrder;
    }

    public void setCurrCustomer(CustomerEntity currCustomer) {
        this.currCustomer = currCustomer;
    }

    public void setNewReview(ReviewEntity newReview) {
        this.newReview = newReview;
    }

    public OrderEntity getCurrOrder() {
        return currOrder;
    }

    public CustomerEntity getCurrCustomer() {
        return currCustomer;
    }

    public ReviewEntity getNewReview() {
        return newReview;
    }

    public MealKitEntity getMealKit() {
        return mealKit;
    }

    public void setMealKit(MealKitEntity mealKit) {
        this.mealKit = mealKit;
    }
    
    
    private void redirectTo404() throws IOException{
                System.err.println("***adfasdfasdfasdfads");

        FacesContext.getCurrentInstance().getExternalContext().redirect("/MakanMaker-war/error404.xhtml");
    }

    /**
     * @return the orderId
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the isAnomy
     */
    public boolean isIsAnomy() {
        return isAnomy;
    }

    /**
     * @param isAnomy the isAnomy to set
     */
    public void setIsAnomy(boolean isAnomy) {
        this.isAnomy = isAnomy;
    }

    /**
     * @return the CheckedRefundPolicy
     */
    public boolean isCheckedRefundPolicy() {
        return CheckedRefundPolicy;
    }

    /**
     * @param CheckedRefundPolicy the CheckedRefundPolicy to set
     */
    public void setCheckedRefundPolicy(boolean CheckedRefundPolicy) {
        System.err.println("***Order MB: Modified CheckedRefundPolicy");
        this.CheckedRefundPolicy = CheckedRefundPolicy;
    }

    /**
     * @return the canRefund
     */
    public boolean isCanRefund() {
        return canRefund;
    }

    /**
     * @param canRefund the canRefund to set
     */
    public void setCanRefund(boolean canRefund) {
        this.canRefund = canRefund;
    }

    /**
     * @return the refundPaymentMethod
     */
    public String getRefundPaymentMethod() {
        return refundPaymentMethod;
    }

    /**
     * @param refundPaymentMethod the refundPaymentMethod to set
     */
    public void setRefundPaymentMethod(String refundPaymentMethod) {
        this.refundPaymentMethod = refundPaymentMethod;
    }

}
