/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;


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
    public OrderEntity createNewOrder(OrderEntity order) throws OrderExistException, GeneralException {
        try {
            em.persist(order);

            em.flush();
            return order;
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

    @Override
    public OrderEntity retrieveOrderById(Long orderId) throws OrderNotFoundException {
        OrderEntity order = em.find(OrderEntity.class, orderId);
        if (order != null) {
            //order.getMealKits().size();
            return order;
        } else {
            throw new OrderNotFoundException("Order ID " + orderId + " does not exists!");
        }
    }

    @Override
    public List<OrderEntity> retrieveOrderByCustomerId(Long customerId) throws EmptyListException {
        Query query = em.createQuery("SELECT o FROM OrderEntity WHERE o.customer.customerId =:id");
        query.setParameter("id", customerId);
        List<OrderEntity> list = query.getResultList();
        if (list.isEmpty()) {
            throw new EmptyListException("No orders from the customer");
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
