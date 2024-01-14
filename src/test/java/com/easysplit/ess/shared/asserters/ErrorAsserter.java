package com.easysplit.ess.shared.asserters;

import com.easysplit.shared.domain.models.ErrorResponse;
import com.easysplit.shared.utils.EssUtils;
import org.assertj.core.api.AbstractAssert;

public class ErrorAsserter extends AbstractAssert<ErrorAsserter, ErrorResponse> {
    public ErrorAsserter(ErrorResponse actual) {
        super(actual, ErrorAsserter.class);
    }

    public ErrorAsserter assertError(ErrorResponse expected) {
        if(expected.getErrorTitle() != null) {
            String actualErrorTitle = this.actual.getErrorTitle();
            String expectedErrorTitle = expected.getErrorTitle();
            if (EssUtils.isNullOrEmpty(actualErrorTitle)) {
                failWithMessage("Error title cannot be empty");
            }

            if (!expectedErrorTitle.equals(actualErrorTitle)) {
                failWithMessage("Expected - " + expectedErrorTitle + " and actual - "
                        + actualErrorTitle + " error titles do not match.");
            }
        }

        if(expected.getErrorMessage() != null) {
            String actualErrorMessage = this.actual.getErrorMessage();
            String expectedErrorMessage = expected.getErrorMessage();
            if (EssUtils.isNullOrEmpty(actualErrorMessage)) {
                failWithMessage("Error title cannot be empty");
            }

            if (!expectedErrorMessage.equals(actualErrorMessage)) {
                failWithMessage("Expected - " + expectedErrorMessage + " and actual - "
                        + actualErrorMessage + " error titles do not match.");
            }
        }

        return this;
    }
}
