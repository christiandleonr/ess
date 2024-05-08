package com.easysplit.ess.transactions.asserters;

import com.easysplit.ess.transactions.domain.models.Debt;
import com.easysplit.shared.domain.models.Money;
import org.assertj.core.api.AbstractAssert;
import org.junit.jupiter.api.Assertions;

public class DebtsAsserter extends AbstractAssert<DebtsAsserter, Debt> {
    public DebtsAsserter(Debt actual) {
        super(actual, DebtsAsserter.class);
    }

    public DebtsAsserter assertDebt(Debt expected) {
        if (this.actual == null) {
            failWithMessage("Debt cannot be empty");
        }

        if (expected.getTotalAmount() != null) {
            Money expectedTotalAmount = expected.getTotalAmount();
            Money actualTotalAmount = this.actual.getTotalAmount();

            if (actualTotalAmount == null) {
                failWithMessage("Total amount cannot be empty");
            }

            Assertions.assertEquals(expectedTotalAmount, actualTotalAmount, "Expected " + expectedTotalAmount + " and actual "
                    + actualTotalAmount + " total amount do not match.");
        }

        if (expected.getDebt() != null) {
            Money expectedDebt = expected.getTotalAmount();
            Money actualDebt = this.actual.getTotalAmount();

            if (actualDebt == null) {
                failWithMessage("Debt cannot be empty");
            }

            Assertions.assertEquals(expectedDebt, actualDebt, "Expected " + expectedDebt + " and actual "
                    + actualDebt + " debt do not match.");
        }

        if (expected.isDebtSettled() != null) {
            Boolean expectedIsDebtSettled = expected.isDebtSettled();
            Boolean actualIsDebtSettled = this.actual.isDebtSettled();

            if (actualIsDebtSettled == null) {
                failWithMessage("Debt settled value cannot be empty");
            }

            Assertions.assertEquals(expectedIsDebtSettled, actualIsDebtSettled, "Expected " + expectedIsDebtSettled + " and actual "
                    + actualIsDebtSettled + " debt settled variable do not match.");
        }

        if (expected.getRevision() != null) {
            Integer expectedRevision = expected.getRevision();
            Integer actualRevision = this.actual.getRevision();

            if (actualRevision == null) {
                failWithMessage("Revision cannot be empty");
            }

            Assertions.assertEquals(expectedRevision, actualRevision, "Expected " + expectedRevision + " and actual "
                    + actualRevision + " revisions do not match.");
        }

        return this;
    }
}
