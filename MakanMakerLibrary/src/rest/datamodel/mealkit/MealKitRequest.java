/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.datamodel.mealkit;

import entity.MealKitEntity;

/**
 *
 * @author Ismahfaris
 */
public class MealKitRequest {
    private Long customerId;
    private MealKitEntity mealKit;

    public MealKitRequest() {
    }

    public MealKitRequest(Long customerId, MealKitEntity mealKit) {
        this.customerId = customerId;
        this.mealKit = mealKit;
    }

    /**
     * @return the customerId
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the mealKit
     */
    public MealKitEntity getMealKit() {
        return mealKit;
    }

    /**
     * @param mealKit the mealKit to set
     */
    public void setMealKit(MealKitEntity mealKit) {
        this.mealKit = mealKit;
    }
    
       
}
