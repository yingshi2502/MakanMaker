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
import ejb.session.stateless.ShoppingCartControllerLocal;
import entity.AddressEntity;
import entity.CustomerEntity;
import entity.MealKitEntity;
import entity.OrderEntity;
import entity.ShoppingCartEntity;
import entity.TransactionEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import util.enumeration.PaymentTypeEnum;
import util.exception.MealKitNotFoundException;

/**
 *
 * @author Summer
 */
@Named(value = "paymentManagedBean")
@ViewScoped
public class PaymentManagedBean implements Serializable{

    @EJB(name = "ShoppingCartControllerLocal")
    private ShoppingCartControllerLocal shoppingCartController;

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
    private ScheduleModel eventModel;
    private ScheduleEvent event;
    private Date date1;
    private OrderEntity order;
    //connect with order here...
    
    public PaymentManagedBean() {
        allAddresses = new ArrayList<>();
        currOrders = new ArrayList<>();
        eventModel = new DefaultScheduleModel();
        event = new DefaultScheduleEvent();
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
        
        System.err.println("Customer "+currentCustomer.getFullName());
        shoppingCart = customerControllerLocal.retrieveShoppingCartByCustomerId(currentCustomer.getCustomerId());
        //to be deleted after shopping cart code up.
        shoppingCart = shoppingCartController.addItem(1l, 2, shoppingCart.getShoppingCartId());
        
        System.err.println("Shopping cart belongs to "+shoppingCart.getCustomer().getFullName());
        try {
            setCurrOrders(shoppingCart);
            System.err.println(currOrders.size());
        } catch (MealKitNotFoundException ex) {
            Logger.getLogger(PaymentManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setSubTotalPrice();
        this.shippingFees=0;
        this.setTotalPrice();
        System.err.println("Price:"+totalPrice);
        eventModel.addEvent(new DefaultScheduleEvent("MealKit1",today6Pm(),nextDay9Am()));
        
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
    
    //for select address
    
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
        //this.selectedAddress = (AddressEntity) event.getComponent().getAttributes().get("")
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Address added successfully (Product ID: " + selectedAddress.getPostalCode() + ")", null));
        this.setShippingFees();
        System.err.println("address id = "+selectedAddress.getAddressId());
    }
    
    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Address Selected", ((AddressEntity) event.getObject()).getPostalCode());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
 
    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage("Address Unselected", ((AddressEntity) event.getObject()).getPostalCode());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    //for order summary
    
    public List<OrderEntity> getCurrOrders() {
        return currOrders;
    }

    public void setCurrOrders(ShoppingCartEntity shoppingCart) throws MealKitNotFoundException {
        
        int size = shoppingCart.getMealKits().size();
        double unitPrice;
        MealKitEntity mealKit;
        int mealKitQuantity;
        
        System.err.println("shoppingCar"+size);
        for (int i=0; i<size; i++){
            System.err.println("&&&&&inside"+i);
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
    
    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
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
    
    // for select schedule
    
    public ScheduleModel getEventModel() {
        return eventModel;
    }
    
    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }
     //test
    public Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }
    //test
    private Date today6Pm() {
        Calendar t = (Calendar) today().clone(); 
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 6);
         
        return t.getTime();
    }
     //test
    private Date nextDay9Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 9);
         
        return t.getTime();
    }
     
    public ScheduleEvent getEvent() {
        return event;
    }
 
    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }
    
    public void addEvent(ActionEvent actionEvent) {
        
        if(event.getId() == null)
            eventModel.addEvent(event);
        else
            eventModel.updateEvent(event);
        
        event = new DefaultScheduleEvent();
        order.setDeliveryDate(event.getStartDate());
        
    }
     
    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta());
         
        addMessage(message);
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void onOrderSelected(){
        //event. = order.getMealKit().getName();
        ScheduleEvent newEvent = new DefaultScheduleEvent("asdfadsf", event.getStartDate(), event.getEndDate());
        event = newEvent;
        System.err.println("a##### on order select AJAX");
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }
    //for payment
    
    public void confirmPayment(){
//        order = orderControllerLocal.createNewOrder(order, order.getCustomer().getCustomerId(), order.getMealKit().getMealKitId(), order.getAddress().getAddressId());
//        TransactionEntity transactionEntity = orderControllerLocal.payForOrder(order, paymentType);
    }
    
    private PaymentTypeEnum paymentType;
    /*public void tabChange(TabChangeEvent event){
        String tabId = event.getTab().getId();
        switch(tabId){
            case "0":
                paymentType = PaymentTypeEnum.PAYPAL;
                break;
            case "1":
                paymentType = PaymentTypeEnum.CREDITCARD;
                break;
            case "2":
                paymentType = PaymentTypeEnum.CASHONDELIVERY;
                break;
            default:
                break;
        }
    }*/
    
}
