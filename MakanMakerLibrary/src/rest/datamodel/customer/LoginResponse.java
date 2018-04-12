
package rest.datamodel.customer;

import entity.CustomerEntity;

/**
 *
 * @author yingshi
 */
public class LoginResponse {
    private boolean result;
    private String message;
    private CustomerEntity customer;

    public LoginResponse() {
    }

    public LoginResponse(boolean result, String message, CustomerEntity customer) {
        this.result = result;
        this.message = message;
        this.customer = customer;
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
