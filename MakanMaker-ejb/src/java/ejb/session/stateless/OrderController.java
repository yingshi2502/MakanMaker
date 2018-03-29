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
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
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

/**
 *
 * @author Summer
 */
@Stateless
public class OrderController implements OrderControllerLocal {

    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;

    public OrderController() {
    }

    @Override
    public OrderEntity createNewOrder(OrderEntity order, Long customerId, Long mealKitId, Long addressId) {
        
            em.persist(order);
            
            CustomerEntity customer = em.find(CustomerEntity.class, customerId);
            MealKitEntity mealKit = em.find(MealKitEntity.class, mealKitId);
            AddressEntity addressToDelivery = em.find(AddressEntity.class, addressId);
            
            order.setCustomer(customer);
            order.setMealKit(mealKit);
            
            order.setAddress(addressToDelivery);
            addressToDelivery.getOrders().add(order);
            
            customer.getOrderHistory().add(order);
            mealKit.getOrders().add(order);
            
            em.flush();
            em.refresh(order);
            
            return order;
       
    }

    /**
     *
     * @param order
     * @param paymentType
     * @return
     */
    @Override
    public TransactionEntity payForOrder(OrderEntity order, PaymentTypeEnum paymentType) {
        TransactionEntity newTransaction = new TransactionEntity();
        newTransaction.setAmount(order.getTotalAmount());
        
        newTransaction.setPaymentType(paymentType);
        newTransaction.setTransactionDateTime(new Date());
        newTransaction.setTransactionType(TransactionTypeEnum.CREDIT);
        order.setOrderStatus(OrderStatusEnum.PREPARING);
        
        em.persist(newTransaction);
        em.merge(order);
        newTransaction.setOrder(order);
        newTransaction.setCustomer(order.getCustomer());
        order.setTransaction(newTransaction);
        
        em.flush();
        return newTransaction;
    }

    @Override
    public OrderEntity updateOrder(OrderEntity order) throws OrderExistException, GeneralException {
        try {
            return em.merge(order);
        } catch (PersistenceException ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) {
                throw new OrderExistException("Order with same name already exist");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }

    //add getTransaction&mealKit for lazy fetching
    @Override
    public OrderEntity retrieveOrderById(Long orderId) throws OrderNotFoundException {
        OrderEntity order = em.find(OrderEntity.class, orderId);
        if (order != null) {
            order.getMealKit();
            order.getTransaction();
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
     * @param order
     * @return
     */
    @Override
    public TransactionEntity refundOrder(OrderEntity order) {
        TransactionEntity newTransaction = new TransactionEntity();
        newTransaction.setAmount(order.getTotalAmount());
        
        newTransaction.setTransactionDateTime(new Date());
        newTransaction.setTransactionType(TransactionTypeEnum.DEBIT);
        
        order.setOrderStatus(OrderStatusEnum.REFUNDED);
        em.persist(newTransaction);
        em.merge(order);
        newTransaction.setCustomer(order.getCustomer());
        newTransaction.setOrder(order);
        
        em.flush();
        return newTransaction;
    }
}
