
package rest.datamodel.customer;

import java.util.List;
import util.helperClass.CartItemWrapper;

/**
 *
 * @author yingshi
 */
public class ShoppingCartResponse {
    
    private List<CartItemWrapper> items;
    private double subTotal;
    
    private boolean result;
    private String message;

    public ShoppingCartResponse() {
    }

    public ShoppingCartResponse(List<CartItemWrapper> items, double subTotal, boolean result, String message) {
        this.items = items;
        this.subTotal = subTotal;
        this.result = result;
        this.message = message;
    }
    

    /**
     * @return the items
     */
    public List<CartItemWrapper> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<CartItemWrapper> items) {
        this.items = items;
    }

    /**
     * @return the subTotal
     */
    public double getSubTotal() {
        return subTotal;
    }

    /**
     * @param subTotal the subTotal to set
     */
    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
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
    
    
}
