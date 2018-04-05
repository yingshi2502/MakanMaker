package jsf.managedbean;

import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.StaffControllerLocal;
import entity.CustomerEntity;
import entity.ManagerEntity;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import util.exception.InvalidLoginCredentialException;



@Named(value = "indexBean")
@ViewScoped

public class IndexManagedBean implements Serializable
{

    @EJB
    private StaffControllerLocal staffController;
    
    @EJB
    private CustomerControllerLocal customerControllerLocal;
    
    private String username;
    private String password;
    
    private String searchKeywords;
    
    public IndexManagedBean()
    {
        searchKeywords="";
    }
    
    
    public void customerLogin(ActionEvent event) throws IOException
    {
        System.err.println("****Customer Login ");

        try
        {
            CustomerEntity currentCustomerEntity = customerControllerLocal.customerLogin(username, password);
            System.err.println("****Customer Login no exception thrown ");
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentCustomerEntity", currentCustomerEntity);
            FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO,"Login success!", "welcome"));

            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");     
        }
        catch(InvalidLoginCredentialException ex)
        {
            System.err.println("**** error"+ex.getMessage());
            FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Invalid login credential: " + ex.getMessage(), "Please try again"));
        }
    }
    
    public void staffLogin(ActionEvent event) throws IOException{
        try{
            ManagerEntity currentManagerEntity = staffController.staffLogin(username, password);
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentManagerEntity", currentManagerEntity);
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");     
        }catch(InvalidLoginCredentialException ex){
            System.err.println("****"+ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credential: " + ex.getMessage(), null));
        }
    }
    
    public void logout(ActionEvent event) throws IOException
    {
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        //FacesContext.getCurrentInstance().getExternalContext().redirect("#{request.contextPath}/index.xhtml"); //got problem maybe
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(context.getApplicationContextPath() + "/index.xhtml");
    }
    
    public void search(ActionEvent event) throws IOException{
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        if (searchKeywords.length() >0){
            context.redirect(context.getApplicationContextPath() +"/mealkit/viewAllMealkits.xhtml?keywords="+searchKeywords);     
        }else{
            context.redirect(context.getApplicationContextPath() +"/mealkit/viewAllMealkits.xhtml?");     
        }

    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the searchKeywords
     */
    public String getSearchKeywords() {
        return searchKeywords.trim();
    }

    /**
     * @param searchKeywords the searchKeywords to set
     */
    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

}