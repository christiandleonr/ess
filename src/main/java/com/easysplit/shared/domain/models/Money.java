package com.easysplit.shared.domain.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Currency;

/**
 * Class that represent the money used for a transaction
 */
@JsonSerialize(using = MoneySerializer.class)
@JsonDeserialize(using = MoneyDeserializer.class)
public class Money {
    private BigDecimal amount;

    public Money() {}

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public Money(int amount) {
        this.amount = BigDecimal.valueOf(amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return this.amount + "";
    }

    /**
     * Divides the amount among a given number of people
     *
     * @param numberOfPeople total number of people to divide the amount between
     * @return amounts that belongs to each person, divided equally
     */
    public BigDecimal[] divideEqually(int numberOfPeople) {
        BigDecimal[] amounts = new BigDecimal[numberOfPeople];

        BigDecimal baseAmountPerPerson = this.amount.divide(new BigDecimal(numberOfPeople), 2, RoundingMode.DOWN);
        BigDecimal remainder = this.amount.subtract(baseAmountPerPerson.multiply(new BigDecimal(numberOfPeople)));

        Arrays.fill(amounts, baseAmountPerPerson);
        for (int i = 0; i < remainder.multiply(new BigDecimal("100")).intValue(); i++) {
            amounts[i] = amounts[i].add(new BigDecimal("0.01"));
        }

        return amounts;
    }
}
