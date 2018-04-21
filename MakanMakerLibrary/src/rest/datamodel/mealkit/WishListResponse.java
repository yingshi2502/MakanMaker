/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.datamodel.mealkit;

import entity.MealKitEntity;
import java.util.List;

/**
 *
 * @author Ismahfaris
 */
public class WishListResponse {
    private String message;
    private boolean result;
    private List<MealKitEntity> mealKits;

    public WishListResponse() {
    }

    public WishListResponse(String message, boolean result, List<MealKitEntity> mealKits) {
        this.message = message;
        this.result = result;
        this.mealKits = mealKits;
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
