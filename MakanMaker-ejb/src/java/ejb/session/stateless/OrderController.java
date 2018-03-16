/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.OrderEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.OrderStatusEnum;
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

    public void persist(Object object) {
        em.persist(object);
    }

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
    
    @Override
    public OrderEntity updateOrder(OrderEntity order) throws OrderExistException,GeneralException {
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
        if (order != null){
            //order.getMealKits().size();
            return order;
        }else{
            throw new OrderNotFoundException("Order ID "+ orderId+" does not exists!");
        }
    }
    
    @Override
    public List<OrderEntity> retrieveOrderByCustomerId(Long customerId) throws EmptyListException{
        Query query = em.createQuery("SELECT o FROM OrderEntity WHERE o.customer.customerId =:id");
        query.setParameter("id", customerId);
        List<OrderEntity> list = query.getResultList();
        if (list.isEmpty()){
            throw new EmptyListException("No orders from the customer");
        }
        return list;
    }
    
    @Override
    public List<OrderEntity> retrieveAllOrders() {
        Query query = em.createQuery("SELECT o FROM Order o");
        List<OrderEntity> orders = query.getResultList();
        for (OrderEntity o : orders) {
            o.getMealKits().size();
        }
        return orders;
    }
    
    @Override
    public void refundOrder(OrderEntity order){
        order.setOrderStatus(OrderStatusEnum.REFUNDED);
        //creditTransaction
        CustomerEntity customer = order.getCustomer();
        em.merge(order);
    }
}
