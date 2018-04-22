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
public class MealKitResponse {
    private String message;
    private boolean result;
    private MealKitEntity mealkit;

    public MealKitResponse() {
    }

    public MealKitResponse(String message, boolean result, MealKitEntity mealkit) {
        this.message = message;
        this.result = result;
        this.mealkit = mealkit;
    }

    
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the result
     */
    public boolean isResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(boolean result) {
        this.result = result;
    }

    /**
     * @return the mealkit
     */
    public MealKitEntity getMealKit() {
        return mealkit;
    }

    /**
     * @param mealkit the mealkit to set
     */
    public void setMealKit(MealKitEntity mealkit) {
        this.mealkit = mealkit;
    }
    
    
}