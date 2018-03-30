/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MealKitControllerLocal;
import entity.MealKitEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import util.exception.EmptyListException;

/**
 *
 * @author Ismahfaris
 */
@Named(value = "mealKitManagedBean")
@Dependent
public class mealKitManagedBean {

    @EJB
    private MealKitControllerLocal mealKitControllerLocal;
    private List<MealKitEntity> mealKits;
    private boolean noMealKit;
    private MealKitEntity currMealKit;
    
    
    @PostConstruct
    public void postConstruct() {       
        
        setMealKits(mealKitControllerLocal.retrieveAllMealKits());
        
    }
    
    
    public mealKitManagedBean() {
        mealKits = new ArrayList<MealKitEntity>();
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
     * @return the noMealKit
     */
    public boolean noMealKit() {
        return noMealKit;
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
    
}
