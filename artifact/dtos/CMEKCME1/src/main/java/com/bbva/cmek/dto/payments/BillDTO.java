package com.bbva.cmek.dto.payments;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The BillDTO class...
 */
public class BillDTO implements Serializable {
    private static final long serialVersionUID = 2931699728946643245L;

    /* Attributes section for the DTO */

    /**
     * The number attribute
     */
    private String number;

    /**
     * The value attribute
     */
    private long value;

    /**
     * The bill Status attribute
     */
    private String status;

    /**
     * The supplier name attribute
     */
    private String supplier;

    /**
     * Get the number attribute
     */
    public String getNumber() {
        return this.number;
    }

    /**
     * Set the number attribute
     */
    public void setNumber(final String number) {
        this.number = number;
    }

    /**
     * Get the value attribute
     */
    public long getValue() {
        return this.value;
    }

    /**
     * Set the value attribute
     */
    public void setValue(long value) {
        this.value = value;
    }

    /**
     * Get the bill status attribute
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the bill status attribute
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the supplier name attribute
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * Set the supplier name attribute
     */
    public void setSupplier(String supplierName) {
        this.supplier = supplier;
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
        final BillDTO rhs = (BillDTO) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj))
                .append(number, rhs.number)
                .append(value, rhs.value)
                .append(status, rhs.status)
                .append(supplier, rhs.supplier)
                .isEquals();
    }

    /**
     * Returns a hash code value for the object.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.number)
                .append(this.value)
                .append(this.status)
                .append(this.supplier)
                .toHashCode();
    }

    /**
     * Returns a string representation of the object.
     * It is important to OBFUSCATE the attributes that are sensitive to show in the representation.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("number", number)
                .append("value", value)
                .append("bill status", status)
                .append("supplier name", supplier)
                .toString();
    }
}
