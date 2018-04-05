/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OrderControllerLocal;
import entity.AddressEntity;
import entity.CustomerEntity;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import entity.OrderEntity;
import entity.ReviewEntity;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import util.exception.EmptyListException;
import util.enumeration.OrderStatusEnum;

/**
 *
 * @author yingshi
 */
@Named
@ViewScoped
public class MyOrderManagedBean implements Serializable{

    @EJB(name = "OrderControllerLocal")
    private OrderControllerLocal orderControllerLocal;

    private List<OrderEntity> orders;
    private boolean noOrder;
    private ReviewEntity newReview;

    public MyOrderManagedBean() {
        orders = new ArrayList<>();
        newReview = new ReviewEntity();
    }

    @PostConstruct
    public void postConstruct() {
        CustomerEntity currCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
        try {
            setOrders(orderControllerLocal.retrieveOrderByCustomerId(currCustomer.getCustomerId()));
        } catch (EmptyListException ex) {
            setNoOrder(true);
        }
    }

    public String getDeliveryDate(Date date){
        SimpleDateFormat ft1= new SimpleDateFormat("dd-MMM-yyyy");
        return ft1.format(date);
    }
    
    public String getLcOrderStatus(OrderStatusEnum status){
        switch (status.toString()) {
            case "PREPARING":
                return "Preparing";
            case "DELIVERING":
                return "Deliverying";
            case "DELIVERED":
                return "Delivered";
            case "RECEIVED":
                return "Reveived";
            default:
                return "Refunded";

        }
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
     * @return the noOrder
     */
    public boolean isNoOrder() {
        return noOrder;
    }

    /**
     * @param noOrder the noOrder to set
     */
    public void setNoOrder(boolean noOrder) {
        this.noOrder = noOrder;
    }

    /**
     * @return the newReview
     */
    public ReviewEntity getNewReview() {
        return newReview;
    }

    /**
     * @param newReview the newReview to set
     */
    public void setNewReview(ReviewEntity newReview) {
        this.newReview = newReview;
    }

}
