
package rest.datamodel.customer;

import entity.MealKitEntity;
import java.util.List;

/**
 *
 * @author yingshi
 */
public class WishListResponse {
    private String message;
    private boolean result;
    private List<MealKitEntity> items;

    public WishListResponse() {
    }

    public WishListResponse(String message, boolean result, List<MealKitEntity> items) {
        this.message = message;
        this.result = result;
        this.items = items;
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
     * @return the items
     */
    public List<MealKitEntity> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<MealKitEntity> items) {
        this.items = items;
    }
    
    
}
