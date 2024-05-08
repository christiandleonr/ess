package com.easysplit.ess.iam;

import com.easysplit.ess.iam.domain.models.Auth;
import com.easysplit.ess.iam.domain.models.RefreshToken;
import com.easysplit.ess.iam.domain.models.Token;
import com.easysplit.ess.iam.utils.TestIamHelper;
import com.easysplit.shared.asserters.ErrorAsserter;
import com.easysplit.shared.utils.TestUtils;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.users.builders.UserBuilder;
import com.easysplit.ess.users.utils.TestUsersHelper;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.models.ErrorResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestIamNegative {
    private final long WAIT_FOR_TOKEN_EXPIRATION = (1000 * 60 * 5) + 5000; // 5 minutes + 5 seconds
    private final long WAIT_FOR_RT_EXPIRATION = (1000 * 60 * 10) + 5000; // 10 minutes + 5 seconds

    @Autowired
    private TestIamHelper testIamHelper;
    @Autowired
    private TestUsersHelper testUsersHelper;

    @Test
    public void testAuthenticationUserNotExist() {
        String username = "InvalidUser";
        Auth auth = new Auth(username, "InvalidPassword");
        ErrorResponse errorResponse = testIamHelper.failAuthenticate(auth, HttpStatus.NOT_FOUND);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                testIamHelper.getMessage(ErrorKeys.GET_USER_NOT_FOUND_TITLE, null),
                testIamHelper.getMessage(ErrorKeys.GET_USER_NOT_FOUND_MESSAGE, new Object[]{username})
        );

        new ErrorAsserter(errorResponse).assertError(expectedErrorResponse);
    }

    @Test
    @Disabled
    public void testAccessTokenExpired() throws InterruptedException {
        String uniqueString = TestUtils.generateUniqueString();

        String username = "Username-" + uniqueString;
        User user = new UserBuilder().setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername(username)
                .setPassword("Password")
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();
        user = testUsersHelper.createUser(user, HttpStatus.CREATED);

        Auth auth = new Auth(user.getUsername(), "Password");
        Token token = testIamHelper.authenticate(auth, HttpStatus.OK);

        // Wait for more than 5 minutes for the token to get expired
        Thread.sleep(WAIT_FOR_TOKEN_EXPIRATION);

        HttpHeaders httpHeaders = TestUtils.buildAuthHeader(token);
        ErrorResponse errorResponse = testUsersHelper.failGet(user.getId(), HttpStatus.UNAUTHORIZED, httpHeaders);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                testIamHelper.getMessage(ErrorKeys.UNAUTHORIZED_EXCEPTION_TITLE, null),
                testIamHelper.getMessage(ErrorKeys.ACCESS_TOKEN_EXPIRED_MESSAGE, null)
        );
        new ErrorAsserter(errorResponse).assertError(expectedErrorResponse);

        testUsersHelper.deleteUser(user.getId());
    }

    @Test
    public void testRefreshTokenNotFound() {
        String sRefreshToken = "InvalidRefreshToken";

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(sRefreshToken);

        ErrorResponse errorResponse = testIamHelper.failRefreshToken(refreshToken, HttpStatus.NOT_FOUND);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                testIamHelper.getMessage(ErrorKeys.REFRESH_TOKEN_NOT_FOUND_TITLE, null),
                testIamHelper.getMessage(ErrorKeys.REFRESH_TOKEN_NOT_FOUND_MESSAGE, new Object[]{sRefreshToken})
        );
        new ErrorAsserter(errorResponse).assertError(expectedErrorResponse);
    }

    @Test
    @Disabled
    public void testRefreshTokenExpired() throws InterruptedException {
        String uniqueString = TestUtils.generateUniqueString();

        String username = "Username-" + uniqueString;
        User user = new UserBuilder().setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername(username)
                .setPassword("Password")
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();
        user = testUsersHelper.createUser(user, HttpStatus.CREATED);

        Auth auth = new Auth(user.getUsername(), "Password");
        Token token = testIamHelper.authenticate(auth, HttpStatus.OK);

        // Wait for more than 10 minutes for the refresh token to get expired
        Thread.sleep(WAIT_FOR_RT_EXPIRATION);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token.getRefreshToken());

        ErrorResponse errorResponse = testIamHelper.failRefreshToken(refreshToken, HttpStatus.UNAUTHORIZED);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                testIamHelper.getMessage(ErrorKeys.UNAUTHORIZED_EXCEPTION_TITLE, null),
                testIamHelper.getMessage(ErrorKeys.REFRESH_TOKEN_EXPIRED_MESSAGE, null)
        );
        new ErrorAsserter(errorResponse).assertError(expectedErrorResponse);

        testUsersHelper.deleteUser(user.getId());
    }
}
