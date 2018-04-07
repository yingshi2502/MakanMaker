
package rest.datamodel.customer;

import entity.AddressEntity;
import java.util.List;

/**
 *
 * @author yingshi
 */
public class AddressListResponse {
    private String message;
    private boolean result;
    private List<AddressEntity> addresses;

    public AddressListResponse() {
    }

    public AddressListResponse(String message, boolean result, List<AddressEntity> addresses) {
        this.message = message;
        this.result = result;
        this.addresses = addresses;
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
     * @return the addresses
     */
    public List<AddressEntity> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses the addresses to set
     */
    public void setAddresses(List<AddressEntity> addresses) {
        this.addresses = addresses;
    }
    
    
}
