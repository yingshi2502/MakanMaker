package jsf.managedbean;

import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.StaffControllerLocal;
import entity.CustomerEntity;
import entity.ManagerEntity;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import util.exception.InvalidLoginCredentialException;



@Named(value = "indexBean")
@RequestScoped

public class IndexManagedBean
{

    @EJB
    private StaffControllerLocal staffController;
    
    @EJB
    private CustomerControllerLocal customerControllerLocal;
    
    private String username;
    private String password;
    
    
    public IndexManagedBean()
    {
        
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Login success!", "welcome"));

            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");     
        }
        catch(InvalidLoginCredentialException ex)
        {
            System.err.println("**** error"+ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid login credential: " + ex.getMessage(), "Please try again"));
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
        FacesContext.getCurrentInstance().getExternalContext().redirect("#{request.contextPath}/index.xhtml"); //got problem maybe
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

}