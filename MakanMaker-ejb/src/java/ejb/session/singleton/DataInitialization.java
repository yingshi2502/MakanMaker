/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.CustomerEntity;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author yingshi
 */
@Singleton
@LocalBean
public class DataInitialization {

    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;

    
    @PostConstruct
    public void postConstruct(){
        CustomerEntity customer = new CustomerEntity("yingshi", "yingshi", "12345");
        em.persist(customer);
    }

}
