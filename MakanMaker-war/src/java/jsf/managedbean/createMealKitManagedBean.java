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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
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
    private UploadedFile uploadedImage;
    private String inputStringRecipe;
    private boolean uploadedOne;
    private List<String> recipe;
    
    /**
     * Creates a new instance of createMealKitManagedBean
     */
    public createMealKitManagedBean() 
    {
        newMealKit = new MealKitEntity();
        newMealKit.setIsAvailable(true);
        tagNames = new ArrayList<>();
        newMealKitSelectedTagNames = new ArrayList<>();
        mealKits = new ArrayList<>();
        filteredMealKits = new ArrayList<>();
        selectItemsTagObject = new ArrayList<>();
        selectItemsTagName = new ArrayList<>();
        inputStringRecipe = "";
        recipe = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        System.err.println("************createMealKitManagedBean.postConstruct(): postConstruct method begins");

        setMealKits(mealKitControllerLocal.retrieveAllMealKits());
        setFilteredMealKits(getMealKits());
        List<TagEntity> tags = tagControllerLocal.retrieveAllTags();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("createMealKitManagedBean.tags", tags);
        inputStringRecipe = "";
        newMealKit.setPrice(0.00);
        
        for(TagEntity tag:tags)
        {
            getSelectItemsTagObject().add(new SelectItem(tag, tag.getName()));
            getSelectItemsTagName().add(new SelectItem(tag.getName(), tag.getName()));
        }
        retrieveAllTagNames();
    }
    
    @PreDestroy
    public void preDestroy()
    {
        System.err.println("************createMealKitManagedBean.preDestroy(): preDestroy method begins");

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("createMealKitManagedBean.tags", null);
        tagNames = new ArrayList<>();
    }
    
    public void saveNewMealKit(ActionEvent event) throws IOException
    {   
        System.err.println("************createMealKitManagedBean.saveNewMealKit(): Start saving new meal kit");
    
        try {
            newMealKit.setRecipe(recipe);
//            newMealKit.setRecipe(Arrays.asList(inputStringRecipe.trim().split(";")));
            newMealKit = mealKitControllerLocal.createNewMealKit(getNewMealKit());
            Long newMealKitEntityId = newMealKit.getMealKitId();
            
            if(!newMealKitSelectedTagNames.isEmpty()) {
                for(String tagName: getNewMealKitSelectedTagNames()) {
                    Long tagId = tagControllerLocal.retrieveTagIdByTagName(tagName);
                    tagControllerLocal.linkTagAndMealKit(tagId, newMealKitEntityId);
                }
            }
            getNewMealKit().setMealKitId(newMealKitEntityId);
            getMealKits().add(getNewMealKit());
            setNewMealKit(new MealKitEntity());
            
            newMealKitSelectedTagNames = new ArrayList<>();
            String str = "";
            inputStringRecipe = str;
            newMealKit.setPrice(0.00);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New meal kit " + newMealKitEntityId + " created successfully", null));
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(context.getApplicationContextPath() + "/managerPages/viewUpdateAllMealKitsManager.xhtml");
            
        } catch (MealKitExistException | GeneralException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "An unexpected error occured.",ex.getMessage()));
        }
    }
    
    public String extend(){
        inputStringRecipe = "";
        return null;
        
    }
    
    public boolean checkIfPriceIsMoreThanZero() {
        if(newMealKit.getPrice() <= 0) {
            return true;
        }
        else {
            return false;
        }
    }
    
    
    public void addNewMealKitMessage() {
        String summary = getNewMealKit().isIsAvailable() ? "Set to Unavailable" : "Set to Available";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }
    
    public List<String> retrieveAllTagNames() {
        List<TagEntity> tags = tagControllerLocal.retrieveAllTags();
        for (TagEntity tag: tags) {
            getTagNames().add(tag.getName());
        }
        return getTagNames();
    }
    public void createNew(){
        recipe.add(inputStringRecipe);
        inputStringRecipe = "";
    }
    public void handleFileUpload(FileUploadEvent event)
    {
        try
        {
            String newFilePath = System.getProperty("user.dir").replaceAll("config", "docroot") + System.getProperty("file.separator") + event.getFile().getFileName();
            newMealKit.setImagePath("http://localhost:8080/"+event.getFile().getFileName());  // need to change path

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputstream();

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }
            setUploadedOne(true);
            fileOutputStream.close();
            inputStream.close();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully!", ""));
        }
        catch(IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
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

    /**
     * @return the uploadedOne
     */
    public boolean isUploadedOne() {
        return uploadedOne;
    }

    /**
     * @param uploadedOne the uploadedOne to set
     */
    public void setUploadedOne(boolean uploadedOne) {
        this.uploadedOne = uploadedOne;
    }

    /**
     * @return the uploadedImage
     */
    public UploadedFile getUploadedImage() {
        return uploadedImage;
    }

    /**
     * @param uploadedImage the uploadedImage to set
     */
    public void setUploadedImage(UploadedFile uploadedImage) {
        this.uploadedImage = uploadedImage;
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
     * @return the recipe
     */
    public List<String> getRecipe() {
        return recipe;
    }

    /**
     * @param recipe the recipe to set
     */
    public void setRecipe(List<String> recipe) {
        System.err.println("***inside set recipe");
        this.recipe = recipe;
    }
    
}
