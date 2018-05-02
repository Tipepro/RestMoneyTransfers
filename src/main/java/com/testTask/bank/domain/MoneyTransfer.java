package com.testTask.bank.domain;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
public class MoneyTransfer {
    @XmlElement
    @NotNull
    @DecimalMin(value = "0")
    private Integer accountNumberFrom;
    @XmlElement
    @NotNull
    @DecimalMin(value = "0")
    private Integer accountNumberTo;
    @XmlElement
    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal amount;

    public MoneyTransfer() {
    }

    public MoneyTransfer(Integer accountNumberFrom, Integer accountNumberTo, BigDecimal amount) {
        this.accountNumberFrom = accountNumberFrom;
        this.accountNumberTo = accountNumberTo;
        this.amount = amount;
    }

    public Integer getAccountNumberFrom() {
        return accountNumberFrom;
    }

    public void setAccountNumberFrom(Integer accountNumberFrom) {
        this.accountNumberFrom = accountNumberFrom;
    }

    public Integer getAccountNumberTo() {
        return accountNumberTo;
    }

    public void setAccountNumberTo(Integer accountNumberTo) {
        this.accountNumberTo = accountNumberTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "MoneyTransfer{" +
                "accountNumberFrom='" + accountNumberFrom + '\'' +
                ", accountNumberTo='" + accountNumberTo + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
