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
import java.io.Serializable;
import java.util.ArrayList;
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
    private OrderEntity currentOrder;
    private boolean noOrder;

    public MyOrderManagedBean() {
        orders = new ArrayList<>();
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

    public Integer getCurrOrderStatusStep() {
        int activeIndex = -1;
        switch (currentOrder.getOrderStatus().toString()) {
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
     * @return the currentOrder
     */
    public OrderEntity getCurrentOrder() {
        return currentOrder;
    }

    /**
     * @param currentOrder the currentOrder to set
     */
    public void setCurrentOrder(OrderEntity currentOrder) {
        this.currentOrder = currentOrder;
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

}
