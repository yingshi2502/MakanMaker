/*
 * Customer Controller
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.MealKitEntity;
import entity.OrderEntity;
import entity.ShoppingCartEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PasswordChangeException;
import util.helperClass.SecurityHelper;

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

    /**
     *
     * @param customer
     * @return
     * @throws CustomerExistException
     * @throws GeneralException
     */
    @Override
    public CustomerEntity createNewCustomer(CustomerEntity customer) throws CustomerExistException, GeneralException {
        try {
            customer.setPassword(SecurityHelper.generatePassword(customer.getPassword()));
            ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
            em.persist(customer);
            em.persist(shoppingCart);

            shoppingCart.setCustomer(customer);
            customer.setShoppingCart(shoppingCart);

            em.flush();
            return customer;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) {
                throw new CustomerExistException("Customer with same username or email already exist");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }

    /**
     *
     * @param customer
     * @return
     * @throws util.exception.CustomerNotFoundException
     * @throws CustomerExistException
     * @throws GeneralException
     */
    @Override
    public CustomerEntity updateCustomer(CustomerEntity customer) throws CustomerNotFoundException {
        System.err.println("inside UpdatedCustomer");
        System.err.println(customer.getMobile());

        if (customer.getCustomerId() != null) {
            CustomerEntity updatedCustomer = retrieveCustomerById(customer.getCustomerId());
                updatedCustomer.setDateOfBirth(customer.getDateOfBirth());
                updatedCustomer.setMobile(customer.getMobile());
                updatedCustomer.setEmail(customer.getEmail());
                System.err.println("inside set email");
                updatedCustomer.setFullName(customer.getFullName());
                updatedCustomer.setGender(customer.getGender());
            
            return updatedCustomer;
        } else {
            throw new CustomerNotFoundException("Customer ID invalid");
        }
    }

    /**
     *
     * @param customerId
     * @return
     * @throws CustomerNotFoundException
     */
    @Override
    public CustomerEntity retrieveCustomerById(Long customerId) {
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);

        return customer;
    }

    /**
     *
     * @param username
     * @return
     * @throws CustomerNotFoundException
     */
    @Override
    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.userName = :username");
        query.setParameter("username", username);
        try {
            return (CustomerEntity) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new CustomerNotFoundException(ex.getMessage());
        }
    }

    /**
     *
     * @param customerId
     * @return null if customerId was wrong but in real case won't;
     *
     */
    @Override
    public List<MealKitEntity> retrieveWishListByCustomerId(Long customerId) {
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);

        if (customer != null) {
            List<MealKitEntity> mealKits = null;
            for (Long mkId : customer.getWishList()) {
                mealKits.add(em.find(MealKitEntity.class, mkId));
            }
            return mealKits;
        } else {
            return null;
        }
    }

    /**
     *
     * @param customerId
     * @return
     */
    @Override
    public List<OrderEntity> retrieveOrderHistoryByCustomerId(Long customerId) {
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);

        if (customer != null) {
            customer.getOrderHistory().size();
            return customer.getOrderHistory();
        } else {
            return null;
        }
    }

    @Override
    public CustomerEntity customerLogin(String username, String password) throws InvalidLoginCredentialException {

        try {
            CustomerEntity customer = retrieveCustomerByUsername(username);
            if (SecurityHelper.verifyPassword(password, customer.getPassword())) {
                return customer;
            } else {
                System.err.println("****Customer Login wrong pass");
                throw new InvalidLoginCredentialException("Wrong password!");
            }
        } catch (CustomerNotFoundException ex) {
            System.err.println("****Customer Login no Username");
            throw new InvalidLoginCredentialException("Username does not exist!");
        }
    }

    /**
     *
     * @param customerId
     * @param oldPassword
     * @param newPassword
     * @return
     * @throws PasswordChangeException
     */
    @Override
    public Boolean changePassword(Long customerId, String oldPassword, String newPassword) throws PasswordChangeException {

        CustomerEntity customer = retrieveCustomerById(customerId);
        if (SecurityHelper.verifyPassword(oldPassword, customer.getPassword())) {
            customer.setPassword(SecurityHelper.generatePassword(newPassword));
            System.err.println("****Customer pass changed");

            return true;
        } else {
            throw new PasswordChangeException("Password changed failed: Current password is wrong");
        }

    }
}
