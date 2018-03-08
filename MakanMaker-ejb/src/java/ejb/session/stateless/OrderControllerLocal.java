/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import java.util.List;
import javax.ejb.Local;
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

    public OrderEntity createNewOrder(OrderEntity order) throws OrderExistException, GeneralException;

    public OrderEntity updateOrder(OrderEntity order) throws OrderExistException, GeneralException;

    public OrderEntity retrieveOrderById(Long orderId) throws OrderNotFoundException;

    public List<OrderEntity> retrieveOrderByCustomerId(Long customerId) throws EmptyListException;

    public List<OrderEntity> retrieveAllOrders();

    public void refundOrder(OrderEntity order);
    
}
