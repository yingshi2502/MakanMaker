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
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import util.exception.EmptyListException;

/**
 *
 * @author Ismahfaris
 */
@Named(value = "mealKitManagedBean")
@ViewScoped
public class mealKitManagedBean implements Serializable {

    @EJB(name = "TagControllerLocal")
    private TagControllerLocal tagControllerLocal;
    @EJB
    private MealKitControllerLocal mealKitControllerLocal;
    private List<MealKitEntity> mealKits;
    private boolean noMealKit;
    private MealKitEntity currMealKit;
    private String searchKeywords;
    private List<TagEntity> tags;
    private List<String> selectedTags;

    @PostConstruct
    public void postConstruct() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String keywords = request.getParameter("keywords");
        setMealKits(mealKitControllerLocal.retrieveAvailableMealKits(false));
        Random r = new Random();
        int choice = r.nextInt(mealKits.size() - 1);
        chefMealKit = mealKits.get(choice);
        choice = r.nextInt(mealKits.size() - 1);
        todayMealKit = mealKits.get(choice);
        
        if (keywords != null) {
            System.err.println("***Keywords retrieved from HTTP: " + keywords);
            setMealKits(mealKitControllerLocal.searchMealKits(keywords,false));
            System.err.println("***mks retrieved from HTTP: " + mealKits.size());
        }

        tags = tagControllerLocal.retrieveAllTags();
        
        
        
        System.err.println("******Tags： " + tags.size());
    }

    public mealKitManagedBean() {
        mealKits = new ArrayList<MealKitEntity>();
        tags = new ArrayList<>();
        selectedTags = new ArrayList<>();
    }

    public void onSelectTag() {
        for (String s : selectedTags) {
            System.err.println("***" + s);
        }

        mealKits = tagControllerLocal.retrieveMealKitsByTags(selectedTags);
    }

    private MealKitEntity chefMealKit;
    private MealKitEntity todayMealKit;
    

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
     * @return the noMealKit
     */
    public boolean noMealKit() {
        return isNoMealKit();
    }

    /**
     * @param noMealKit the noMealKit to set
     */
    public void setNoMealKit(boolean noMealKit) {
        this.noMealKit = noMealKit;
    }

    /**
     * @return the currMealKit
     */
    public MealKitEntity getCurrMealKit() {
        return currMealKit;
    }

    /**
     * @param currMealKit the currMealKit to set
     */
    public void setCurrMealKit(MealKitEntity currMealKit) {
        this.currMealKit = currMealKit;
    }

    public void viewMealKitDetails(ActionEvent event) throws IOException {
        Long mealKitIdToView = (Long) event.getComponent().getAttributes().get("mealKitId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("mealKitIdToView", mealKitIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewMealKitDetails.xhtml" + "?id=" + mealKitIdToView);
    }

    /**
     * @return the noMealKit
     */
    public boolean isNoMealKit() {
        return noMealKit;
    }

    /**
     * @return the searchKeywords
     */
    public String getSearchKeywords() {
        return searchKeywords;
    }

    /**
     * @param searchKeywords the searchKeywords to set
     */
    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
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
     * @return the selectedTags
     */
    public List<String> getSelectedTags() {
        return selectedTags;
    }

    /**
     * @param selectedTags the selectedTags to set
     */
    public void setSelectedTags(List<String> selectedTags) {
        this.selectedTags = selectedTags;
    }

    /**
     * @return the chefMealKit
     */
    public MealKitEntity getChefMealKit() {
        return chefMealKit;
    }

    /**
     * @param chefMealKit the chefMealKit to set
     */
    public void setChefMealKit(MealKitEntity chefMealKit) {
        this.chefMealKit = chefMealKit;
    }

    /**
     * @return the todayMealKit
     */
    public MealKitEntity getTodayMealKit() {
        return todayMealKit;
    }

    /**
     * @param todayMealKit the todayMealKit to set
     */
    public void setTodayMealKit(MealKitEntity todayMealKit) {
        this.todayMealKit = todayMealKit;
    }
}
