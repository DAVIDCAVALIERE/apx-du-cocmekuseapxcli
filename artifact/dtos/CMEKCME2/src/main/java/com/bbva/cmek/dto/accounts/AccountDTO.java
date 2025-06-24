package com.bbva.cmek.dto.accounts;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The AccountDTO class...
 */
public class AccountDTO implements Serializable {
    private static final long serialVersionUID = 2931699728946643245L;

    /* Attributes section for the DTO */

    /**
     * The identifier attribute
     */
    private String id;

    /**
     * The status attribute
     */
    private String status;

    /**
     * The balance attribute
     */
    private long balance;

    /**
     * The account type attribute
     */
    private String typeAccount;

    /**
     * The holder attribute
     */
    private String titular;

    private String errorCode;
    private String errorMessage;

    /**
     * Get the identifier attribute
     */
    public String getId() {
        return id;
    }

    /**
     * Set the identifier attribute
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the status attribute
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status attribute
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the balance attribute
     */
    public long getBalance() {
        return balance;
    }

    /**
     * Set the balance attribute
     */
    public void setBalance(long balance) {
        this.balance = balance;
    }

    /**
     * Get the account type attribute
     */
    public String getTypeAccount() {
        return typeAccount;
    }

    /**
     * Set the account type attribute
     */
    public void setTypeAccount(String type) {
        this.typeAccount = typeAccount;
    }

    /**
     * Get the holder attribute
     */
    public String getTitular() {
        return titular;
    }

    /**
     * Set the holder attribute
     */
    public void setTitular(String titular) {
        this.titular = titular;
    }

    //


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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
        final AccountDTO rhs = (AccountDTO) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj))
                .append(id, rhs.id)
                .append(status, rhs.status)
                .append(balance, rhs.balance)
                .append(typeAccount, rhs.typeAccount)
                .append(titular, rhs.titular)
                .isEquals();
    }

    /**
     * Returns a hash code value for the object.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.id)
                .append(this.status)
                .append(this.balance)
                .append(this.typeAccount)
                .append(this.titular)
                .toHashCode();
    }

    /**
     * Returns a string representation of the object.
     * It is important to OBFUSCATE the attributes that are sensitive to show in the representation.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("identifier", id)
                .append("status", status)
                .append("balance", balance)
                .append("account type", typeAccount)
                .append("holder", titular)
                .toString();
    }
}
