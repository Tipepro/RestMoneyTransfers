package com.testTask.bank.domain;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@XmlRootElement
public class Account {
    private final Lock lock = new ReentrantLock();
    @XmlElement
    @NotNull
    @DecimalMin(value = "0")
    private Integer accountNumber;
    @XmlElement
    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amount;

    public Account() {
    }

    public Account(Integer accountNumber, BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void subtractAmount(BigDecimal subtrahend) {
        this.amount = amount.subtract(subtrahend);
    }

    public void addAmount(BigDecimal augend) {
        this.amount = amount.add(augend);
    }

    public Lock lock() {
        return lock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (accountNumber != null ? !accountNumber.equals(account.accountNumber) : account.accountNumber != null)
            return false;
        return amount != null ? amount.equals(account.amount) : account.amount == null;
    }

    @Override
    public int hashCode() {
        int result = accountNumber != null ? accountNumber.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", amount=" + amount +
                '}';
    }
}
