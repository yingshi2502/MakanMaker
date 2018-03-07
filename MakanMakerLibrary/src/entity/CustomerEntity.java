/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Ismahfaris
 */
@Entity
public class CustomerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    
    @Column(unique = true, nullable = false)
    private String userName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password; 
    private List<String> socialMediaAccount;
    
    @OneToMany(mappedBy = "customer")
    private List<OrderEntity> orderHistory;
    @OneToOne
    private AddressEntity address;
    @OneToOne
    private CreditCardEntity creditCard;
    @ManyToMany
    private List<MealKitEntity> wishList = new ArrayList<MealKitEntity>();

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public CustomerEntity(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getCustomerId() != null ? getCustomerId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof CustomerEntity)) {
            return false;
        }
        CustomerEntity other = (CustomerEntity) object;
        if ((this.getCustomerId() == null && other.getCustomerId() != null) || (this.getCustomerId() != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerEntity[ id=" + getCustomerId() + " ]";
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
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
    public List<OrderEntity> getOrderHistory() {
        return orderHistory;
    }

    /**
     * @param orderHistory the orderHistory to set
     */
    public void setOrderHistory(List<OrderEntity> orderHistory) {
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
