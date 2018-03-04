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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PasswordChangeException;

/**
 *
 * @author yingshi
 */
@Stateless
public class CustomerController implements CustomerControllerLocal {

    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;

    
    
    public CustomerController() {
    }
    
    @Override
    public CustomerEntity createNewCustomer(CustomerEntity customer) throws CustomerExistException {
        return null;
    }
    
   
    @Override
    public CustomerEntity updateCustomer(CustomerEntity customer) throws CustomerNotFoundException {
        return null;
    }
    
    @Override
    public CustomerEntity retrieveCustomerById(Long customerId) throws CustomerNotFoundException{
        
        return null;
    }
    
    @Override
    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException{
        
        return null;
    }
    
    @Override
    public List<MealKitEntity> retrieveWishListByCustomerId(Long customerId){
        
        return null;
    }
    
    @Override
    public List<OrderEntity> retrieveOrderHistoryByCustomerId(Long customerId){
        
        return null;
    }
    
    @Override
    public CustomerEntity customerLogin(String usename, String password) throws InvalidLoginCredentialException{
        
        return null;
    }

   
    @Override
    public Boolean changePassword(Long customerId, String newPassword, String oldPassword) throws PasswordChangeException {
        
        return null;
    }

    
    
    
    
}
