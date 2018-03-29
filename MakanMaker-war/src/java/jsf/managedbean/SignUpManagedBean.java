/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerControllerLocal;
import entity.CustomerEntity;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CustomerExistException;
import util.exception.GeneralException;

/**
 *
 * @author yingshi
 */
@Named(value = "signUpManagedBean")
@RequestScoped
public class SignUpManagedBean {

    @EJB(name = "CustomerControllerLocal")
    private CustomerControllerLocal customerControllerLocal;

    private String email;
    private String password;
    private String reTypePassword;
    private CustomerEntity newCustomer;
    
    
    public SignUpManagedBean() {
        newCustomer = new CustomerEntity();
    }
    
    public void signup(ActionEvent event) throws IOException{
        System.err.println("New Customer Info"+newCustomer.getFullName());
        if (!password.equals(reTypePassword)){
            FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Invalid details", "Two password input mismatch!"));
        }else{
            try {
                newCustomer.setPassword(password);
                customerControllerLocal.createNewCustomer(newCustomer);
                newCustomer = new CustomerEntity();
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
                FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid details","Two password input mismatch!"));
            } catch (CustomerExistException ex) {
            FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Invalid details", "Username or email already exists"));
            } catch (GeneralException ex) {
                Logger.getLogger(SignUpManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setReTypePassword(String reTypePassword) {
        this.reTypePassword = reTypePassword;
    }

    public void setNewCustomer(CustomerEntity newCustomer) {
        this.newCustomer = newCustomer;
    }

    
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getReTypePassword() {
        return reTypePassword;
    }

    public CustomerEntity getNewCustomer() {
        return newCustomer;
    }
    
    
}
