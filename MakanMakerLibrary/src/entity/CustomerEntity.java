/*
 * ADD FULLNAME & MOBILE
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

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
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private String mobile;
      
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password; 
    
    @OneToMany(mappedBy = "customer")
    private List<OrderEntity> orderHistory;
    @OneToMany(mappedBy = "customer")
    private List<AddressEntity> addresses;
    
    private List<Long> wishList; //// stores MealKitID

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateOfBirth;
    
    private Integer gender; //0:male, 1:female
            
    @OneToMany(mappedBy = "customer")
    private List<TransactionEntity> transactions;
    
    @OneToOne(mappedBy = "customer")
    private ShoppingCartEntity shoppingCart;
    
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public CustomerEntity() {
        wishList = new ArrayList<>();
    }

    public CustomerEntity(String userName, String fullName, String mobile, String email, String password, Date dob, Integer gender) {
        this.userName = userName;
        this.fullName = fullName;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dob;
        this.gender = gender;
        wishList = new ArrayList<>();
    }


    public String getFullName() {
        return fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    /**
     * @return the dateOfBirth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
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
//     */
//    public List<String> getSocialMediaAccount() {
//        return socialMediaAccount;
//    }
//
//    /**
//     * @param socialMediaAccount the socialMediaAccount to set
//     */
//    public void setSocialMediaAccount(List<String> socialMediaAccount) {
//        this.socialMediaAccount = socialMediaAccount;
//    }

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
     * @return the wishList
     */
    public List<Long> getWishList() {
        return wishList;
    }

    /**
     * @param wishList the wishList to set
     */
    public void setWishList(List<Long> wishList) {
        this.wishList = wishList;
    }

    /**
     * @return the transactions
     */
    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    /**
     * @param transactions the transactions to set
     */
    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
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

    /**
     * @return the shoppingCart
     */
    public ShoppingCartEntity getShoppingCart() {
        return shoppingCart;
    }

    /**
     * @param shoppingCart the shoppingCart to set
     */
    public void setShoppingCart(ShoppingCartEntity shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
    
}
