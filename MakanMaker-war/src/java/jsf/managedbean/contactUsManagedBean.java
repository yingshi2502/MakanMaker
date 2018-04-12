/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EmailControllerLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Ismahfaris
 */
@Named(value = "contactUsManagedBean")
@ViewScoped
public class contactUsManagedBean implements Serializable{

    @EJB(name = "EmailControllerLocal")
    private EmailControllerLocal emailControllerLocal;
    
    
    
    private String fromEmailAddress;
    private String content;
    private String title;
    private String isMMAcc;
    private String typeOfQuestions;
    
    public contactUsManagedBean() {
    }
    
    
    public List<String> completeTextOne(String query) {
        List<String> results = new ArrayList<String>();

        results.add(query + "Delivery/Shipping issues");
        results.add(query + "Skip upcoming deliveries");
        results.add(query + "Past and Future Payments");
        results.add(query + "Ingredients in my order");
        results.add(query + "My account settings");
        results.add(query + "Gifts, vouchers and trials");
        results.add(query + "General");
         
        return results;
    }
    
        public List<String> completeTextTwo(String query) {
        List<String> results = new ArrayList<String>();

        results.add(query + "Yes");
        results.add(query + "No. I do not have a Makan Maker account");
         
        return results;
    }

    public void sendContactUsForm(ActionEvent event){
        try {
            emailControllerLocal.emailSendContactUs(getFromEmailAddress(), getTitle(), getContent(),typeOfQuestions, isMMAcc.equals("Yes"));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Email Sent", "Thanks for contacting us!"));
        } catch (InterruptedException ex) {
        }
    }
        
        
        
    /**
     * @return the typeOfQuestions
     */
    public String getTypeOfQuestions() {
        return typeOfQuestions;
    }

    /**
     * @param typeOfQuestions the typeOfQuestions to set
     */
    public void setTypeOfQuestions(String typeOfQuestions) {
        this.typeOfQuestions = typeOfQuestions;
    }

    /**
     * @return the isMMAcc
     */
    public String getIsMMAcc() {
        return isMMAcc;
    }

    /**
     * @param isMMAcc the isMMAcc to set
     */
    public void setIsMMAcc(String isMMAcc) {
        this.isMMAcc = isMMAcc;
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

    /**
     * @return the fromEmailAddress
     */
    public String getFromEmailAddress() {
        return fromEmailAddress;
    }

    /**
     * @param fromEmailAddress the fromEmailAddress to set
     */
    public void setFromEmailAddress(String fromEmailAddress) {
        this.fromEmailAddress = fromEmailAddress;
    }


}
