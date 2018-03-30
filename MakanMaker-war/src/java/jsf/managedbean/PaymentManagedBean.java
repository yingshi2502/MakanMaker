/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AddressControllerLocal;
import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.OrderControllerLocal;
import entity.AddressEntity;
import entity.CustomerEntity;
import entity.OrderEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Summer
 */
@Named(value = "paymentManagedBean")
@RequestScoped
public class PaymentManagedBean {

    /**
     * Creates a new instance of PaymentManagedBean
     */
    
    @EJB(name = "CustomerControllerLocal")
    private CustomerControllerLocal customerControllerLocal;

    @EJB(name = "OrderControllerLocal")
    private OrderControllerLocal orderControllerLocal;

    @EJB(name = "AddressControllerLocal")
    private AddressControllerLocal addressControllerLocal;

    private CustomerEntity currentCustomer;
    private List<AddressEntity> allAddresses;
    private AddressEntity defaultAddress;
    private AddressEntity selectedAddress;
    private List<OrderEntity> currOrders;
    
    public PaymentManagedBean() {
        allAddresses = new ArrayList<>();
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
    }
    
    public CustomerEntity getCurrentCustomer() {
        return currentCustomer;
    }

    /**
     * @param currentCustomer the currentCustomer to set
     */
    public void setCurrentCustomer(CustomerEntity currentCustomer) {
        this.currentCustomer = currentCustomer;
    }
    
    public AddressEntity getDefaultAddress() {
        return defaultAddress;
    }

    /**
     * @param defaultAddress the defaultAddress to set
     */
    public void setDefaultAddress(AddressEntity defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public List<AddressEntity> getAllAddresses() {
        return allAddresses;
    }

    public void setAllAddresses(List<AddressEntity> allAddresses) {
        this.allAddresses = allAddresses;
    }

    public AddressEntity getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(AddressEntity selectedAddress) {
        this.selectedAddress = selectedAddress;
    }
    
}
