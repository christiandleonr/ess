package com.easysplit.ess.iam.asserters;

import com.easysplit.ess.iam.domain.models.Token;
import com.easysplit.shared.utils.EssUtils;
import org.assertj.core.api.AbstractAssert;

public class TokenAsserter extends AbstractAssert<TokenAsserter, Token> {
    public TokenAsserter(Token actual) {
        super(actual, TokenAsserter.class);
    }

    public TokenAsserter assertToken() {
        if (this.actual == null) {
            failWithMessage("Token cannot be empty");
        }

        if (EssUtils.isNullOrEmpty(this.actual.getToken())) {
            failWithMessage("Access token cannot be empty");
        }

        if (EssUtils.isNullOrEmpty(this.actual.getRefreshToken())) {
            failWithMessage("Refresh token cannot be empty");
        }

        return this;
    }
}
