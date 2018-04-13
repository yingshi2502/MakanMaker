/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AddressControllerLocal;
import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.OrderControllerLocal;
import ejb.session.stateless.TagControllerLocal;
import entity.AddressEntity;
import entity.CustomerEntity;
import entity.ManagerEntity;
import entity.MealKitEntity;
import entity.OrderEntity;
import entity.TagEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import util.helperClass.SecurityHelper;

/**
 *
 * @author yingshi
 */
@Singleton
@LocalBean
@Startup
public class DataInitialization {

    @EJB
    private TagControllerLocal tagControllerLocal;

    @EJB
    private AddressControllerLocal addressControllerLocal;

    @EJB
    private OrderControllerLocal orderControllerLocal;

    @EJB
    private CustomerControllerLocal customerController;

    
    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;
    
    private CustomerEntity customer;
    
    @PostConstruct
    public void postConstruct(){
        if (em.find(ManagerEntity.class, 1l) == null){
            initialize();
        }
        
    }

    private void initialize(){
        ManagerEntity manager = new ManagerEntity("manager", "password");
        manager.setPassword(SecurityHelper.generatePassword(manager.getPassword()));
        em.persist(manager);
        
        createCustomer();
        createAddress();
        createMKTag();
        linkMKToTag();
        createOrder();

    }
    private void linkMKToTag(){
        tagControllerLocal.linkTagAndMealKit(1l, 1l);
        tagControllerLocal.linkTagAndMealKit(7l, 1l);
        tagControllerLocal.linkTagAndMealKit(7l, 4l);
        tagControllerLocal.linkTagAndMealKit(6l, 5l);
        tagControllerLocal.linkTagAndMealKit(6l, 6l);
        em.flush();
    }
    
    
    private void createOrder(){
        String code = "";
//        OrderEntity order = new OrderEntity(Double.NaN, deliveryDate, purchasingDate, OrderStatusEnum.PREPARING, code, code, Double.MIN_VALUE)
        Date now = new Date();        
        Date date = new Date(now.getYear(), now.getMonth(), now.getDate()+1, 23, 59, 59);

        OrderEntity order = new OrderEntity(Double.valueOf(25), 2, new Date(), date, OrderStatusEnum.PREPARING, code, "Add more flavour", Double.valueOf(5));
        
       orderControllerLocal.createNewOrder(order, 1l, 1l,1l,false);  
    }
    
    private void createAddress(){
        try {
            AddressEntity address = new AddressEntity("118430", "37 PGP", "#05-28", Boolean.TRUE, Boolean.TRUE, "99999999", "Huang Yingshi");
            addressControllerLocal.createNewAddress(address,1l,false);
            em.flush();
        } catch (GeneralException ex) {
           System.err.println("****Error in creating address");
        }
    }
    
