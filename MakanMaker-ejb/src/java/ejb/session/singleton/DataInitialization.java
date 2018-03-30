/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AddressControllerLocal;
import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.MealKitControllerLocal;
import ejb.session.stateless.OrderControllerLocal;
import ejb.session.stateless.ShoppingCartControllerLocal;
import entity.AddressEntity;
import entity.CustomerEntity;
import entity.ManagerEntity;
import entity.MealKitEntity;
import entity.OrderEntity;
import entity.ShoppingCartEntity;
import entity.TagEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.OrderStatusEnum;
import util.enumeration.TagCategoryEnum;
import util.exception.CustomerExistException;
import util.exception.GeneralException;
import util.exception.MealKitExistException;
import util.exception.OrderExistException;
import util.helperClass.SecurityHelper;

/**
 *
 * @author yingshi
 */
@Singleton
@LocalBean
@Startup
public class DataInitialization {

    @EJB(name = "MealKitControllerLocal")
    private MealKitControllerLocal mealKitController;

    @EJB(name = "AddressControllerLocal")
    private AddressControllerLocal addressControllerLocal;

    @EJB(name = "OrderControllerLocal")
    private OrderControllerLocal orderControllerLocal;

    @EJB
    private CustomerControllerLocal customerController;
    
    
    
    

    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;
    
    private CustomerEntity customer;
    
    @PostConstruct
    public void postConstruct(){
        if (em.find(ManagerEntity.class, 1l) == null){
            try {
                initialize();
            } catch (MealKitExistException ex) {
                Logger.getLogger(DataInitialization.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        ManagerEntity manager = new ManagerEntity("manager", "password");
//        manager.setPassword(SecurityHelper.generatePassword(manager.getPassword()));
//        em.persist(manager);
    }

    private void initialize() throws MealKitExistException{
        ManagerEntity manager = new ManagerEntity("manager", "password");
        manager.setPassword(SecurityHelper.generatePassword(manager.getPassword()));
        em.persist(manager);
        createCustomer();
        createTags();
        createOrder();
    }
    
    private void createOrder(){
        OrderEntity order = new OrderEntity(Double.valueOf(20), 2, new Date(), new Date(), OrderStatusEnum.PREPARING, "Add more flavour");
        orderControllerLocal.createNewOrder(order, 1l, 1l,1l);
    }
    
    
    private void createCustomer() throws MealKitExistException{
        try {
            customer = new CustomerEntity("yingshi", "Huang Yingshi","88888888","huangyingshi@gmail.com", "password", new Date(1998, 4, 23),1);
            customerController.createNewCustomer(customer);
            
            AddressEntity address = new AddressEntity("118430", "37 PGP", "#05-28", Boolean.TRUE, Boolean.TRUE, "99999999", "Huang Yingshi");
            addressControllerLocal.createNewAddress(address,1l);
            
            MealKitEntity mealKit = new MealKitEntity("Rice set", 10.00, Boolean.TRUE);
            mealKitController.createNewMealKit(mealKit);
            
            customer.getShoppingCart().addMealKit(mealKit);
            customer.getShoppingCart().addQuantity(4);
            
          
        
        } catch (CustomerExistException | GeneralException ex) {
            System.err.println("Error in creating customer");
        }
    }
    
    private void createTags(){
        TagEntity tag = new TagEntity("Western",TagCategoryEnum.GEOGRPHIC);
        em.persist(tag);
        
        tag = new TagEntity("Chinese",TagCategoryEnum.GEOGRPHIC);
        em.persist(tag);
        
        tag = new TagEntity("Japanese",TagCategoryEnum.GEOGRPHIC);
        em.persist(tag);
        
        tag = new TagEntity("Indian",TagCategoryEnum.GEOGRPHIC);
        em.persist(tag);
        
        tag = new TagEntity("Malay",TagCategoryEnum.GEOGRPHIC);
        em.persist(tag);
        
        MealKitEntity mealKit = new MealKitEntity("Nasi Lemak", Double.valueOf(10.0), true);
        
        List<String> ingre = new ArrayList<>();
        ingre.add("Creamy");
        ingre.add("Coconut-indused rice");
        ingre.add("Spicy Sambal");
        mealKit.setIngredients(ingre); 
        em.persist(mealKit);
        customer.getWishList().add(mealKit.getMealKitId());
        
        tag = new TagEntity("Vegetarian",TagCategoryEnum.DIET);
        em.persist(tag);
        
        tag = new TagEntity("Seafood",TagCategoryEnum.DIET);
        em.persist(tag);
        
        tag = new TagEntity("Beef",TagCategoryEnum.DIET);
        em.persist(tag); 
        
        em.flush();
    }
    
    public DataInitialization() {
        
    }

    
}
