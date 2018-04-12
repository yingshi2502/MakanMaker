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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import util.exception.CustomerNotFoundException;
import util.exception.EmptyListException;
import util.exception.PasswordChangeException;

/**
 *
 * @author yingshi
 */
@Named(value = "profileManagedBean")
@RequestScoped
public class profileManagedBean {

    @EJB(name = "CustomerControllerLocal")
    private CustomerControllerLocal customerControllerLocal;

    @EJB(name = "OrderControllerLocal")
    private OrderControllerLocal orderControllerLocal;

    @EJB(name = "AddressControllerLocal")
    private AddressControllerLocal addressControllerLocal;

    private CustomerEntity currentCustomer;
    private CustomerEntity customerToUpdate;
    private AddressEntity defaultAddress;
    private boolean noAddress;
    private boolean noOrder;
    private List<OrderEntity> recentOrders;

    private String currPass;
    private String newPass;
    private String repeatedPass;

    public profileManagedBean() {
        recentOrders = new ArrayList<>();
        noAddress = false;
    }

    @PostConstruct
    public void postConstruct() {
        currentCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
        
        try {
            defaultAddress = addressControllerLocal.getDefaultAddressById(currentCustomer.getCustomerId());
        } catch (EmptyListException ex) {
            defaultAddress = null;
            setNoAddress(true);
        }catch(NullPointerException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please Login", null));
            try {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

                FacesContext.getCurrentInstance().getExternalContext().redirect("/MakanMaker-war/index.xhtml");
            } catch (IOException ex1) {
                Logger.getLogger(WishListManagedBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        try {
            recentOrders = orderControllerLocal.retrieveOrderByCustomerId(currentCustomer.getCustomerId());
        } catch (EmptyListException ex) {
            setNoOrder(true);
        }
    }

    public void updateProfile(ActionEvent event) throws IOException {
        try {
            System.err.println("** update profile");
            CustomerEntity updated = customerControllerLocal.updateCustomer(currentCustomer);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().replace("currentCustomerEntity", updated);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Update Successful ", null));
            RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", "Updated Successfully!"));

            FacesContext.getCurrentInstance().getExternalContext().redirect("myProfile.xhtml");

        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Update Failed" + ex.getMessage(), null));
        }

    }

    public void changePassword(ActionEvent event) throws IOException {
        try {
            if (!repeatedPass.equals(newPass)) {
                FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mismatch! ", null));
                System.err.println("*** Password Mismatch");
            } else {
                customerControllerLocal.changePassword(currentCustomer.getCustomerId(), currPass, newPass);
                System.err.println("*** Password Succeed");
                FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Update Successful ", null));
                FacesContext.getCurrentInstance().getExternalContext().redirect("myProfile.xhtml");
            }

        } catch (PasswordChangeException ex) {
            System.err.println("*** Old password wrong");

            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Old password does not match!", null));
        }
    }
    
    public void logout(ActionEvent event) throws IOException {
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        //FacesContext.getCurrentInstance().getExternalContext().redirect("#{request.contextPath}/index.xhtml"); //got problem maybe
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(context.getApplicationContextPath() + "/index.xhtml");
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

    public String getRepeatedPass() {
        return repeatedPass;
    }

    public void setRepeatedPass(String repeatedPass) {
        this.repeatedPass = repeatedPass;
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
     * @return the recentOrders
     */
    public List<OrderEntity> getRecentOrders() {
        return recentOrders;
    }

    /**
     * @param recentOrders the recentOrders to set
     */
    public void setRecentOrders(List<OrderEntity> recentOrders) {
        this.recentOrders = recentOrders;
    }

    /**
     * @return the noAddress
     */
    public boolean isNoAddress() {
        return noAddress;
    }

    /**
     * @return the customerToUpdate
     */
    public CustomerEntity getCustomerToUpdate() {
        return customerToUpdate;
    }

    /**
     * @param customerToUpdate the customerToUpdate to set
     */
    public void setCustomerToUpdate(CustomerEntity customerToUpdate) {
        this.customerToUpdate = customerToUpdate;
    }

    /**
     * @param noAddress the noAddress to set
     */
    public void setNoAddress(boolean noAddress) {
        this.noAddress = noAddress;
    }

    /**
     * @return the noOrder
     */
    public boolean isNoOrder() {
        return noOrder;
    }

    /**
     * @param noOrder the noOrder to set
     */
    public void setNoOrder(boolean noOrder) {
        this.noOrder = noOrder;
    }

    /**
     * @return the currPass
     */
    public String getCurrPass() {
        return currPass;
    }

    /**
     * @param currPass the currPass to set
     */
    public void setCurrPass(String currPass) {
        this.currPass = currPass;
    }

    /**
     * @return the newPass
     */
    public String getNewPass() {
        return newPass;
    }

    /**
     * @param newPass the newPass to set
     */
    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

}
