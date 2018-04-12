
package util.helperClass;

import java.util.Date;

/**
 *
 * @author yingshi
 */
public class CreditCardWrapper {
    private String number;
    private String name;
    private Date expiry;
    private int cvv;

    public CreditCardWrapper() {
    }

    public CreditCardWrapper(String number, String name, Date expiry, int cvv) {
        this.number = number;
        this.name = name;
        this.expiry = expiry;
        this.cvv = cvv;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the expiry
     */
    public Date getExpiry() {
        return expiry;
    }

    /**
     * @param expiry the expiry to set
     */
    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    /**
     * @return the cvv
     */
    public int getCvv() {
        return cvv;
    }

    /**
     * @param cvv the cvv to set
     */
    public void setCvv(int cvv) {
        this.cvv = cvv;
    }
    
    
}
