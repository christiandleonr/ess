package com.easysplit.ess.iam.utils;

import com.easysplit.ess.iam.domain.models.Auth;
import com.easysplit.ess.iam.domain.models.RefreshToken;
import com.easysplit.ess.iam.domain.models.Token;
import com.easysplit.ess.shared.utils.TestRESTHelper;
import com.easysplit.shared.domain.models.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class TestIamHelper {
    private final String IAM_PATH = "/api/iam";
    private final String IAM_REFRESH_TOKEN_PATH = IAM_PATH + "/refreshToken";
    @Autowired
    private TestRESTHelper testRestHelper;
    @Autowired
    private MessageSource messageSource;

    public Token authenticate(Auth auth, HttpStatus httpStatus) {
        return (Token) testRestHelper.postNoAuth(IAM_PATH, auth, Token.class, httpStatus);
    }

    public ErrorResponse failAuthenticate(Auth auth, HttpStatus httpStatus) {
        return testRestHelper.failPostNoAuth(IAM_PATH, auth, httpStatus);
    }

    public Token refreshToken(RefreshToken refreshToken, HttpStatus httpStatus) {
        return (Token) testRestHelper.postNoAuth(IAM_REFRESH_TOKEN_PATH, refreshToken, Token.class, httpStatus);
    }

    public ErrorResponse failRefreshToken(RefreshToken refreshToken, HttpStatus httpStatus) {
        return testRestHelper.failPostNoAuth(IAM_REFRESH_TOKEN_PATH, refreshToken, httpStatus);
    }

    public String getMessage(String errorKey, Object[] args) {
        return messageSource.getMessage(errorKey, args, null);
    }
}
