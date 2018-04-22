/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;


import entity.AddressEntity;
import entity.CustomerEntity;
import entity.MealKitEntity;
import entity.OrderEntity;
import entity.TransactionEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.OrderStatusEnum;
import util.enumeration.PaymentTypeEnum;
import util.enumeration.TransactionTypeEnum;
import util.exception.EmptyListException;
import util.exception.GeneralException;
import util.exception.OrderExistException;
import util.exception.OrderNotFoundException;
import javax.ejb.Timer;


/**
 *
 * @author Summer
 */
@Stateless
public class OrderController implements OrderControllerLocal {

    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;
    
    @Resource
    TimerService timerService;
    
    
    public OrderController() {
    }

    @Override
    public OrderEntity createNewOrder(OrderEntity order, Long customerId, Long mealKitId, Long addressId, boolean mobile) {
        
            SimpleDateFormat sf = new SimpleDateFormat("ssmmhhddMMyy");
            order.setOrderNumber(customerId+sf.format((new Date()))+mealKitId+order.getOrderNumber());    
            em.persist(order);
            
            CustomerEntity customer = em.find(CustomerEntity.class, customerId);
            MealKitEntity mealKit = em.find(MealKitEntity.class, mealKitId);
            AddressEntity addressToDelivery = em.find(AddressEntity.class, addressId);
            
            order.setCustomer(customer);
            order.setMealKit(mealKit);
            
            order.setAddress(addressToDelivery);
            addressToDelivery.getOrders().add(order);
            System.err.println("****Order Add Address"+addressToDelivery.getOrders().size());
            customer.getOrderHistory().add(order);
            System.err.println("****Order Meal Kit"+mealKit.getName()+mealKit.getMealKitId());
            System.err.println("****Order Meal Kit Orders"+mealKit.getOrders().size());

            mealKit.getOrders().add(order);

                        
//            String transactionDescription = "Purchase order [id] = "+order.getOrderId();
//            String transactionCode = order.getPurchasingDate().toString();
//            TransactionEntity transaction = new TransactionEntity(Double.valueOf(25.0), TransactionTypeEnum.CREDIT, PaymentTypeEnum.PAYPAL, new Date(), transactionDescription);
//            
//            em.persist(transaction);
//            
//            transaction.setCustomer(customer);
//            transaction.setOrder(order);
//            order.setTransaction(transaction);
//            customer.getTransactions().add(transaction);
            
            
            em.flush();
            em.refresh(order);
            
            if (mobile){
                payForOrder(order.getOrderId(), PaymentTypeEnum.PAYPAL);
            }
            
            return order;
       
    }
    
    @Override
    public void createOrderFromRest(Long customerId, Date delivery, Long mealKitId, Long addressId, String specialRequest, int qty){
        AddressEntity a = em.find(AddressEntity.class, addressId);
        MealKitEntity m = em.find(MealKitEntity.class, mealKitId);
        OrderEntity o = new OrderEntity();
        o.setDeliveryDate(delivery);
        o.setOrderStatus(OrderStatusEnum.PREPARING);
        o.setExtraRequest(specialRequest);
        o.setPurchasingDate(new Date());
        o.setShippingFee(a.getShippingFee());
        double t = m.getPrice() * qty;
        o.setTotalAmount(t);
        o.setOrderNumber("MM"+(new Random()).nextInt(10));
        o = createNewOrder(o, customerId, mealKitId, addressId, true);
        payForOrder(o.getOrderId(), PaymentTypeEnum.PAYPAL);
    }

    /**
     *
     * @param order
     * @param paymentType
     * @return
     */
    @Override
    public TransactionEntity payForOrder(Long orderId, PaymentTypeEnum paymentType) {
        TransactionEntity newTransaction = new TransactionEntity();
        OrderEntity order = em.find(OrderEntity.class, orderId);
        Random r = new Random();
        int i=r.nextInt(54);
        newTransaction.setAmount(order.getTotalAmount());
        newTransaction.setDescription("PAY FOR ORDER id=["+orderId+"]");
        newTransaction.setPaymentType(paymentType);
        newTransaction.setTransactionDateTime(new Date());
        newTransaction.setTransactionType(TransactionTypeEnum.CREDIT);
        SimpleDateFormat sf = new SimpleDateFormat("yyMMddhhmm");
        newTransaction.setTransactionCode(orderId+String.valueOf((char)(i + 64))+sf.format(new Date())+order.getCustomer().getPassword().substring(0, 5));
        order.setOrderStatus(OrderStatusEnum.PREPARING);
        createTimer(order.getOrderId(), getBeginningOfDay(order.getDeliveryDate()));
        
        em.persist(newTransaction);
        
        newTransaction.setOrder(order);
        newTransaction.setCustomer(order.getCustomer());
        order.setTransaction(newTransaction);
        
        em.flush();
        return newTransaction;
    }

