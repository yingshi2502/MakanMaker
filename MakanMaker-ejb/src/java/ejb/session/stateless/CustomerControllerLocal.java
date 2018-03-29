/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.MealKitEntity;
import entity.OrderEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PasswordChangeException;

/**
 *
 * @author yingshi
 */
@Local
public interface CustomerControllerLocal {

    CustomerEntity customerLogin(String usename, String password) throws InvalidLoginCredentialException;

    Boolean changePassword(Long customerId, String newPassword, String oldPassword) throws PasswordChangeException;

    CustomerEntity createNewCustomer(CustomerEntity customer) throws CustomerExistException,GeneralException;

    public CustomerEntity updateCustomer(CustomerEntity customer) throws CustomerNotFoundException;

    public CustomerEntity retrieveCustomerById(Long customerId);

    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public List<MealKitEntity> retrieveWishListByCustomerId(Long customerId);

    public List<OrderEntity> retrieveOrderHistoryByCustomerId(Long customerId);
    
}
