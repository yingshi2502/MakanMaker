/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import entity.CustomerEntity;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public AddressEntity createNewAddress(AddressEntity address, Long customerId, boolean detach) throws GeneralException {
        try {
            CustomerEntity customer = customerControllerLocal.retrieveCustomerById(customerId,false);
            em.persist(address);

            customer.getAddresses().add(address);
            address.setCustomer(customer);
            em.flush();
            em.refresh(address);

            Long changeBilling = -1l;
            if (address.getIsDefaultBilling()) {
                changeBilling = address.getAddressId();
            }

            Long changeShipping = -1l;
            if (address.getIsDefaultShipping()) {
                changeShipping = address.getAddressId();
            }
            setDefaultAddress(changeShipping, changeBilling, customerId);

            if (detach){
                em.detach(address);
                      
                address.getOrders().clear();
                address.setCustomer(null);
            }
            
            return address;
        } catch (PersistenceException ex) {
            throw new GeneralException("An unexpected Error has occurred when creating new address");
        }
    }

    @Override   //default Shipping
    public AddressEntity getDefaultAddressById(Long customerId) throws EmptyListException {
        List<AddressEntity> addresses = retrieveAddressByCustomerId(customerId,false);
        for (AddressEntity a : addresses) {
            if (a.getIsDefaultShipping()) {
                return a;
            }
        }
        return null;
    }

    @Override
    public void updateAddress(AddressEntity addressToUpdate) {
        AddressEntity address = em.find(AddressEntity.class, addressToUpdate.getAddressId());
        address.setFloorUnit(addressToUpdate.getFloorUnit());
        address.setFullName(addressToUpdate.getFullName());
        address.setPhoneNumber(addressToUpdate.getPhoneNumber());
        address.setPostalCode(addressToUpdate.getPostalCode());
        address.setStreetAddress(addressToUpdate.getStreetAddress());

        Long changeBilling = -1l;
        if (addressToUpdate.getIsDefaultBilling()) {
            changeBilling = address.getAddressId();
        }
        Long changeShipping = -1l;
        if (addressToUpdate.getIsDefaultShipping()) {
            changeShipping = address.getAddressId();
        }
        setDefaultAddress(changeShipping, changeBilling, address.getCustomer().getCustomerId());

    }

    private void setDefaultAddress(Long defaultShippingId, Long defaultBillingId, Long customerId) {  //merge? managed state?
        try {
            List<AddressEntity> addresses = retrieveAddressByCustomerId(customerId, false);
            System.err.println("#****" + defaultBillingId + defaultShippingId);
            if (defaultShippingId.compareTo(0l) > 0) {
                for (AddressEntity a : addresses) {
                    if (a.getIsDefaultShipping() && !Objects.equals(a.getAddressId(), defaultShippingId)) {
                        a.setIsDefaultShipping(false);
                    }
                    if (a.getAddressId().equals(defaultShippingId)) {
                        a.setIsDefaultShipping(true);
                    }
                }
            }
            if (defaultBillingId.compareTo(0l) > 0) {
                for (AddressEntity a : addresses) {
                    if (a.getIsDefaultBilling() && !Objects.equals(a.getAddressId(), defaultBillingId)) {
                        a.setIsDefaultBilling(false);
                    }
                    if (a.getAddressId().equals(defaultBillingId)) {
                        a.setIsDefaultBilling(true);
                    }
                }
            }
        } catch (EmptyListException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void deleteAddress(Long addressId) throws GeneralException {
        try {
            AddressEntity address = em.find(AddressEntity.class, addressId);
            address.setIsDeleted(Boolean.TRUE);
            System.err.println("****" + address.getAddressId() + "asdfa" + address.getCustomer().getCustomerId());
            List<AddressEntity> addresses = retrieveAddressByCustomerId(address.getCustomer().getCustomerId(),false);

            AddressEntity chosenDefault = addresses.get(0);

            Long changeBilling = -1l;
            if (address.getIsDefaultBilling()) {
                changeBilling = chosenDefault.getAddressId();
            }
            Long changeShipping = -1l;
            if (address.getIsDefaultShipping()) {
                changeShipping = chosenDefault.getAddressId();
            }
            setDefaultAddress(changeShipping, changeBilling, address.getCustomer().getCustomerId());
            em.merge(chosenDefault);
        } catch (PersistenceException ex) {
            throw new GeneralException("An unexpected Error has occurred when deleting new address!");
        } catch (EmptyListException ex) {
        }

    }

    @Override
    public List<AddressEntity> retrieveAddressByCustomerId(Long customerId, boolean detach) throws EmptyListException {
        Query query = em.createQuery("SELECT a FROM AddressEntity a WHERE a.customer.customerId=:customerId AND a.isDeleted=FALSE");
        query.setParameter("customerId", customerId);

        List<AddressEntity> addresses = query.getResultList();
        if (detach){
            for (AddressEntity a: addresses){
                em.detach(a);
                a.getOrders().clear();
                a.setCustomer(null);
            }
        }
        
        if (addresses == null) {
            throw new EmptyListException("No Address has been created!");
        }
        if (addresses.isEmpty()) {
            throw new EmptyListException("No Address has been created!");
        } else {
            return addresses;
        }

    }

    @Override
    public AddressEntity retrieveAddressById(Long id, boolean detach) {
        AddressEntity add = em.find(AddressEntity.class, id);
        if (add == null) {
            return null;
        }
        if (detach) {
            em.detach(add);
            add.setCustomer(null);
            add.getOrders().clear();
        }
        return add;
    }

}
