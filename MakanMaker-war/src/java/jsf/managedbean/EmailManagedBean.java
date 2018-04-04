/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EmailControllerLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author yingshi
 */
@Named(value = "emailManagedBean")
@RequestScoped
public class EmailManagedBean {

    
    @EJB
    private EmailControllerLocal emailControllerLocal;

    
    
    /**
     * Creates a new instance of EmailManagedBean
     */
    private String fromEmailAddress;
    private String toEmailAddress;
    private String name;
    private String content;
    private String luokuan;
    private String title;
    
    public EmailManagedBean() {
    }

    public void sendAprFool(ActionEvent event){
        try {
            emailControllerLocal.emailAprFool(fromEmailAddress, toEmailAddress, name, getTitle(), getContent(), getLuokuan());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Email Sent", null));
        } catch (InterruptedException ex) {
            Logger.getLogger(EmailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void setFromEmailAddress(String fromEmailAddress) {
        this.fromEmailAddress = fromEmailAddress;
    }

    public void setToEmailAddress(String toEmailAddress) {
        this.toEmailAddress = toEmailAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromEmailAddress() {
        return fromEmailAddress;
    }

    public String getToEmailAddress() {
        return toEmailAddress;
    }

    public String getName() {
        return name;
    }
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the luokuan
     */
    public String getLuokuan() {
        return luokuan;
    }

    /**
     * @param luokuan the luokuan to set
     */
    public void setLuokuan(String luokuan) {
        this.luokuan = luokuan;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    
    
}
