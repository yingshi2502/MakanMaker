/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MealKitControllerLocal;
import entity.MealKitEntity;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import util.exception.MealKitNotFoundException;

/**
 *
 * @author Ismahfaris
 */
@Named(value = "viewMealKitDetailsManagedBean")
@ViewScoped
public class viewMealKitDetailsManagedBean implements Serializable {
    @EJB
    private MealKitControllerLocal mealKitControllerLocal;
    
    private Long mealKitId;
    private MealKitEntity mealKitEntityToView;
    /**
     * Creates a new instance of viewMealKitDetailsManagedBean
     */
    public viewMealKitDetailsManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct()
    {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String value = request.getParameter("id");
        setMealKitId(Long.valueOf(value));
        
        try
        {            
            setMealKitEntityToView(mealKitControllerLocal.retrieveMealKitById(getMealKitId()));
        }
//        catch(MealKitNotFoundException ex)
//        {
//            setMealKitEntityToView(new MealKitEntity());
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the mealKit details: " + ex.getMessage(), null));
//        }
        catch(Exception ex)
        {
            setMealKitEntityToView(new MealKitEntity());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    
        public void back(ActionEvent event) throws IOException
    {
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllMealKits.xhtml");
    }

    public void updateMealKit(ActionEvent event) throws IOException
    {
        System.err.println("********* mealKitEntityToView.getMealKitId(): " + mealKitEntityToView.getMealKitId());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("mealKitIdToUpdate", mealKitEntityToView.getMealKitId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("updateMealKit.xhtml");
    }
    
    public void deleteMealKit(ActionEvent event) throws IOException
    {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("mealKitIdToDelete", mealKitEntityToView.getMealKitId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("deleteMealKit.xhtml");
    }    
        
        
    /**
     * @return the mealKitEntityToView
     */
    public MealKitEntity getMealKitEntityToView() {
        return mealKitEntityToView;
    }

    /**
     * @param mealKitEntityToView the mealKitEntityToView to set
     */
    public void setMealKitEntityToView(MealKitEntity mealKitEntityToView) {
        this.mealKitEntityToView = mealKitEntityToView;
    }

    /**
     * @return the mealKitId
     */
    public Long getMealKitId() {
        return mealKitId;
    }

    /**
     * @param mealKitId the mealKitId to set
     */
    public void setMealKitId(Long mealKitId) {
        this.mealKitId = mealKitId;
    }
    
}
