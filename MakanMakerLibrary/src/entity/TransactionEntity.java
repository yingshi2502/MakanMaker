
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.enumeration.PaymentTypeEnum;
import util.enumeration.TransactionTypeEnum;

/**
 *
 * @author yingshi
 */
@Entity
public class TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    
    private Double amount;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = false)
    private CustomerEntity customer;
    
    //not sure yet
    private Long orderId;
    
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum transactionType;
    
    @Enumerated(EnumType.STRING)
    private PaymentTypeEnum paymentType;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDateTime;

    public TransactionEntity() {
    }

    public TransactionEntity(Double amount, Long orderId, TransactionTypeEnum transactionType, PaymentTypeEnum paymentType, Date transactionDateTime) {
        this.amount = amount;
        this.orderId = orderId;
        this.transactionType = transactionType;
        this.paymentType = paymentType;
        this.transactionDateTime = transactionDateTime;
    }
    
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionId != null ? transactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transactionId fields are not set
        if (!(object instanceof TransactionEntity)) {
            return false;
        }
        TransactionEntity other = (TransactionEntity) object;
        if ((this.transactionId == null && other.transactionId != null) || (this.transactionId != null && !this.transactionId.equals(other.transactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransactionEntity[ id=" + transactionId + " ]";
    }
    
        /**
     * @return the orderId
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

     /**
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
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
     * @return the transactionType
     */
    public TransactionTypeEnum getTransactionType() {
        return transactionType;
    }

    /**
     * @param transactionType the transactionType to set
     */
    public void setTransactionType(TransactionTypeEnum transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * @return the transactionDateTime
     */
    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    /**
     * @param transactionDateTime the transactionDateTime to set
     */
    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    /**
     * @return the paymentType
     */
    public PaymentTypeEnum getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(PaymentTypeEnum paymentType) {
        this.paymentType = paymentType;
    }

}
