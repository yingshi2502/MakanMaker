/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import util.enumeration.OrderStatusEnum;

/**
 *
 * @author Ismahfaris
 */
@Entity
public class OrderEntity implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CustomerEntity customer;
    
    @Column(nullable = false)
    private Double totalAmount;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private MealKitEntity mealKit;
    
    private Integer quantity;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date deliveryDate;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date purchasingDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatusEnum orderStatus;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AddressEntity address;
    
    @OneToOne(mappedBy="order")
    private TransactionEntity transaction;
    
    private String extraRequest;
    
    private Double shippingFee;
   
    private boolean isReviewed;
    
    public OrderEntity(){
        
    }

    public OrderEntity(Double totalAmount, Integer quantity, Date deliveryDate, Date purchasingDate, OrderStatusEnum orderStatus, String extraRequest, Double shippingFee) {
        this.totalAmount = totalAmount;
        this.quantity = quantity;
        this.deliveryDate = deliveryDate;
        this.purchasingDate = purchasingDate;
        this.orderStatus = orderStatus;
        this.extraRequest = extraRequest;
        this.shippingFee = shippingFee;
    }
    
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getOrderId() != null ? getOrderId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the orderId fields are not set
        if (!(object instanceof OrderEntity)) {
            return false;
        }
        OrderEntity other = (OrderEntity) object;
        if ((this.getOrderId() == null && other.getOrderId() != null) || (this.getOrderId() != null && !this.orderId.equals(other.orderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OrderEntity[ id=" + getOrderId() + " ]";
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
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
     * @return the totalAmount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }
    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(List<Integer> quantity) {
        this.setQuantity(quantity);
    }


    /**
     * @return the purchasingDate
     */
    public Date getPurchasingDate() {
        return purchasingDate;
    }

    /**
     * @param purchasingDate the purchasingDate to set
     */
    public void setPurchasingDate(Date purchasingDate) {
        this.purchasingDate = purchasingDate;
    }

    /**
     * @return the orderStatus
     */
    public OrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    /**
     * @param orderStatus the orderStatus to set
     */
    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * @return the extraRequest
     */
    public String getExtraRequest() {
        return extraRequest;
    }

    /**
     * @param extraRequest the extraRequest to set
     */
    public void setExtraRequest(String extraRequest) {
        this.extraRequest = extraRequest;
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
     * @return the mealKit
     */
    public MealKitEntity getMealKit() {
        return mealKit;
    }

    /**
     * @param mealKit the mealKit to set
     */
    public void setMealKit(MealKitEntity mealKit) {
        this.mealKit = mealKit;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate the deliveryDate to set
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * @return the transaction
     */
    public TransactionEntity getTransaction() {
        return transaction;
    }

    /**
     * @param transaction the transaction to set
     */
    public void setTransaction(TransactionEntity transaction) {
        this.transaction = transaction;
    }

    /**
     * @return the shippingFee
     */
    public Double getShippingFee() {
        return shippingFee;
    }

    /**
     * @param shippingFee the shippingFee to set
     */
    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    /**
     * @return the isReviewed
     */
    public Boolean getIsReviewed() {
        return isReviewed;
    }

    /**
     * @param isReviewed the isReviewed to set
     */
    public void setIsReviewed(Boolean isReviewed) {
        this.isReviewed = isReviewed;
    }
    
}
