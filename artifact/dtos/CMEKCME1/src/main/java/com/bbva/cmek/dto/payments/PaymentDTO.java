package com.bbva.cmek.dto.payments;

import java.io.Serializable;
import java.util.Date;

import com.bbva.cmek.dto.accounts.AccountDTO;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The PaymentDTO class...
 */
public class PaymentDTO implements Serializable {
    private static final long serialVersionUID = 2931699728946643245L;

    /* Attributes section for the DTO */

    /**
     * The account data attribute
     */
    private AccountDTO account;

    /**
     * The bill data attribute
     */
    private BillDTO bill;

    /**
     * The payment date/time attribute
     */
    private Date operationDateTime;

    /**
     * The unique payment identifier attribute
     */
    private String id;


    /**
     * Get the account data attribute
     */
    public AccountDTO getAccount() {
        return account;
    }

    /**
     * Set the account data attribute
     */
    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    /**
     * Get the bill data attribute
     */
    public BillDTO getBill() {
        return bill;
    }

    /**
     * Set the bill data attribute
     */
    public void setBill(BillDTO bill) {
        this.bill = bill;
    }

    /**
     * Get the operation DateTime attribute
     */
    public Date getOperationDateTime() {
        return this.operationDateTime;
    }

    /**
     * Set the payment date/time attribute
     */
    public void setOperationDateTime(final Date operationDateTime) {
        this.operationDateTime = operationDateTime;
    }

    /**
     * Get the payment identifier attribute
     */
    public String getId() {
        return this.id;
    }

    /**
     * Set the payment identifier attribute
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        final PaymentDTO rhs = (PaymentDTO) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj))
                .append(account, rhs.account)
                .append(bill, rhs.bill)
                .append(operationDateTime, rhs.operationDateTime)
                .append(id, rhs.id)
                .isEquals();
    }

    /**
     * Returns a hash code value for the object.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.account)
                .append(this.bill)
                .append(this.operationDateTime)
                .append(this.id)
                .toHashCode();
    }

    /**
     * Returns a string representation of the object.
     * It is important to OBFUSCATE the attributes that are sensitive to show in the representation.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("account data", account)
                .append("bill data", bill)
                .append("payment date time", operationDateTime)
                .append("id", id)
                .toString();
    }
}
