package com.easysplit.ess.transactions.asserters;

import com.easysplit.ess.groups.asserters.GroupsAsserter;
import com.easysplit.ess.groups.domain.models.Group;
import com.easysplit.ess.transactions.domain.models.Debt;
import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.users.asserters.UserAsserter;
import com.easysplit.shared.utils.EssUtils;
import org.assertj.core.api.AbstractAssert;
import org.junit.jupiter.api.Assertions;

import java.util.Currency;

public class TransactionsAsserter extends AbstractAssert<TransactionsAsserter, Transaction> {
    public TransactionsAsserter(Transaction actual) {
        super(actual, TransactionsAsserter.class);
    }

    public TransactionsAsserter assertTransaction(Transaction expected) {
        if (this.actual == null) {
            failWithMessage("Transaction cannot be empty");
        }

        if (expected.getId() != null) {
            String expectedId = expected.getId();
            String actualId = this.actual.getId();

            if (EssUtils.isNullOrEmpty(actualId)) {
                failWithMessage("Transaction's id cannot be null nor empty");
            }

            Assertions.assertEquals(expectedId, actualId, "Expected " + expectedId + " and actual "
                    + actualId + " ids do not match.");
        }

        if (expected.getName() != null) {
            String expectedName = expected.getName();
            String actualName = this.actual.getName();
            if (EssUtils.isNullOrEmpty(actualName)) {
                failWithMessage("Transaction's name cannot be null nor empty.");
            }

            Assertions.assertEquals(expectedName, actualName, "Expected " + expectedName + " and actual "
                    + actualName + " group names do not match.");
        }

        if (expected.getCurrency() != null) {
            Currency expectedCurrency = expected.getCurrency();
            Currency actualCurrency = this.actual.getCurrency();

            if (actualCurrency == null) {
                failWithMessage("Currency cannot be null.");
            }

            Assertions.assertEquals(expectedCurrency, actualCurrency, "The expected " + expectedCurrency
                    + " and the actual " + actualCurrency + " currencies do not match.");
        }

        if (expected.getDebt() != null) {
            Debt expectedDebt = expected.getDebt();
            Debt actualDebt = this.actual.getDebt();

            if (actualDebt == null) {
                failWithMessage("Debt cannot be empty");
            }

            new DebtsAsserter(actualDebt).assertDebt(expectedDebt);
        }

        if (expected.getGroup() != null) {
            Group expectedGroup = expected.getGroup();
            Group actualGroup = this.actual.getGroup();

            if (actualGroup == null) {
                failWithMessage("Group cannot be empty");
            }

            new GroupsAsserter(actualGroup).assertGroup(expectedGroup);
        }

        if (expected.getCreditor() != null) {
            User expectedCreditor = expected.getCreditor();
            User actualCreditor = this.actual.getCreditor();

            if (actualCreditor == null) {
                failWithMessage("Creditor cannot be empty");
            }

            new UserAsserter(actualCreditor).assertUser(expectedCreditor);
        }

        if (expected.getDebtor() != null) {
            User expectedDebtor = expected.getDebtor();
            User actualDebtor = this.actual.getDebtor();

            if (actualDebtor == null) {
                failWithMessage("Debtor cannot be empty");
            }

            new UserAsserter(actualDebtor).assertUser(expectedDebtor);
        }

        return this;
    }
}