    private void createCustomer(){
        try {
            customer = new CustomerEntity("yingshi", "Huang Yingshi","88888888","huangyingshi@gmail.com", "password", new Date(1998-2000, 4, 23),1);
            customerController.createNewCustomer(customer,false);
            customer.getWishList().add(1l);
            em.flush();
            em.refresh(customer);
        } catch (CustomerExistException | GeneralException ex) {
            System.err.println("***Error in creating customer");
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

    private void createMKTag(){
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
        
        tag = new TagEntity("Vegetarian",TagCategoryEnum.DIET);
        em.persist(tag);
        
        tag = new TagEntity("Seafood",TagCategoryEnum.DIET);
        em.persist(tag);
        
        tag = new TagEntity("Beef",TagCategoryEnum.DIET);
        em.persist(tag);
        
        
        List<String> ingredients = new ArrayList<String>();
        ingredients.add("For the coconut rice:");
        ingredients.add("2 cups basmati rice");
        ingredients.add("6 pandan leaves, knotted");
        ingredients.add("8cm knob of ginger, bruised");
        ingredients.add("270ml coconut milk");
        ingredients.add("For the sambal:");
        ingredients.add("30 dried chillies, seeds removed and soaked in hot water");
        ingredients.add("5 shallots, 3 quartered and 2 thinly sliced");
        ingredients.add("1 tbsp ginger or garlic paste");
        MealKitEntity mealKit = new MealKitEntity("Nasi Lemak", 2.00, ingredients, true, 30, "The best Nasi Lemak in town", "40 to 30 min", "../images/mealKit1.jpg", "Easy"); 
        em.persist(mealKit);

        ingredients = new ArrayList<String>();
        ingredients.add("For the coconut rice:");
        ingredients.add("2 cups basmati rice");
        ingredients.add("6 pandan leaves, knotted");
        ingredients.add("8cm knob of ginger, bruised");
        ingredients.add("270ml coconut milk");
        ingredients.add("For the sambal:");
        ingredients.add("30 dried chillies, seeds removed and soaked in hot water");
        ingredients.add("5 shallots, 3 quartered and 2 thinly sliced");
        ingredients.add("1 tbsp ginger or garlic paste");       
        mealKit = new MealKitEntity("Bobo Chaha", 4.00, ingredients, true, 100, "Cooling for a hot day", "20 to 30 min", "../images/mealKit2.jpg","Easy"); 
        em.persist(mealKit);
        
        ingredients = new ArrayList<String>();
        ingredients.add("For the coconut rice:");
        ingredients.add("2 cups basmati rice");
        ingredients.add("6 pandan leaves, knotted");
        ingredients.add("8cm knob of ginger, bruised");
        ingredients.add("270ml coconut milk");
        ingredients.add("For the sambal:");
        ingredients.add("30 dried chillies, seeds removed and soaked in hot water");
        ingredients.add("5 shallots, 3 quartered and 2 thinly sliced");
        ingredients.add("1 tbsp ginger or garlic paste");       
        mealKit = new MealKitEntity("Chicken Chop", 4.00, ingredients, true, 100, "For meat lovers all day everyday", "20 to 30 min", "../images/mealKit3.jpg","Hard"); 
        em.persist(mealKit);
        
        
        ingredients = new ArrayList<String>();
        ingredients.add("For the coconut rice:");
        ingredients.add("2 cups basmati rice");
        ingredients.add("6 pandan leaves, knotted");
        ingredients.add("8cm knob of ginger, bruised");
        ingredients.add("270ml coconut milk");
        ingredients.add("For the sambal:");
        ingredients.add("30 dried chillies, seeds removed and soaked in hot water");
        ingredients.add("5 shallots, 3 quartered and 2 thinly sliced");
        ingredients.add("1 tbsp ginger or garlic paste");   
        mealKit = new MealKitEntity("Fish & Chips", 4.00, ingredients, true, 100, "For fish lovers all day everyday", "20 to 30 min", "../images/mealKit4.jpg","Hard"); 
        em.persist(mealKit);
        
        
        ingredients = new ArrayList<String>();
        ingredients.add("For the coconut rice:");
        ingredients.add("2 cups basmati rice");
        ingredients.add("6 pandan leaves, knotted");
        ingredients.add("8cm knob of ginger, bruised");
        ingredients.add("270ml coconut milk");
        ingredients.add("For the sambal:");
        ingredients.add("30 dried chillies, seeds removed and soaked in hot water");
        ingredients.add("5 shallots, 3 quartered and 2 thinly sliced");
        ingredients.add("1 tbsp ginger or garlic paste");    
        mealKit = new MealKitEntity("Mee Goreng", 4.00, ingredients, true, 100, "Fried Noodles Stir Fried with Beef", "20 to 30 min", "../images/mealKit5.jpg","Easy"); 
        em.persist(mealKit);        
        
        ingredients = new ArrayList<String>();
        ingredients.add("For the coconut rice:");
        ingredients.add("2 cups basmati rice");
        ingredients.add("6 pandan leaves, knotted");
        ingredients.add("8cm knob of ginger, bruised");
        ingredients.add("270ml coconut milk");
        ingredients.add("For the sambal:");
        ingredients.add("30 dried chillies, seeds removed and soaked in hot water");
        ingredients.add("5 shallots, 3 quartered and 2 thinly sliced");
        ingredients.add("1 tbsp ginger or garlic paste");    
        mealKit = new MealKitEntity("Rojak", 4.00, ingredients, true, 100, "For the all in one fans", "10 to 20 min", "../images/mealKit6.jpg","Very Easy"); 
        em.persist(mealKit);    
        System.err.println("***Finished Create MKs");

        em.flush();
    }
    
}
