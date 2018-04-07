
package rest.datamodel.customer;

import entity.AddressEntity;

/**
 *
 * @author yingshi
 */
public class AddressResponse {
    private String message;
    private boolean result;
    private AddressEntity address;

    public AddressResponse() {
    }

    public AddressResponse(String message, boolean result, AddressEntity address) {
        this.message = message;
        this.result = result;
        this.address = address;
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
