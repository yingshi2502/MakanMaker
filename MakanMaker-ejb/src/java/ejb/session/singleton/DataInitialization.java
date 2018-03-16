/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.CustomerEntity;
import entity.ManagerEntity;
import entity.TagEntity;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.TagCategoryEnum;
import util.helperClass.SecurityHelper;

/**
 *
 * @author yingshi
 */
@Singleton
@LocalBean
@Startup
public class DataInitialization {

    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;
    
    @PostConstruct
    public void postConstruct(){
        initialize();
    }

    private void initialize(){
        ManagerEntity manager = new ManagerEntity("manager", "password");
        manager.setPassword(SecurityHelper.generatePassword(manager.getPassword()));
        em.persist(manager);
        
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
       
    }
    
    
    public DataInitialization() {
        
    }

    
}
