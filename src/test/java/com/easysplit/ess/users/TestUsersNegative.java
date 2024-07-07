package com.easysplit.ess.users;

import com.easysplit.shared.asserters.ErrorAsserter;
import com.easysplit.shared.utils.MessageHelper;
import com.easysplit.shared.utils.TestUtils;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.user.domain.validators.UserValidator;
import com.easysplit.ess.users.builders.UserBuilder;
import com.easysplit.ess.users.utils.TestUsersHelper;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.models.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestUsersNegative {
    @Autowired
    private TestUsersHelper testUsersHelper;
    @Autowired
    private MessageHelper messageHelper;

    /**
     * Attempts to create a user with a null name, null names are invalid,
     * and we should get the proper error message
     */
    @Test
    public void testCreateUserNullName() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_EMPTYNAME_MESSAGE, null)
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with an empty name, empty names are invalid,
     * and we should get the proper error message
     */
    @Test
    public void testCreateUserEmptyName() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("")
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_EMPTYNAME_MESSAGE, null)
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with the name exceeding the limit of characters, this is invalid, and we should get
     * the proper error message
     */
    @Test
    public void testCreateUserNameTooLarge() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName(TestUtils.generateString(UserValidator.USER_NAME_LENGTH_LIMIT + 1))
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_NAMETOOLONG_MESSAGE, new Object[]{ UserValidator.USER_NAME_LENGTH_LIMIT })
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with an empty lastname, empty lastnames are invalid,
     * and we should get the proper error message
     */
    @Test
    public void testCreateUserEmptyLastname() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("")
                .setUsername("Username-" + uniqueString)
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_EMPTYLASTNAME_MESSAGE, null)
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with the lastname exceeding the limit of characters, this is invalid, and we should get
     * the proper error message
     */
    @Test
    public void testCreateUserLastnameTooLarge() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname(TestUtils.generateString(UserValidator.USER_LASTNAME_LENGTH_LIMIT + 1))
                .setUsername("Username-" + uniqueString)
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_LASTNAMETOOLONG_MESSAGE, new Object[]{ UserValidator.USER_LASTNAME_LENGTH_LIMIT })
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with an empty username, empty usernames are invalid,
     * and we should get the proper error message
     */
    @Test
    public void testCreateUserEmptyUsername() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("")
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_EMPTYUSERNAME_MESSAGE, null)
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with the username exceeding the limit of characters,
     * this is invalid, and we should get the proper error message
     */
    @Test
    public void testCreateUserUsernameTooLarge() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername(TestUtils.generateString(UserValidator.USER_USERNAME_LENGTH_LIMIT + 1))
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_USERNAMETOOLONG_MESSAGE, new Object[]{ UserValidator.USER_USERNAME_LENGTH_LIMIT })
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with a username already being used, this is invalid, and we should get the proper
     * error message
     */
    @Test
    public void testCreateUserUsernameExist() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User user = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setEmail(uniqueString + "@gmail.com")
                .setPassword("Password")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        user = testUsersHelper.createUser(user, HttpStatus.CREATED);

        User invalidUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername(user.getUsername())
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_NOT_UNIQUE_USERNAME_MESSAGE, new Object[]{ user.getUsername() })
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(invalidUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with a null email, null emails are invalid,
     * and we should get the proper error message
     */
    @Test
    public void testCreateUserNullEmail() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setPassword("Password")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_EMPTYEMAIL_MESSAGE, null)
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with an empty email, empty emails are invalid,
     * and we should get the proper error message
     */
    @Test
    public void testCreateUserEmptyEmail() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setPassword("Password")
                .setEmail("")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_EMPTYEMAIL_MESSAGE, null)
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with the email exceeding the limit of characters,
     * this is invalid, and we should get the proper error message
     */
    @Test
    public void testCreateUserEmailTooLarge() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setPassword("Password")
                .setEmail(TestUtils.generateString(UserValidator.USER_EMAIL_LENGTH_LIMIT + 1) + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_EMAILTOOLONG_MESSAGE, new Object[]{ UserValidator.USER_EMAIL_LENGTH_LIMIT })
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with an email with an invalid format,
     * we should get the proper error message
     */
    @Test
    public void testCreateUserInvalidEmail() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setPassword("Password")
                .setEmail("INVALID")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_INVALIDEMAILFORMAT_MESSAGE, new Object[]{ "INVALID" })
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);

    }

    /**
     * Attempts to create a user with an email already being used, this is invalid,
     * and we should get the proper error message
     */
    @Test
    public void testCreateUserEmailExist() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User user = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setPassword("Password")
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        user = testUsersHelper.createUser(user, HttpStatus.CREATED);

        uniqueString = TestUtils.generateUniqueString();
        User invalidUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setPassword("Password")
                .setEmail(user.getEmail())
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_NOT_UNIQUE_EMAIL_MESSAGE, new Object[]{ user.getEmail() })
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(invalidUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with a null phone, null phones are invalid,
     * and we should get the proper error message
     */
    @Test
    public void testCreateUserNullPhone() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setPassword("Password")
                .setEmail(uniqueString + "@gmail.com")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_EMPTYPHONE_MESSAGE, null)
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with an empty phone, empty phones are invalid,
     * and we should get the proper error message
     */
    @Test
    public void testCreateUserEmptyPhone() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setPassword("Password")
                .setEmail(uniqueString + "@gmail.com")
                .setPhone("")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_EMPTYPHONE_MESSAGE, null)
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with the phone exceeding the limit of characters,
     * this is invalid, and we should get the proper error message
     */
    @Test
    public void testCreateUserPhoneTooLarge() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setPassword("Password")
                .setEmail(uniqueString + "@gmail.com")
                .setPhone("66778484709")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_WRONGPHONESIZE_MESSAGE, null)
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with a phone with an invalid format,
     * we should get the proper error message
     */
    @Test
    public void testCreateUserInvalidPhone() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User expectedUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setPassword("Password")
                .setEmail(uniqueString + "@gmail.com")
                .setPhone("INVALID")
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_INVALIDPHONECHAR_MESSAGE, null)
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(expectedUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a user with a phone already being used, this is invalid,
     * and we should get the proper error message
     */
    @Test
    public void testCreateUserPhoneNumberExist() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        User user = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setPassword("Password")
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();

        user = testUsersHelper.createUser(user, HttpStatus.CREATED);

        uniqueString = TestUtils.generateUniqueString();
        User invalidUser = userBuilder.setName("Name-" + uniqueString)
                .setLastname("Lastname-" + uniqueString)
                .setUsername("Username-" + uniqueString)
                .setEmail(uniqueString + "@gmail.com")
                .setPhone(user.getPhone())
                .build();

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_USER_NOT_UNIQUE_PHONE_MESSAGE, new Object[]{ user.getPhone() })
        );

        ErrorResponse actualErrorResponse = testUsersHelper.failCreateUser(invalidUser, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to get a user that does not exist, we should get the proper not found error message
     */
    @Test
    public void testGetUserNotFound() {
        ErrorResponse actualErrorResponse = testUsersHelper.failGet("INVALID_ID", HttpStatus.NOT_FOUND);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.GET_USER_NOT_FOUND_TITLE, null),
                messageHelper.getMessage(ErrorKeys.GET_USER_NOT_FOUND_MESSAGE, new Object[]{ "INVALID_ID" })
        );

        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to delete a user that does not exist, we should get the proper not found error message
     */
    @Test
    public void testDeleteUserNotFound() {
        ErrorResponse actualErrorResponse = testUsersHelper.failDelete("INVALID_ID", HttpStatus.NOT_FOUND);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.GET_USER_NOT_FOUND_TITLE, null),
                messageHelper.getMessage(ErrorKeys.GET_USER_NOT_FOUND_MESSAGE, new Object[]{ "INVALID_ID" })
        );

        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }
}
