/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import entity.TransactionEntity;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.PaymentTypeEnum;
import util.exception.EmptyListException;
import util.exception.GeneralException;
import util.exception.OrderExistException;
import util.exception.OrderNotFoundException;

/**
 *
 * @author Summer
 */
@Local
public interface OrderControllerLocal {

    public OrderEntity createNewOrder(OrderEntity order, Long customerId, Long mealKitId, Long addressId) ;

    public OrderEntity updateOrder(OrderEntity order);

    public OrderEntity retrieveOrderById(Long orderId) throws OrderNotFoundException;

    public List<OrderEntity> retrieveOrderByCustomerId(Long customerId) throws EmptyListException;

    public List<OrderEntity> retrieveAllOrders();

//    public void refundOrder(OrderEntity order);


    public TransactionEntity payForOrder(OrderEntity order, PaymentTypeEnum paymentType);

    public TransactionEntity refundOrder(Long orderId, String description,PaymentTypeEnum paymentType);
    
}
