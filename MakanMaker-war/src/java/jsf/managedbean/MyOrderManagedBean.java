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
import entity.TagEntity;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import util.exception.EmptyListException;
import util.enumeration.OrderStatusEnum;

/**
 *
 * @author yingshi
 */
@Named
@ViewScoped
public class MyOrderManagedBean implements Serializable {

    @EJB(name = "OrderControllerLocal")
    private OrderControllerLocal orderControllerLocal;

    private List<OrderEntity> orders;
    private boolean noOrder;
    private ReviewEntity newReview;
    private List<String> statusNames;
    private List<OrderEntity> filteredOrders;
    private String selectedType;

    public MyOrderManagedBean() {
        orders = new ArrayList<>();
        newReview = new ReviewEntity();
        filteredOrders = new ArrayList<>();
        statusNames = new ArrayList<>();

    }

    @PostConstruct
    public void postConstruct() {
        CustomerEntity currCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
        if (currCustomer == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please Login", null));
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/index.xhtml");
//                context.redirect(context.getApplicationContextPath() + "/index.xhtml");
            } catch (IOException ex1) {
                Logger.getLogger(WishListManagedBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } else {
            try {
                setOrders(orderControllerLocal.retrieveOrderByCustomerId(currCustomer.getCustomerId()));
                makeStatusName();
                for (OrderEntity o : orders) {
                    if (o.getOrderStatus().equals(OrderStatusEnum.PREPARING)){
                        filteredOrders.add(o);
                    }
                }
            } catch (EmptyListException ex) {
                setNoOrder(true);
                makeStatusName();
                for (OrderEntity o : orders) {
                    filteredOrders.add(o);
                }
            } catch (NullPointerException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please Login", null));
                try {
                    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                    context.redirect(context.getApplicationContextPath() + "/index.xhtml");
                } catch (IOException ex1) {
                    Logger.getLogger(WishListManagedBean.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }

    public boolean filterByStatus(Object value, Object filter, Locale locale) {

        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.isEmpty()) {
            return true;
        }
        if (value == null) {
            return false;
        }

        String selected = filter.toString();
        System.err.println("*****order filter" + selected);
        String orderStatus = ((OrderStatusEnum) value).name();
        System.err.println("*****order value" + orderStatus);
        System.err.println(selected.compareToIgnoreCase(orderStatus) == 0);
        return selected.compareToIgnoreCase(orderStatus) == 0;
    }

    public void onSelectOrderType() {
        System.err.println("****selected Type" + selectedType);
        filteredOrders.clear();
        for (OrderEntity o : orders) {
            if (selectedType == null || (getLcOrderStatus(o.getOrderStatus())).equals(selectedType)) {
                filteredOrders.add(o);
            }
        }
    }

    public String getDeliveryDate(Date date) {
        SimpleDateFormat ft1 = new SimpleDateFormat("dd-MMM-yyyy");
        return ft1.format(date);
    }
    
    public String getPurchasingDT(Date date){
        SimpleDateFormat ft1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm");
        return ft1.format(date);
    }

    public String getLcOrderStatus(OrderStatusEnum status) {
        switch (status.toString()) {
            case "PREPARING":
                return "Preparing";
            case "DELIVERING":
                return "Deliverying";
            case "DELIVERED":
                return "Delivered";
            case "RECEIVED":
                return "Received";
            case "UNPAID":
                return "Unpaid";
            default:
                return "Refunded";

        }
    }

    private void makeStatusName() {
        statusNames.add("Preparing");
        statusNames.add("Deliverying");
        statusNames.add("Delivered");
        statusNames.add("Received");
        statusNames.add("Refunded");
        statusNames.add("Unpaid");
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

    /**
     * @return the statusNames
     */
    public List<String> getStatusNames() {
        return statusNames;
    }

    /**
     * @param statusNames the statusNames to set
     */
    public void setStatusNames(List<String> statusNames) {
        this.statusNames = statusNames;
    }

    /**
     * @return the filteredOrders
     */
    public List<OrderEntity> getFilteredOrders() {
        return filteredOrders;
    }

    /**
     * @param filteredOrders the filteredOrders to set
     */
    public void setFilteredOrders(List<OrderEntity> filteredOrders) {
        this.filteredOrders = filteredOrders;
    }

    /**
     * @return the selectedType
     */
    public String getSelectedType() {
        return selectedType;
    }

    /**
     * @param selectedType the selectedType to set
     */
    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

}
