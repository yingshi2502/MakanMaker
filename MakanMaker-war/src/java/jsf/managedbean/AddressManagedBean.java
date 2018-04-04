/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AddressControllerLocal;
import entity.AddressEntity;
import entity.CustomerEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.exception.EmptyListException;
import util.exception.GeneralException;

/**
 *
 * @author yingshi
 */
@Named
@ViewScoped
public class AddressManagedBean implements Serializable{

    @EJB(name = "AddressControllerLocal")
    private AddressControllerLocal addressControllerLocal;

    
    private List<AddressEntity> addresses;
    private boolean noAddress;
    private AddressEntity newAddress;
    private Long customerId;
    private AddressEntity addressToUpdate;

    
    public AddressManagedBean() {
        addresses = new ArrayList<>();
        newAddress = new AddressEntity();
    }
    
    @PostConstruct
    public void postConstruct(){
        CustomerEntity currCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
        
        if (currCustomer==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Login!", null));
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AddressManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        setCustomerId(currCustomer.getCustomerId());
        try {
            setAddresses(addressControllerLocal.retrieveAddressByCustomerId(getCustomerId()));
            setNoAddress(false);
        } catch (EmptyListException ex) {
            setNoAddress(true);
        }
    }
    
    
    public void viewAddressDetails(ActionEvent event) throws IOException
    {
        Long productIdToView = (Long)event.getComponent().getAttributes().get("customerId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("productIdToView", productIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
    }
    
    
    
    public void createNewAddress(ActionEvent event)
    {
        try
        {
            AddressEntity ae = addressControllerLocal.createNewAddress(newAddress, customerId);
            setAddresses(addressControllerLocal.retrieveAddressByCustomerId(getCustomerId()));
            setNewAddress(new AddressEntity());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New address created successfully", null));
        } catch (GeneralException ex) {
        } catch (EmptyListException ex) {
        }
    }
    
    
    
    public void updateAddress(ActionEvent event)
    {
            addressControllerLocal.updateAddress(getAddressToUpdate());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product updated successfully", null));
        
    }
    
    
    
    public void deleteAddress(ActionEvent event)
    {
        try {
            AddressEntity addressToDelete = (AddressEntity)event.getComponent().getAttributes().get("addressToDelete");
            
            
            addressControllerLocal.deleteAddress(addressToDelete.getAddressId());
            addresses = addressControllerLocal.retrieveAddressByCustomerId(customerId);
//
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product deleted successfully", null));
//        
        } catch (GeneralException ex) {
            Logger.getLogger(AddressManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ex) {
            noAddress = true;
        }
    }

    /**
     * @return the addresses
     */
    public List<AddressEntity> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses the addresses to set
     */
    public void setAddresses(List<AddressEntity> addresses) {
        this.addresses = addresses;
    }

    /**
     * @return the noAddress
     */
    public boolean isNoAddress() {
        return noAddress;
    }

    /**
     * @param noAddress the noAddress to set
     */
    public void setNoAddress(boolean noAddress) {
        this.noAddress = noAddress;
    }

    /**
     * @return the newAddress
     */
    public AddressEntity getNewAddress() {
        return newAddress;
    }

    /**
     * @param newAddress the newAddress to set
     */
    public void setNewAddress(AddressEntity newAddress) {
        this.newAddress = newAddress;
    }

    /**
     * @return the customerId
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the addressToUpdate
     */
    public AddressEntity getAddressToUpdate() {
        return addressToUpdate;
    }

    /**
     * @param addressToUpdate the addressToUpdate to set
     */
    public void setAddressToUpdate(AddressEntity addressToUpdate) {
        this.addressToUpdate = addressToUpdate;
    }

}
