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
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.DragDropEvent;
import util.exception.GeneralException;
import util.exception.MealKitExistException;


/**
 *
 * @author Katrina
 */
@Named(value = "viewUpdateAllMealKitsManagerManagedBean")
@ViewScoped
public class viewUpdateAllMealKitsManagerManagedBean implements Serializable {

    @EJB
    private TagControllerLocal tagControllerLocal;

    @EJB
    private MealKitControllerLocal mealKitControllerLocal;
    
    private MealKitEntity selectedMealKitToUpdate;
    private String inputStringIngredients;
    private String inputStringRecipe;
    
    private List<MealKitEntity> mealKits;
    private List<MealKitEntity> droppedMealKits;
    private List<SelectItem> selectItemsTagObject;
    private List<SelectItem> selectItemsTagName;
    private List<String> updatedMealKitSelectedTagNames;
    private List<String> tagNames;


    public viewUpdateAllMealKitsManagerManagedBean() {
        
        selectedMealKitToUpdate = new MealKitEntity();
        inputStringIngredients = "";
        inputStringRecipe = "";
        
        mealKits = new ArrayList<>();
        droppedMealKits = new ArrayList<>();
        selectItemsTagObject = new ArrayList<>();
        selectItemsTagName = new ArrayList<>();
        updatedMealKitSelectedTagNames = new ArrayList<>();
        tagNames = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        mealKits = mealKitControllerLocal.retrieveAllMealKits();
        List<TagEntity> tags = tagControllerLocal.retrieveAllTags();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("viewUpdateAllMealKitsManagerManagedBean.tags", tags);
        setInputStringIngredients("");
        setInputStringRecipe("");
        selectedMealKitToUpdate.setPrice(0.00);

        for(TagEntity tag:tags)
        {
            getSelectItemsTagObject().add(new SelectItem(tag, tag.getName()));
            getSelectItemsTagName().add(new SelectItem(tag.getTagCategory().toString(), tag.getTagCategory().toString()));
        }
        
        retrieveAllTagNames();
    }
    
    public void updateMealKit(ActionEvent event)
    {   
        System.err.println("************updateMealKit(): Start saving new meal kit");
    
        try {
            selectedMealKitToUpdate = mealKitControllerLocal.createNewMealKit(getSelectedMealKitToUpdate());
            Long updatedMealKitEntityId = selectedMealKitToUpdate.getMealKitId();
            
            if(!updatedMealKitSelectedTagNames.isEmpty()) {
                for(String tagName: getUpdatedMealKitSelectedTagNames()) {
                    Long tagId = tagControllerLocal.retrieveTagIdByTagName(tagName);
                    tagControllerLocal.linkTagAndMealKit(tagId, updatedMealKitEntityId);
                }
            }
            getSelectedMealKitToUpdate().setMealKitId(updatedMealKitEntityId);
            getMealKits().add(getSelectedMealKitToUpdate());
            setSelectedMealKitToUpdate(new MealKitEntity());
            
            updatedMealKitSelectedTagNames = new ArrayList<>();
            String str = "";
            inputStringIngredients = str;
            inputStringRecipe = str;
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New meal kit " + updatedMealKitEntityId + " created successfully", null));
            
        } catch (MealKitExistException | GeneralException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "An unexpected error occured: " + ex.getMessage(), null));
        }
    }
    
    public void onMKDrop(DragDropEvent ddEvent) {
        MealKitEntity mk = ((MealKitEntity) ddEvent.getData());
  
        droppedMealKits.add(mk);
        mealKits.remove(mk);
    }

    @PreDestroy
    public void preDestroy()
    {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("viewUpdateAllMealKitsManagerManagedBean.tags", null);
        setInputStringIngredients("");
        setInputStringRecipe("");
        tagNames = new ArrayList<>();
    }
    
    
    
    public void updateMealKitMessage() {
        String summary = getSelectedMealKitToUpdate().isIsAvailable() ? "Set to Available" : "Set to Unavailable";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }
    
    public void handleIngredientsText(){
        selectedMealKitToUpdate.setIngredients(Arrays.asList(getInputStringIngredients().split(";")));
        System.err.println("************viewUpdateAllMealKitsManagerManagedBean.handleIngredientsText(): Ingredients set");
    }
    
    public void handleRecipeText(){
        selectedMealKitToUpdate.setIngredients(Arrays.asList(getInputStringRecipe().split(";")));
        System.err.println("************viewUpdateAllMealKitsManagerManagedBean.handleRecipeText(): Recipe set");
    }
    
    public List<String> retrieveAllTagNames() {
        List<TagEntity> tags = tagControllerLocal.retrieveAllTags();
        for (TagEntity tag: tags) {
            getTagNames().add(tag.getName());
        }
        return getTagNames();
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
     * @return the selectedMealKitToUpdate
     */
    public MealKitEntity getSelectedMealKitToUpdate() {
        System.err.println("*********.getSelectedMealKitToUpdate() was accessed");
        return selectedMealKitToUpdate;
    }

    /**
     * @param selectedMealKitToUpdate the selectedMealKitToUpdate to set
     */
    public void setSelectedMealKitToUpdate(MealKitEntity selectedMealKitToUpdate) {
        this.selectedMealKitToUpdate = selectedMealKitToUpdate;
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
     * @return the selectItemsTagCategory
     */
    public List<SelectItem> getSelectItemsTagName() {
        return selectItemsTagName;
    }
    
    public void setSelectItemsTagName(List<SelectItem> selectItemsTagName) {
        this.selectItemsTagName = selectItemsTagName;
    }

    /**
     * @return the droppedMealKits
     */
    public List<MealKitEntity> getDroppedMealKits() {
        return droppedMealKits;
    }

    /**
     * @param droppedMealKits the droppedMealKits to set
     */
    public void setDroppedMealKits(List<MealKitEntity> droppedMealKits) {
        this.droppedMealKits = droppedMealKits;
    }

    /**
     * @return the inputStringIngredients
     */
    public String getInputStringIngredients() {
        return inputStringIngredients;
    }

    /**
     * @param inputStringIngredients the inputStringIngredients to set
     */
    public void setInputStringIngredients(String inputStringIngredients) {
        this.inputStringIngredients = inputStringIngredients;
    }

    /**
     * @return the inputStringRecipe
     */
    public String getInputStringRecipe() {
        return inputStringRecipe;
    }

    /**
     * @param inputStringRecipe the inputStringRecipe to set
     */
    public void setInputStringRecipe(String inputStringRecipe) {
        this.inputStringRecipe = inputStringRecipe;
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
     * @return the updatedMealKitSelectedTagNames
     */
    public List<String> getUpdatedMealKitSelectedTagNames() {
        return updatedMealKitSelectedTagNames;
    }

    /**
     * @param updatedMealKitSelectedTagNames the updatedMealKitSelectedTagNames to set
     */
    public void setUpdatedMealKitSelectedTagNames(List<String> updatedMealKitSelectedTagNames) {
        this.updatedMealKitSelectedTagNames = updatedMealKitSelectedTagNames;
    }

    
}
