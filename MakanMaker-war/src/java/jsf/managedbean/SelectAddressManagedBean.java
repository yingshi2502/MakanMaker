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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.exception.EmptyListException;

/**
 *
 * @author yingshi
 */
@Named(value = "selectAddressManagedBean")
@ViewScoped
public class SelectAddressManagedBean implements Serializable{

    @EJB(name = "AddressControllerLocal")
    private AddressControllerLocal addressControllerLocal;
    
    private List<AddressEntity> allAddresses;
    private AddressEntity defaultAddress;
    private AddressEntity selectedAddress;
    private CustomerEntity currentCustomer;
    /**
     * Creates a new instance of SelectAddressManagedBean
     */
    public SelectAddressManagedBean() {
        allAddresses = new ArrayList<>();
        defaultAddress = new AddressEntity();
        selectedAddress = new AddressEntity();
    }
    
    @PostConstruct
    public void postConstruct(){
        try {
            this.setCurrentCustomer((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity"));
            System.err.println("*****just after retirve customer" + currentCustomer.getEmail());
            setAllAddresses(addressControllerLocal.retrieveAddressByCustomerId(getCurrentCustomer().getCustomerId(), false));
            System.err.println("*****just after retirve address" + allAddresses.size());
            //defaultAddress = addressControllerLocal.getDefaultAddressById(currentCustomer.getCustomerId());
            selectedAddress = addressControllerLocal.getDefaultAddressById(currentCustomer.getCustomerId());
        } catch (EmptyListException ex) {
            Logger.getLogger(SelectAddressManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void putSelectedAddress(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedAddressEntity", selectedAddress);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MakanMaker-war/checkout/select_schedule.xhtml");
    }
    

    /**
     * @return the allAddresses
     */
    public List<AddressEntity> getAllAddresses() {
        return allAddresses;
    }

    /**
     * @param allAddresses the allAddresses to set
     */
    public void setAllAddresses(List<AddressEntity> allAddresses) {
        this.allAddresses = allAddresses;
    }

    /**
     * @return the defaultAddress
     */
    public AddressEntity getDefaultAddress() {
        return defaultAddress;
    }

    /**
     * @param defaultAddress the defaultAddress to set
     */
    public void setDefaultAddress(AddressEntity defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    /**
     * @return the selectedAddress
     */
    public AddressEntity getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(AddressEntity selectedAddress) {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Address added successfully (Product ID: " + selectedAddress.getPostalCode() + ")", null));
//        //shipping fee
        
      
        this.selectedAddress = selectedAddress;
    }

    /**
     * @return the currentCustomer
     */
    public CustomerEntity getCurrentCustomer() {
        return currentCustomer;
    }

    /**
     * @param currentCustomer the currentCustomer to set
     */
    public void setCurrentCustomer(CustomerEntity currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

}
