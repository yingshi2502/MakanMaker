/*
 * need to revise: Address:
Full Name, Street Address, Phone Number, Floor/Unit Number, PostCode
 */
package entity;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Ismahfaris
 */
@Entity
public class AddressEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    
    @Column(nullable = false, length=6)
    private String postalCode;
    
    @Column(nullable = false)
    private String streetAddress;
    @Column(nullable = false)
    private String floorUnit;
    
    @Column(nullable = false)
    private Boolean isDefaultShipping;
    
    @Column(nullable = false)
    private Boolean isDefaultBilling;
    
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private Boolean isDeleted; //customer wants to delete the address, don't directly delete but just change the BooleanValue
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CustomerEntity customer;
    
    private double shippingFee;
    
    @OneToMany(mappedBy = "address")
    private List<OrderEntity> orders;
    
    public AddressEntity(){
        this.isDeleted = false;
        setShippingFee();
    }

    public AddressEntity(String postalCode, String streetAddress, String floorUnit, Boolean isDefaultShipping, Boolean isDefaultBilling, String phoneNumber, String fullName) {
        this.postalCode = postalCode;
        this.streetAddress = streetAddress;
        this.floorUnit = floorUnit;
        this.isDefaultShipping = isDefaultShipping;
        this.isDefaultBilling = isDefaultBilling;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.isDeleted = false;
       setShippingFee();

    }

    private void setShippingFee(){
        Random r = new Random();
        int fD = r.nextInt(10);
        int fR = r.nextInt(10);
        double f = fD + fR * 0.1;
        setShippingFee(f);
    }
    
    public Boolean getIsDefaultBilling() {
        return isDefaultBilling;
    }

    public void setIsDefaultBilling(Boolean isDefaultBilling) {
        this.isDefaultBilling = isDefaultBilling;
    }


    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getAddressId() != null ? getAddressId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AddressEntity)) {
            return false;
        }
        AddressEntity other = (AddressEntity) object;
        if ((this.getAddressId() == null && other.getAddressId() != null) || (this.getAddressId() != null && !this.addressId.equals(other.addressId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AddressEntity[ id=" + getAddressId() + " ]";
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the streetAddress
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * @param streetAddress the streetAddress to set
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     * @return the floorUnit
     */
    public String getFloorUnit() {
        return floorUnit;
    }

    /**
     * @param floorUnit the floorUnit to set
     */
    public void setFloorUnit(String floorUnit) {
        this.floorUnit = floorUnit;
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
     * @return the orders
     */
    public List<OrderEntity> getOrders() {
        return orders;
    }

    /**
     * @param orders the orders to set
     */
    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    /**
     * @return the isDefaultShipping
     */
    public Boolean getIsDefaultShipping() {
        return isDefaultShipping;
    }

    /**
     * @param isDefaultShipping the isDefaultShipping to set
     */
    public void setIsDefaultShipping(Boolean isDefaultShipping) {
        this.isDefaultShipping = isDefaultShipping;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the isDeleted
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * @return the shippingFee
     */
    public double getShippingFee() {
        return shippingFee;
    }

    /**
     * @param shippingFee the shippingFee to set
     */
    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }
    
}
