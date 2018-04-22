package util.helperClass;

import entity.MealKitEntity;

/**
 *
 * @author yingshi
 */
public class CartItemWrapper {

    private int quantity;
    private MealKitEntity mk;
    private String extraRequest;
    
    public CartItemWrapper() {
        this.extraRequest = "";
    }

    public CartItemWrapper(int q, MealKitEntity mk) {
        this.quantity = q;
        this.mk = mk;
        this.extraRequest = "";
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the mk
     */
    public MealKitEntity getMk() {
        return mk;
    }

    /**
     * @param mk the mk to set
     */
    public void setMk(MealKitEntity mk) {
        this.mk = mk;
    }

    /**
     * @return the extraRequest
     */
    public String getExtraRequest() {
        return extraRequest;
    }

    /**
     * @param extraRequest the extraRequest to set
     */
    public void setExtraRequest(String extraRequest) {
        this.extraRequest = extraRequest;
    }
}
