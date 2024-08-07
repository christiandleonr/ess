package com.easysplit.ess.users;

import com.easysplit.ess.iam.domain.models.Auth;
import com.easysplit.ess.iam.domain.models.Token;
import com.easysplit.ess.iam.utils.TestIamHelper;
import com.easysplit.ess.user.domain.models.Friendship;
import com.easysplit.ess.user.domain.models.FriendshipStatus;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.users.asserters.UsersListAsserter;
import com.easysplit.ess.users.builders.UserBuilder;
import com.easysplit.ess.users.utils.TestUsersHelper;
import com.easysplit.shared.asserters.ErrorAsserter;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.models.ErrorResponse;
import com.easysplit.shared.domain.models.ResourceList;
import com.easysplit.shared.utils.MessageHelper;
import com.easysplit.shared.utils.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestFriendsNegative {
    @Autowired
    private TestUsersHelper usersHelper;
    @Autowired
    private TestIamHelper iamHelper;
    @Autowired
    private MessageHelper messageHelper;

    private static User user1, user2;
    private static final List<User> usersCreated = new ArrayList<>();

    @BeforeAll
    public static void setUp(@Autowired TestUsersHelper usersHelper) {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        user1 = userBuilder.setName("User1-" + uniqueString)
                .setLastname("User1-Lastname-" + uniqueString)
                .setUsername("User1-Username-" + uniqueString)
                .setPassword("Password")
                .setEmail("user1" + uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();
        user1 = usersHelper.createUser(user1, HttpStatus.CREATED);
        usersCreated.add(user1);

        userBuilder.clear();
        user2 = userBuilder.setName("User2-" + uniqueString)
                .setLastname("User2-Lastname-" + uniqueString)
                .setUsername("User2-Username-" + uniqueString)
                .setPassword("Password")
                .setEmail("user2" + uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();
        user2 = usersHelper.createUser(user2, HttpStatus.CREATED);
        usersCreated.add(user2);
    }

    @AfterAll
    public static void tearDown(@Autowired TestUsersHelper usersHelper) {
        for (User user: usersCreated) {
            if (user.getId() != null) {
                usersHelper.deleteUser(user.getId());
            }
        }
    }

    @Test
    public void TestCreateFriendshipFriendNull() {
        Friendship friendship = new Friendship();

        Auth auth = new Auth(user1.getEmail(), "Password");
        Token token = iamHelper.authenticate(auth, HttpStatus.OK);
        HttpHeaders authHeaders = TestUtils.buildAuthHeader(token);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_FRIENDSHIP_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_FRIENDSHIP_NULL_FRIEND_MESSAGE)
        );

        ErrorResponse actualErrorResponse = usersHelper.failAddFriend(friendship, HttpStatus.BAD_REQUEST, authHeaders);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    @Test
    public void TestCreateFriendshipFriendIdNull() {
        Friendship friendship = new Friendship();
        friendship.setFriend(new User());

        Auth auth = new Auth(user1.getEmail(), "Password");
        Token token = iamHelper.authenticate(auth, HttpStatus.OK);
        HttpHeaders authHeaders = TestUtils.buildAuthHeader(token);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_FRIENDSHIP_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_FRIENDSHIP_EMPTY_FRIEND_ID_MESSAGE)
        );

        ErrorResponse actualErrorResponse = usersHelper.failAddFriend(friendship, HttpStatus.BAD_REQUEST, authHeaders);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    @Test
    public void TestCreateFriendshipFriendIdEmpty() {
        Friendship friendship = new Friendship();
        friendship.setFriend(new User());
        friendship.getFriend().setId("");

        Auth auth = new Auth(user1.getEmail(), "Password");
        Token token = iamHelper.authenticate(auth, HttpStatus.OK);
        HttpHeaders authHeaders = TestUtils.buildAuthHeader(token);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_FRIENDSHIP_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_FRIENDSHIP_EMPTY_FRIEND_ID_MESSAGE)
        );

        ErrorResponse actualErrorResponse = usersHelper.failAddFriend(friendship, HttpStatus.BAD_REQUEST, authHeaders);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    @Test
    public void TestCreateFriendshipAlreadyExist() {
        Friendship friendship = new Friendship();
        friendship.setFriend(user2);

        Auth auth = new Auth(user1.getEmail(), "Password");
        Token token = iamHelper.authenticate(auth, HttpStatus.OK);
        HttpHeaders authHeaders = TestUtils.buildAuthHeader(token);

        usersHelper.addFriend(friendship, HttpStatus.CREATED, authHeaders);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.LOAD_FRIENDSHIP_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.LOAD_FRIENDSHIP_EXIST_MESSAGE, new Object[] { user2.getId(), user1.getId() })
        );

        ErrorResponse actualErrorResponse = usersHelper.failAddFriend(friendship, HttpStatus.BAD_REQUEST, authHeaders);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }
}
