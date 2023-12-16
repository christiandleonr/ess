package com.easysplit.shared.domain.models;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Class that represent the money used for a transaction
 */
public class Money {
    private BigDecimal amount;
    private Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return this.amount + " " + this.currency.getCurrencyCode();
    }
}
