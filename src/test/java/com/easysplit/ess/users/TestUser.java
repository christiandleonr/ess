package com.easysplit.ess.users;

import com.easysplit.ess.shared.utils.TestUtils;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.users.asserters.UserAsserter;
import com.easysplit.ess.users.builders.UserBuilder;
import com.easysplit.ess.users.utils.TestUsersHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestUser {
    @Autowired
    private TestUsersHelper testUsersHelper;

    /**
     * Validates that a user can be created when all its values are valid
     */
    @Test
    public void testCreateUser() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setPassword("Password")
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        User actualUser = testUsersHelper.createUser(expectedUser, HttpStatus.CREATED);
        new UserAsserter(actualUser).assertUser(expectedUser);

        testUsersHelper.deleteUser(actualUser.getId());
    }

    /**
     * Attempts to create a user with a null lastname, null lastnames are invalid,
     * and we should get the proper error message
     */
    @Test
    public void testCreateUserNullLastName() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .setPassword("Password")
                .build();

        User actualUser = testUsersHelper.createUser(expectedUser, HttpStatus.CREATED);
        new UserAsserter(actualUser).assertUser(expectedUser);

        testUsersHelper.deleteUser(actualUser.getId());
    }

    /**
     * Attempts to create a user with a null username, null usernames are invalid,
     * and we should get the proper error message
     */
    @Test
    public void testCreateUserNullUsername() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .setPassword("Password")
                .build();

        User actualUser = testUsersHelper.createUser(expectedUser, HttpStatus.CREATED);
        new UserAsserter(actualUser).assertUser(expectedUser);

        testUsersHelper.deleteUser(actualUser.getId());
    }

    /**
     * Validates that we can fetch an existing user
     */
    @Test
    public void testGetUser() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setPassword("Password")
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        String id = testUsersHelper.createUser(expectedUser, HttpStatus.CREATED).getId();
        User actualUser = testUsersHelper.getUser(id, User.class, HttpStatus.OK);

        new UserAsserter(actualUser).assertUser(expectedUser);

        testUsersHelper.deleteUser(id);
    }

    /**
     * Validates we can delete an existing user
     */
    @Test
    public void testDeleteUser() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User user = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setPassword("Password")
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        User createdUser = testUsersHelper.createUser(user, HttpStatus.CREATED);
        testUsersHelper.deleteUser(createdUser.getId());
    }
}
