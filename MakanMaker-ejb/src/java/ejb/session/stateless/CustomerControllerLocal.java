/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.MealKitEntity;
import entity.OrderEntity;
import entity.ShoppingCartEntity;
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

    CustomerEntity customerLogin(String usename, String password, boolean detach) throws InvalidLoginCredentialException;

    Boolean changePassword(Long customerId, String newPassword, String oldPassword) throws PasswordChangeException;

    CustomerEntity createNewCustomer(CustomerEntity customer, boolean detach) throws CustomerExistException,GeneralException;

    public CustomerEntity updateCustomer(CustomerEntity customer, boolean detach) throws CustomerNotFoundException;

    public CustomerEntity retrieveCustomerById(Long customerId, boolean detach);

    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public List<MealKitEntity> retrieveWishListByCustomerId(Long customerId);

    public List<OrderEntity> retrieveOrderHistoryByCustomerId(Long customerId);

    public ShoppingCartEntity retrieveShoppingCartByCustomerId(Long customerId, boolean detach);

    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException;
    
}
