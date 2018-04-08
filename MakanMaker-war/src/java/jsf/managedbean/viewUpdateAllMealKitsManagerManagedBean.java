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
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
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
    private String recipeForUpdate;
    private MealKitEntity selectedMealKitToUpdate;
//    private String inputStringIngredients;
//    private String inputStringRecipe;
//    private List<MealKitEntity> filteredMealKits;
    private List<MealKitEntity> mealKits;
//    private List<String> updatedMealKitSelectedTagNames;
    
    private String updatePrice;
    private String updateNu;
    
    private List<TagEntity> tags;
    private MealKitEntity selectedMealKitToView;

    public viewUpdateAllMealKitsManagerManagedBean() {

        selectedMealKitToUpdate = new MealKitEntity();
//        inputStringIngredients = "";
//        inputStringRecipe = "";
        selectedMealKitToView = new MealKitEntity();
        selectItems = new ArrayList<>();
        mealKits = new ArrayList<>();
//        updatedMealKitSelectedTagNames = new ArrayList<>();
        tags = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        mealKits = mealKitControllerLocal.retrieveAllMealKits();
        tags = tagControllerLocal.retrieveAllTags();
        
        selectedMealKitToUpdate.setPrice(0.00);
        selectedMealKitToView.setPrice(0.00);
        
        for (TagEntity tag: tags){
            getSelectItems().add(new SelectItem(tag, tag.getName(), tag.getName()));
        }
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TagEntityConverter.tags", tags);
    
    }

    public void updateMealKit(ActionEvent event) {
        try {
            System.err.println("************updateMealKit(): Start saving new meal kit");
            System.err.println("***get IngredientsFor Update"+getIngredientsForUpdate());
            selectedMealKitToUpdate.setIngredients(Arrays.asList(getIngredientsForUpdate().split(";")));
            selectedMealKitToUpdate.setRecipe(Arrays.asList(getRecipeForUpdate().split(";")));
            selectedMealKitToUpdate.setPrice(Double.parseDouble(updatePrice));
            selectedMealKitToUpdate.setNutrition(Integer.parseInt(updateNu));
            Long mkId = selectedMealKitToUpdate.getMealKitId();
            
            if (!selectedMealKitToUpdate.getTags().isEmpty()) {
                tagControllerLocal.clearAllTags(mkId);
                for (TagEntity t : selectedMealKitToUpdate.getTags()) {
                    tagControllerLocal.linkTagAndMealKit(t.getTagId(), mkId);
                }
            }

            selectedMealKitToUpdate = mealKitControllerLocal.updateMealKit(selectedMealKitToUpdate);
            mealKits.remove(selectedMealKitToUpdate);
            getMealKits().add(getSelectedMealKitToUpdate());

            setSelectedMealKitToUpdate(new MealKitEntity());

//            updatedMealKitSelectedTagNames = new ArrayList<>();
//            String str = "";
//            inputStringIngredients = str;
//            inputStringRecipe = str;

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New meal kit " + mkId + " created successfully", null));
        } catch (MealKitExistException | GeneralException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }

    }

    @PreDestroy
    public void preDestroy() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("viewUpdateAllMealKitsManagerManagedBean.tags", null);
//        setInputStringIngredients("");
//        setInputStringRecipe("");
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

        List<TagEntity> selectedMKTags = (List<TagEntity>) value;

        String s = convertTagsToString(selectedMKTags);
        return s.contains(selectedTag);
    }

    public String getIngredientsForUpdate() {
        String longString = "";
        if (selectedMealKitToUpdate.getIngredients() == null || selectedMealKitToUpdate.getIngredients().isEmpty()) {
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

//    /**
//     * @return the inputStringIngredients
//     */
//    public String getInputStringIngredients() {
//        return inputStringIngredients;
//    }
//
//    /**
//     * @param inputStringIngredients the inputStringIngredients to set
//     */
//    public void setInputStringIngredients(String inputStringIngredients) {
//        this.inputStringIngredients = inputStringIngredients;
//    }
//
//    /**
//     * @return the inputStringRecipe
//     */
//    public String getInputStringRecipe() {
//        return inputStringRecipe;
//    }
//
//    /**
//     * @param inputStringRecipe the inputStringRecipe to set
//     */
//    public void setInputStringRecipe(String inputStringRecipe) {
//        this.inputStringRecipe = inputStringRecipe;
//    }

//    /**
//     * @return the updatedMealKitSelectedTagNames
//     */
//    public List<String> getUpdatedMealKitSelectedTagNames() {
//        return updatedMealKitSelectedTagNames;
//    }
//
//    /**
//     * @param updatedMealKitSelectedTagNames the updatedMealKitSelectedTagNames
//     * to set
//     */
//    public void setUpdatedMealKitSelectedTagNames(List<String> updatedMealKitSelectedTagNames) {
//        this.updatedMealKitSelectedTagNames = updatedMealKitSelectedTagNames;
//    }

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

//    /**
//     * @return the filteredMealKits
//     */
//    public List<MealKitEntity> getFilteredMealKits() {
//        return filteredMealKits;
//    }
//
//    /**
//     * @param filteredMealKits the filteredMealKits to set
//     */
//    public void setFilteredMealKits(List<MealKitEntity> filteredMealKits) {
//        this.filteredMealKits = filteredMealKits;
//    }

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

    /**
     * @return the recipeForUpdate
     */
    public String getRecipeForUpdate() {
        String longString = "";
        if (selectedMealKitToUpdate.getRecipe()== null || selectedMealKitToUpdate.getRecipe().isEmpty()) {
            return "-empty-";
        }
        
        for (String s : selectedMealKitToUpdate.getRecipe()) {
            longString += s + "; ";
        }
        if (recipeForUpdate == null || recipeForUpdate.length()==0) return longString;
        return recipeForUpdate;
    }

    /**
     * @param recipeForUpdate the recipeForUpdate to set
     */
    public void setRecipeForUpdate(String s) {
        recipeForUpdate = s;
        selectedMealKitToUpdate.setRecipe(Arrays.asList(getRecipeForUpdate().split(";")));
    }

    
    /*
    
    
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
    */
}
