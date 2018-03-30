/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MealKitControllerLocal;
import ejb.session.stateless.TagControllerLocal;
import entity.MealKitEntity;
import entity.TagEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import util.exception.GeneralException;
import util.exception.MealKitExistException;

/**
 *
 * @author Katrina
 */
@Named(value = "createMealKitManagedBean")
@ViewScoped
public class createMealKitManagedBean implements Serializable{

    @EJB
    private TagControllerLocal tagControllerLocal;
    
    @EJB
    private MealKitControllerLocal mealKitControllerLocal;
    
    private MealKitEntity newMealKit;
    private MealKitEntity selectedMealKitToView;
    private MealKitEntity selectedMealKitToUpdate;
    private List<String> tagNames; 
    private List<String> newMealKitSelectedTagNames;
    private List<MealKitEntity> mealKits;
    private List<MealKitEntity> filteredMealKits;
    private List<SelectItem> selectItemsTagObject;
    private List<SelectItem> selectItemsTagName;
    /**
     * Creates a new instance of createMealKitManagedBean
     */
    public createMealKitManagedBean() 
    {
        newMealKit = new MealKitEntity();
        tagNames = new ArrayList<>();
        newMealKitSelectedTagNames = new ArrayList<>();
        mealKits = new ArrayList<>();
        filteredMealKits = new ArrayList<>();
        selectItemsTagObject = new ArrayList<>();
        selectItemsTagName = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setMealKits(mealKitControllerLocal.retrieveAllMealKits());
        setFilteredMealKits(getMealKits());
        List<TagEntity> tags = tagControllerLocal.retrieveAllTags();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("createMealKitManagedBean.tags", tags);
        
        for(TagEntity tag:tags)
        {
            getSelectItemsTagObject().add(new SelectItem(tag, tag.getName()));
            getSelectItemsTagName().add(new SelectItem(tag.getName(), tag.getName()));
        }
    }
    
    @PreDestroy
    public void preDestroy()
    {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("createMealKitManagedBean.tags", null);
    }
    
    public void saveNewMealKit(ActionEvent event)
    {   
        try {
            setNewMealKit(mealKitControllerLocal.createNewMealKit(getNewMealKit()));
            Long newMealKitEntityId = getNewMealKit().getMealKitId();
            for(String tagName: getNewMealKitSelectedTagNames()) {
                Long tagId = tagControllerLocal.retrieveTagIdByTagName(tagName);
                tagControllerLocal.linkTagAndMealKit(tagId, newMealKitEntityId);
            }
            getNewMealKit().setMealKitId(newMealKitEntityId);
            getMealKits().add(getNewMealKit());
            
            setNewMealKit(new MealKitEntity());
        
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New meal kit " + newMealKitEntityId + " created successfully", null));
            
        } catch (MealKitExistException | GeneralException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  ex.getMessage(), ""));
        }
    }
    
    public void addNewMealKitMessage() {
        String summary = getNewMealKit().isIsAvailable() ? "Set to Available" : "Set to Unavailable";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }
    
    public List<String> retrieveAllTagNames() {
        List<TagEntity> tags = tagControllerLocal.retrieveAllTags();
        for (TagEntity tag: tags) {
            getTagNames().add(tag.getName());
        }
        return getTagNames();
    }

    /**
     * @return the newMealKit
     */
    public MealKitEntity getNewMealKit() {
        return newMealKit;
    }

    /**
     * @param newMealKit the newMealKit to set
     */
    public void setNewMealKit(MealKitEntity newMealKit) {
        this.newMealKit = newMealKit;
    }

    /**
     * @return the selectedMealKitToView
     */
    public MealKitEntity getSelectedMealKitToView() {
        return selectedMealKitToView;
    }

    /**
     * @param selectedMealKitToView the selectedMealKitToView to set
     */
    public void setSelectedMealKitToView(MealKitEntity selectedMealKitToView) {
        this.selectedMealKitToView = selectedMealKitToView;
    }

    /**
     * @return the selectedMealKitToUpdate
     */
    public MealKitEntity getSelectedMealKitToUpdate() {
        return selectedMealKitToUpdate;
    }

    /**
     * @param selectedMealKitToUpdate the selectedMealKitToUpdate to set
     */
    public void setSelectedMealKitToUpdate(MealKitEntity selectedMealKitToUpdate) {
        this.selectedMealKitToUpdate = selectedMealKitToUpdate;
    }

    /**
     * @return the tagNames
     */
    public List<String> getTagNames() {
        return tagNames;
    }

    /**
     * @param tagNames the tagNames to set
     */
    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    /**
     * @return the newMealKitSelectedTagNames
     */
    public List<String> getNewMealKitSelectedTagNames() {
        return newMealKitSelectedTagNames;
    }

    /**
     * @param newMealKitSelectedTagNames the newMealKitSelectedTagNames to set
     */
    public void setNewMealKitSelectedTagNames(List<String> newMealKitSelectedTagNames) {
        this.newMealKitSelectedTagNames = newMealKitSelectedTagNames;
    }

    /**
     * @return the mealKits
     */
    public List<MealKitEntity> getMealKits() {
        return mealKits;
    }

    /**
     * @param mealKits the mealKits to set
     */
    public void setMealKits(List<MealKitEntity> mealKits) {
        this.mealKits = mealKits;
    }

    /**
     * @return the filteredMealKits
     */
    public List<MealKitEntity> getFilteredMealKits() {
        return filteredMealKits;
    }

    /**
     * @param filteredMealKits the filteredMealKits to set
     */
    public void setFilteredMealKits(List<MealKitEntity> filteredMealKits) {
        this.filteredMealKits = filteredMealKits;
    }

    /**
     * @return the selectItemsTagObject
     */
    public List<SelectItem> getSelectItemsTagObject() {
        return selectItemsTagObject;
    }

    /**
     * @param selectItemsTagObject the selectItemsTagObject to set
     */
    public void setSelectItemsTagObject(List<SelectItem> selectItemsTagObject) {
        this.selectItemsTagObject = selectItemsTagObject;
    }

    /**
     * @return the selectItemsTagName
     */
    public List<SelectItem> getSelectItemsTagName() {
        return selectItemsTagName;
    }

    /**
     * @param selectItemsTagName the selectItemsTagName to set
     */
    public void setSelectItemsTagName(List<SelectItem> selectItemsTagName) {
        this.selectItemsTagName = selectItemsTagName;
    }
    
}
