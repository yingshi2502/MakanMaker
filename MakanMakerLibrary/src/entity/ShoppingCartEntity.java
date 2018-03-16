
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author yingshi
 */
@Entity
public class ShoppingCartEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long shoppingCartId;

    @OneToOne
    private CustomerEntity customer;
    
    private List<Long> mealKits;//mealKitId
    
    private List<Integer> quantity;
    
    private Double totalAmount;

    public ShoppingCartEntity() {
        totalAmount = Double.valueOf(0);
    }
    
    public Long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (shoppingCartId != null ? shoppingCartId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the shoppingCartId fields are not set
        if (!(object instanceof ShoppingCartEntity)) {
            return false;
        }
        ShoppingCartEntity other = (ShoppingCartEntity) object;
        if ((this.shoppingCartId == null && other.shoppingCartId != null) || (this.shoppingCartId != null && !this.shoppingCartId.equals(other.shoppingCartId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ShoppingCartEntity[ id=" + shoppingCartId + " ]";
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

    /**
     * @return the mealKits
     */
    public List<Long> getMealKits() {
        return mealKits;
    }

    /**
     * @param mealKits the mealKits to set
     */
    public void setMealKits(List<Long> mealKits) {
        this.mealKits = mealKits;
    }

    /**
     * @return the quantity
     */
    public List<Integer> getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(List<Integer> quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the totalAmount
     */
    public Double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
