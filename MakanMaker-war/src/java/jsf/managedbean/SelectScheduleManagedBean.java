/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.OrderControllerLocal;
import ejb.session.stateless.ShoppingCartControllerLocal;
import entity.AddressEntity;
import entity.CustomerEntity;
import entity.MealKitEntity;
import entity.OrderEntity;
import entity.ShoppingCartEntity;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import util.enumeration.OrderStatusEnum;
import util.helperClass.CartItemWrapper;

/**
 *
 * @author yingshi
 */
@Named(value = "selectScheduleManagedBean")
@ViewScoped
public class SelectScheduleManagedBean implements Serializable{

    @EJB
    private OrderControllerLocal orderController;

    @EJB
    private CustomerControllerLocal customerController;

   
    @EJB
    private ShoppingCartControllerLocal shoppingCartController;
    private String extraRequest;
    private List<MealKitEntity> mealKits;
    private CustomerEntity currentCustomer;
    private ShoppingCartEntity shoppingCart;
    private List<CartItemWrapper> items;
    private ScheduleModel eventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private AddressEntity addressToShip;
    private double shippingFee;
    private List<OrderEntity> orders;
    
    public SelectScheduleManagedBean() {
        mealKits = new ArrayList<>();
        items = new ArrayList<>();
        addressToShip = new AddressEntity();
        orders = new ArrayList<>();
        extraRequest="#";
    }

    @PostConstruct
    public void postConstruct(){
        currentCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
        setAddressToShip((AddressEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedAddressEntity"));
        
        System.err.println("Customer "+currentCustomer.getFullName());
        shoppingCart = customerController.retrieveShoppingCartByCustomerId(currentCustomer.getCustomerId(),false);
        
        //to be deleted after shopping cart code up.
        //System.err.println("shopping cart "+shoppingCart.sho);
        //shoppingCart = shoppingCartController.addItem(1l, 2, shoppingCart.getShoppingCartId());
        mealKits = shoppingCartController.retrieveMealKitsByCustomerId(shoppingCart.getShoppingCartId(),false);
        System.err.println("****address "+addressToShip.getPostalCode());
        int i=0;
        List<Integer> qs = shoppingCart.getQuantity();
        for (MealKitEntity m: mealKits){
            items.add(new CartItemWrapper(qs.get(i),m));
            i++;
        }
        ScheduleEvent testEvent;
        eventModel = new DefaultScheduleModel();
        
        i=0;
        for (CartItemWrapper c: items){
            testEvent = new DefaultScheduleEvent(c.getMk().getName(), getDate(i), getDate(i), c);
            i++;
            eventModel.addEvent(testEvent);
        }
                 
    }
    
    public void addEvent(ActionEvent actionEvent) {
        System.err.println("***inside add Event");

//        if(event.getId() == null){
//            eventModel.addEvent(event);
//        } 
//        else{
//            eventModel.updateEvent(event);
//        }
            
        CartItemWrapper c =(CartItemWrapper) event.getData();
        System.err.println("***convert wrapper" + c.getMk().getName());
        c.setExtraRequest(extraRequest);
        DefaultScheduleEvent newEvent = new DefaultScheduleEvent(event.getTitle(), event.getStartDate(), event.getEndDate(), c);
        System.err.println("***convert new c" + newEvent.getData());
        eventModel.deleteEvent(event);
        eventModel.addEvent(newEvent);
        extraRequest = "#";
        event = new DefaultScheduleEvent();
        System.err.println("***stilll ok");
    }
    
    public void confirmSchedule(ActionEvent event){
        System.err.println("***inside confirm schedule");
        List<ScheduleEvent> events = eventModel.getEvents();
        System.err.println("*** order size"+events.size());

        int i=1; //for orderNumber specification
        for (ScheduleEvent e: events){
            CartItemWrapper item = (CartItemWrapper)e.getData();
            double totalAmount = item.getQuantity() * item.getMk().getPrice();
            OrderEntity order = new OrderEntity(totalAmount, item.getQuantity(), new Date(), e.getStartDate(), OrderStatusEnum.UNPAID, String.valueOf((char)(i + 64)), item.getExtraRequest(), addressToShip.getShippingFee()/events.size());
            order = orderController.createNewOrder(order, currentCustomer.getCustomerId(), item.getMk().getMealKitId(), addressToShip.getAddressId(),false);
            orders.add(order);
            i++;
        }
        System.err.println("*** order size"+orders.size());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("newOrders", orders);
    }
    
    private Date todayDate = new Date();
    
    public Date getTodayDate(){
        return todayDate;
    }
    
    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }
    
    private Date getDate(int i) {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.DATE, t.get(Calendar.DATE) + i);   
         
        return t.getTime();
    }
    
    
    public void onEventSelect(SelectEvent selectEvent) {
        setEvent((ScheduleEvent) selectEvent.getObject());
        setExtraRequest(((CartItemWrapper)event.getData()).getExtraRequest());
    }
    
    public void onDateSelect(SelectEvent selectEvent) {
        setEvent(new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject()));
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Set Delivery Date for "+event.getScheduleEvent().getTitle(), "Date:" + sf.format(event.getScheduleEvent().getStartDate()));
         
        addMessage(message);
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
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
     * @return the eventModel
     */
    public ScheduleModel getEventModel() {
        return eventModel;
    }

    /**
     * @param eventModel the eventModel to set
     */
    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    /**
     * @return the event
     */
    public ScheduleEvent getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    /**
     * @return the addressToShip
     */
    public AddressEntity getAddressToShip() {
        return addressToShip;
    }

    /**
     * @param addressToShip the addressToShip to set
     */
    public void setAddressToShip(AddressEntity addressToShip) {
        this.addressToShip = addressToShip;
    }

    /**
     * @return the shippingFee
     */
    public double getShippingFee() {
        return shippingFee;
    }

    /**
     * @param shippingFee the shippingFee to set
     */
    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
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
     * @return the extraRequest
     */
    public String getExtraRequest() {
        return extraRequest;
    }

    /**
     * @param extraRequest the extraRequest to set
     */
    public void setExtraRequest(String extraRequest) {
        this.extraRequest = extraRequest;
    }
    
}
