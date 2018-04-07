
package rest.datamodel.customer;

import entity.CustomerEntity;

/**
 *
 * @author yingshi
 */
public class SignUpResponse {
    private String message;
    private boolean result;
    private CustomerEntity customer;

    public SignUpResponse() {
    }

    public SignUpResponse(String message, boolean result, CustomerEntity customer) {
        this.message = message;
        this.result = result;
        this.customer = customer;
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
     * @return the customer
     */
    public CustomerEntity getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }
    
    
}
