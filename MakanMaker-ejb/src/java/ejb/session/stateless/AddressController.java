/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import entity.CustomerEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.EmptyListException;
import util.exception.GeneralException;

/**
 *
 * @author yingshi
 */
@Stateful
public class AddressController implements AddressControllerLocal {

    @PersistenceContext(unitName = "MakanMaker-ejbPU")
    private EntityManager em;

    @EJB(name = "CustomerControllerLocal")
    private CustomerControllerLocal customerControllerLocal;

    @Override
    public AddressEntity createNewAddress(AddressEntity address, Long customerId) throws GeneralException {
        try {
            CustomerEntity customer = customerControllerLocal.retrieveCustomerById(customerId);
            em.persist(address);

            customer.getAddresses().add(address);
            address.setCustomer(customer);

            em.flush();
            em.refresh(address);

            return address;
        } catch (PersistenceException | CustomerNotFoundException ex) {
            throw new GeneralException("An unexpected Error has occurred when creating new address");
        }
    }


    @Override
    public void updateAddress(AddressEntity address) {
        em.merge(address);
    }

    @Override
    public void deleteAddress(Long addressId) throws GeneralException {
        try {
            AddressEntity address = em.find(AddressEntity.class, addressId);
            address.setIsDeleted(Boolean.TRUE);
        } catch (PersistenceException ex) {
            throw new GeneralException("An unexpected Error has occurred when deleting new address!");
        }

    }

    @Override
    public List<AddressEntity> retrieveAddressByCustomerId(Long customerId) throws EmptyListException{
        Query query = em.createQuery("SELECT a FROM AddressEntity a WHERE a.customer.customerId=:customerId AND a.isDeleted=FALSE");
        query.setParameter("customerId", customerId);
       
        List<AddressEntity> addresses = query.getResultList();
        if (addresses == null){
            throw new EmptyListException("No Address has been created!");
        }else{
            return addresses;
        }
        
    }
    

    
}
