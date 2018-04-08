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
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    private List<SelectItem> selectItems; 
    private String ingredientsForUpdate;
    private MealKitEntity selectedMealKitToUpdate;
    private String inputStringIngredients;
    private String inputStringRecipe;
    private List<MealKitEntity> filteredMealKits;
    private List<MealKitEntity> mealKits;
    private List<MealKitEntity> droppedMealKits;
    private List<SelectItem> selectItemsTagObject;
    private List<SelectItem> selectItemsTagName;
    private List<String> updatedMealKitSelectedTagNames;
    
    private String updatePrice;
    private String updateNu;
//    private List<String> tagNames;
    private List<TagEntity> tags;
    private MealKitEntity selectedMealKitToView;

    public viewUpdateAllMealKitsManagerManagedBean() {

        selectedMealKitToUpdate = new MealKitEntity();
        inputStringIngredients = "";
        inputStringRecipe = "";
        selectedMealKitToView = new MealKitEntity();
        selectItems = new ArrayList<>();
        mealKits = new ArrayList<>();
        droppedMealKits = new ArrayList<>();
        selectItemsTagObject = new ArrayList<>();
        selectItemsTagName = new ArrayList<>();
        updatedMealKitSelectedTagNames = new ArrayList<>();
        tags = new ArrayList<>();
//        tagNames = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        mealKits = mealKitControllerLocal.retrieveAllMealKits();
        tags = tagControllerLocal.retrieveAllTags();
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("viewUpdateAllMealKitsManagerManagedBean.tags", tags);
        setInputStringIngredients("");
        setInputStringRecipe("");
        selectedMealKitToUpdate.setPrice(0.00);
        selectedMealKitToView.setPrice(0.00);
        
        for (TagEntity tag: tags){
            getSelectItems().add(new SelectItem(tag, tag.getName(), tag.getName()));
        }
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TagEntityConverter.tags", tags);
    
        
        
//        for(TagEntity tag:tags)
//        {
//            getSelectItemsTagObject().add(new SelectItem(tag, tag.getName()));
//            getSelectItemsTagName().add(new SelectItem(tag.getTagCategory().toString(), tag.getTagCategory().toString()));
//        }

//        retrieveAllTagNames();
    }

    public void updateMealKit(ActionEvent event) {
        try {
            System.err.println("************updateMealKit(): Start saving new meal kit");
            System.err.println("***get IngredientsFor Update"+getIngredientsForUpdate());
            selectedMealKitToUpdate.setIngredients(Arrays.asList(getIngredientsForUpdate().split(";")));
            selectedMealKitToUpdate.setPrice(Double.parseDouble(updatePrice));
            selectedMealKitToUpdate.setNutrition(Integer.parseInt(updateNu));
            Long mkId = selectedMealKitToUpdate.getMealKitId();

            
            if (!updatedMealKitSelectedTagNames.isEmpty()) {
                tagControllerLocal.clearAllTags(mkId);
                for (TagEntity t : selectedMealKitToUpdate.getTags()) {
                    tagControllerLocal.linkTagAndMealKit(t.getTagId(), mkId);
                }
            }

            selectedMealKitToUpdate = mealKitControllerLocal.updateMealKit(selectedMealKitToUpdate);
//            
//            Long updatedMealKitEntityId = selectedMealKitToUpdate.getMealKitId();
//            
//            if(!updatedMealKitSelectedTagNames.isEmpty()) {
//                for(String tagName: getUpdatedMealKitSelectedTagNames()) {
//                    Long tagId = tagControllerLocal.retrieveTagIdByTagName(tagName);
//                    tagControllerLocal.linkTagAndMealKit(tagId, updatedMealKitEntityId);
//                }
//            }
            mealKits.remove(selectedMealKitToUpdate);
            getMealKits().add(getSelectedMealKitToUpdate());

            setSelectedMealKitToUpdate(new MealKitEntity());

            updatedMealKitSelectedTagNames = new ArrayList<>();
            String str = "";
            inputStringIngredients = str;
            inputStringRecipe = str;

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New meal kit " + mkId + " created successfully", null));
        } catch (MealKitExistException | GeneralException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }

    }