    @Override
    public OrderEntity updateOrder(OrderEntity order){
        OrderEntity toUpdate = em.find(OrderEntity.class, order.getOrderId());
        toUpdate.setOrderStatus(order.getOrderStatus());
        
        return order;
        
    }

    //add getTransaction&mealKit for lazy fetching
    @Override
    public OrderEntity retrieveOrderById(Long orderId) throws OrderNotFoundException {
        OrderEntity order = em.find(OrderEntity.class, orderId);
        if (order != null) {
            order.getMealKit();
            order.getTransaction();
            order.getCustomer();
            return order;
        } else {
            throw new OrderNotFoundException("Order ID " + orderId + " does not exists!");
        }
    }

    //add get MealKit for lazy fetching
    @Override
    public List<OrderEntity> retrieveOrderByCustomerId(Long customerId) throws EmptyListException {
        System.err.println("***** order/customerId "+customerId);
        Query query = em.createQuery("SELECT o FROM OrderEntity o WHERE o.customer.customerId =:id");
        query.setParameter("id", customerId);
        List<OrderEntity> list = query.getResultList();
        if (list.isEmpty()) {
            throw new EmptyListException("No orders from the customer");
        }else{
            for (OrderEntity o:list){
                o.getMealKit();
            }
        }
        return list;
    }

    @Override
    public List<OrderEntity> retrieveAllOrders() {
        Query query = em.createQuery("SELECT o FROM Order o");
        List<OrderEntity> orders = query.getResultList();
        for (OrderEntity o : orders) {
            o.getCustomer();
            o.getMealKit();
        }
        return orders;
    }

//    @Override
//    public void refundOrder(OrderEntity order){
//        order.setOrderStatus(OrderStatusEnum.REFUNDED);
//        //creditTransaction
//        CustomerEntity customer = order.getCustomer();
//        em.merge(order);
//    }
    
    /**
     * create transaction entity and refund the order
     * order cannot be deliveried// refund policy???
     * @param orderId
     * @param description
     * @param type
     * @return
     */
    @Override
    public TransactionEntity refundOrder(Long orderId, String description, PaymentTypeEnum type){
        SimpleDateFormat sf = new SimpleDateFormat("ssmmhhddMMyy");
        TransactionEntity newTransaction = new TransactionEntity();
        OrderEntity order = em.find(OrderEntity.class, orderId);
        
        newTransaction.setAmount(order.getTotalAmount());
        newTransaction.setPaymentType(type);
        newTransaction.setTransactionCode("RE"+sf.format(new Date())+orderId);
        newTransaction.setTransactionDateTime(new Date());
        newTransaction.setTransactionType(TransactionTypeEnum.DEBIT);
        newTransaction.setDescription(description);
        
        order.setOrderStatus(OrderStatusEnum.REFUNDED);
        order.setTransaction(newTransaction);
        em.persist(newTransaction);
        
        
        newTransaction.setCustomer(order.getCustomer());
        newTransaction.setOrder(order);
        
        em.flush();
        return newTransaction;
    }
    
    private void createTimer(Long orderId, Date dateTime) {
        TimerConfig timerConfig = new TimerConfig(orderId, true);
        Timer timer = timerService.createSingleActionTimer(dateTime, timerConfig);
    }
    
    @Timeout
    private void timeOut(Timer timer) {
        System.err.println("******TimerOut: OrderId " + timer.getInfo() + "******");
        Long orderId = (Long) timer.getInfo();
        try {
            OrderEntity order = retrieveOrderById(orderId);
            if (order.getOrderStatus().equals(OrderStatusEnum.PREPARING)) {
                order.setOrderStatus(OrderStatusEnum.DELIVERING);
                createTimer(orderId, order.getDeliveryDate());
            } else {
                if (order.getOrderStatus().equals(OrderStatusEnum.DELIVERING)) {
                    order.setOrderStatus(OrderStatusEnum.DELIVERED);
                    createTimer(orderId, getEndOfDay(new Date()));
                }else{
                    if (order.getOrderStatus().equals(OrderStatusEnum.DELIVERED)){
                        order.setOrderStatus(OrderStatusEnum.RECEIVED);
                    }
                }
            }
        } catch (OrderNotFoundException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Date getEndOfDay(Date now){
        Date date = new Date(now.getYear(), now.getMonth(), now.getDate(), 23, 59, 59);
        return date;
    }
    
    private Date getBeginningOfDay(Date now){
        
        Date date = new Date(now.getYear(), now.getMonth(), now.getDate(), 6, 0, 0);
        return date;
    }
    
    
    
}
