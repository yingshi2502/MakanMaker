/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MealKitControllerLocal;
import entity.MealKitEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author yingshi
 */
@Named(value = "homePageManagedBean")
@ViewScoped
public class HomePageManagedBean implements Serializable{

    @EJB(name = "MealKitControllerLocal")
    private MealKitControllerLocal mealKitControllerLocal;

    private List<MealKitEntity> mealKits;
    /**
     * Creates a new instance of HomePageManagedBean
     */
    public HomePageManagedBean() {
        mealKits = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct(){
        List<MealKitEntity> mealKitsAll = mealKitControllerLocal.retrieveAvailableMealKits(false);
        System.err.println("****mealkit num"+mealKitsAll.size());
        int i=0;
        for (MealKitEntity m: mealKitsAll){
            if(i>mealKitsAll.size()-4){
                mealKits.add(m);
            }
            i++;
        }
         System.err.println("****mealkit num"+mealKits.size());
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
    
}
