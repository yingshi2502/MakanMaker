
package rest.datamodel.customer;

import entity.OrderEntity;
import java.util.List;

/**
 *
 * @author yingshi
 */
public class CreateOrdersRequest {
    private List<OrderEntity> orders;
    private Long customerId;
    private List<Long> mealKitIds;
    private Long addressId;

    public CreateOrdersRequest() {
    }

    /**
     * @return the orders
     */
    public List<OrderEntity> getOrders() {
        return orders;
    }

    /**
     * @param orders the orders to set
     */
    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
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
     * @return the mealKitIds
     */
    public List<Long> getMealKitIds() {
        return mealKitIds;
    }

    /**
     * @param mealKitIds the mealKitIds to set
     */
    public void setMealKitIds(List<Long> mealKitIds) {
        this.mealKitIds = mealKitIds;
    }

    /**
     * @return the addressId
     */
    public Long getAddressId() {
        return addressId;
    }

    /**
     * @param addressId the addressId to set
     */
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
    
    
}