//    public void onMKDrop(DragDropEvent ddEvent) {
//        MealKitEntity mk = ((MealKitEntity) ddEvent.getData());
//  
//        droppedMealKits.add(mk);
//        mealKits.remove(mk);
//    }
    @PreDestroy
    public void preDestroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("viewUpdateAllMealKitsManagerManagedBean.tags", null);
        setInputStringIngredients("");
        setInputStringRecipe("");
    }

    private String convertTagsToString(List<TagEntity> tags) {
        String s = "";
        for (TagEntity t : tags) {
            s += t.getName() + ", ";
        }
//        s = s.substring(0, s.length()-2);
        return s;
    }

    public boolean filterByTags(Object value, Object filter, Locale locale) {

        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.isEmpty()) {
            return true;
        }
        if (value == null) {
            return false;
        }

        String selectedTag = filter.toString();
//        System.err.println("***"+selectedTag + " "+filter);

        List<TagEntity> selectedMKTags = (List<TagEntity>) value;
//        System.err.println("**** tags size"+selectedMKTags.size());

        String s = convertTagsToString(selectedMKTags);
//        System.err.println("****tags "+s);
//        System.err.println("**** result"+ s.contains(selectedTag));
        return s.contains(selectedTag);
    }

    public String getIngredientsForUpdate() {
        String longString = "";
        if (selectedMealKitToUpdate.getIngredients() == null) {
            return "empty";
        }
        
        for (String s : selectedMealKitToUpdate.getIngredients()) {
            longString += s + "; ";
        }
        if (ingredientsForUpdate == null || ingredientsForUpdate.length()==0) return longString;
        return ingredientsForUpdate;
    }

    public void setIngredientsForUpdate(String s) {
        ingredientsForUpdate = s;
        selectedMealKitToUpdate.setIngredients(Arrays.asList(getIngredientsForUpdate().split(";")));
    }

    public void updateMealKitMessage() {
        String summary = getSelectedMealKitToUpdate().isIsAvailable() ? "Set to Available" : "Set to Unavailable";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }

    public void handleIngredientsText() {
        selectedMealKitToUpdate.setIngredients(Arrays.asList(getInputStringIngredients().split(";")));
//        System.err.println("************viewUpdateAllMealKitsManagerManagedBean.handleIngredientsText(): Ingredients set");
    }

    public void handleRecipeText() {
        selectedMealKitToUpdate.setIngredients(Arrays.asList(getInputStringRecipe().split(";")));
//        System.err.println("************viewUpdateAllMealKitsManagerManagedBean.handleRecipeText(): Recipe set");
    }

//    public List<String> retrieveAllTagNames() {
//        List<TagEntity> tags = tagControllerLocal.retrieveAllTags();
//        for (TagEntity tag: tags) {
//            getTagNames().add(tag.getName());
//        }
//        return getTagNames();
//    }
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
//        System.err.println("*********.getSelectedMealKitToUpdate() was accessed");
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

//    /**
//     * @return the tagNames
//     */
//    public List<String> getTagNames() {
//        return tagNames;
//    }
//
//    /**
//     * @param tagNames the tagNames to set
//     */
//    public void setTagNames(List<String> tagNames) {
//        this.tagNames = tagNames;
//    }
    /**
     * @return the updatedMealKitSelectedTagNames
     */
    public List<String> getUpdatedMealKitSelectedTagNames() {
        return updatedMealKitSelectedTagNames;
    }

    /**
     * @param updatedMealKitSelectedTagNames the updatedMealKitSelectedTagNames
     * to set
     */
    public void setUpdatedMealKitSelectedTagNames(List<String> updatedMealKitSelectedTagNames) {
        this.updatedMealKitSelectedTagNames = updatedMealKitSelectedTagNames;
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
     * @return the tags
     */
    public List<TagEntity> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
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
     * @return the updatePrice
     */
    public String getUpdatePrice() {
        if (updatePrice != null){
            return updatePrice;
        }
//        if(selectedMealKitToUpdate.getPrice() == null) return "";
        return ""+selectedMealKitToUpdate.getPrice();
    }

    /**
     * @param updatePrice the updatePrice to set
     */
    public void setUpdatePrice(String updatePrice) {
        this.updatePrice = updatePrice;
    }

    /**
     * @return the updateNu
     */
    public String getUpdateNu() {
        if (updateNu != null) return updateNu;
        if(selectedMealKitToUpdate.getNutrition() == null) return "";
        return ""+selectedMealKitToUpdate.getNutrition();
    }

    /**
     * @param updateNu the updateNu to set
     */
    public void setUpdateNu(String updateNu) {
        this.updateNu = updateNu;
    }

    /**
     * @return the selectItems
     */
    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    /**
     * @param selectItems the selectItems to set
     */
    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

}
