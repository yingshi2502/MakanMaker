
package rest.datamodel.customer;

import entity.AddressEntity;

/**
 *
 * @author yingshi
 */
public class AddressRequest {
    private Long customerId;
    private AddressEntity address;

    public AddressRequest() {
    }

    public AddressRequest(Long customerId, AddressEntity address) {
        this.customerId = customerId;
        this.address = address;
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
     * @return the address
     */
    public AddressEntity getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(AddressEntity address) {
        this.address = address;
    }
    
    
}
