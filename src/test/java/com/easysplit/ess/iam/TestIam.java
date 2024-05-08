package com.easysplit.ess.iam;

import com.easysplit.ess.iam.asserters.TokenAsserter;
import com.easysplit.ess.iam.domain.models.Auth;
import com.easysplit.ess.iam.domain.models.RefreshToken;
import com.easysplit.ess.iam.domain.models.Token;
import com.easysplit.ess.iam.utils.TestIamHelper;
import com.easysplit.ess.shared.utils.TestUtils;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.users.builders.UserBuilder;
import com.easysplit.ess.users.utils.TestUsersHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestIam {
    @Autowired
    private TestIamHelper testIamHelper;
    @Autowired
    private TestUsersHelper testUsersHelper;

    @Test
    public void testAuthenticateUser() {
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

        Auth auth = new Auth(user.getEmail(), "Password");
        Token token = testIamHelper.authenticate(auth, HttpStatus.OK);

        new TokenAsserter(token).assertToken();

        testUsersHelper.deleteUser(user.getId());
    }

    @Test
    public void testRefreshToken() {
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

        Auth auth = new Auth(user.getEmail(), "Password");
        Token token = testIamHelper.authenticate(auth, HttpStatus.OK);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token.getRefreshToken());
        token = testIamHelper.refreshToken(refreshToken, HttpStatus.OK);
        new TokenAsserter(token).assertToken();

        testUsersHelper.deleteUser(user.getId());
    }
}
