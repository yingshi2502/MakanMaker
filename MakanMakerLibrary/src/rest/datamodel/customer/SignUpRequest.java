
package rest.datamodel.customer;

import entity.CustomerEntity;

/**
 *
 * @author yingshi
 */
public class SignUpRequest {
    private CustomerEntity customer;

    public SignUpRequest(CustomerEntity customer) {
        this.customer = customer;
    }

    public SignUpRequest() {
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
