/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.criteria.Order;

/**
 *
 * @author Ismahfaris
 */
@Entity
public class CustomerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Id
    private Long customerId;
    private String userName;
    private String email;
    private String password;
    private List<String> socialMediaAccount;
    
    @OneToMany(mappedBy = "customer")
    private List<Order> orderHistory;
    @OneToOne
    private AddressEntity address;
    @OneToOne
    private CreditCardEntity creditCard;
    @ManyToMany
    private List<MealKitEntity> wishList = new ArrayList<MealKitEntity>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerEntity)) {
            return false;
        }
        CustomerEntity other = (CustomerEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerEntity[ id=" + getId() + " ]";
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
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
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the socialMediaAccount
     */
    public List<String> getSocialMediaAccount() {
        return socialMediaAccount;
    }

    /**
     * @param socialMediaAccount the socialMediaAccount to set
     */
    public void setSocialMediaAccount(List<String> socialMediaAccount) {
        this.socialMediaAccount = socialMediaAccount;
    }

    /**
     * @return the orderHistory
     */
    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    /**
     * @param orderHistory the orderHistory to set
     */
    public void setOrderHistory(List<Order> orderHistory) {
        this.orderHistory = orderHistory;
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

    /**
     * @return the creditCard
     */
    public CreditCardEntity getCreditCard() {
        return creditCard;
    }

    /**
     * @param creditCard the creditCard to set
     */
    public void setCreditCard(CreditCardEntity creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * @return the wishList
     */
    public List<MealKitEntity> getWishList() {
        return wishList;
    }

    /**
     * @param wishList the wishList to set
     */
    public void setWishList(List<MealKitEntity> wishList) {
        this.wishList = wishList;
    }
    
}
