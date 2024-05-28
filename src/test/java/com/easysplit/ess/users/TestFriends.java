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
import com.easysplit.shared.domain.models.ResourceList;
import com.easysplit.shared.utils.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class TestFriends {
    @Autowired
    private static TestUsersHelper usersHelper;
    @Autowired
    private TestIamHelper iamHelper;

    private static User user1, user2;
    private static final List<User> usersCreated = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        String uniqueString = TestUtils.generateUniqueString();

        UserBuilder userBuilder = new UserBuilder();
        user1 = userBuilder.setName("User1-" + uniqueString)
                .setLastname("User1-Lastname-" + uniqueString)
                .setUsername("User1-Username-" + uniqueString)
                .setPassword("Password")
                .setEmail("user1" + uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();
        user1 = usersHelper.createUser(user1, HttpStatus.OK);
        usersCreated.add(user1);

        userBuilder.clear();
        user2 = userBuilder.setName("User2-" + uniqueString)
                .setLastname("User2-Lastname-" + uniqueString)
                .setUsername("User2-Username-" + uniqueString)
                .setPassword("Password")
                .setEmail("user2" + uniqueString + "@gmail.com")
                .setPhone(TestUtils.generate10DigitNumber() + "")
                .build();
        user2 = usersHelper.createUser(user2, HttpStatus.OK);
        usersCreated.add(user2);
    }

    @AfterAll
    public static void tearDown() {
        for (User user: usersCreated) {
            if (user.getId() != null) {
                usersHelper.deleteUser(user.getId());
            }
        }
    }

    @Test
    public void testAddAndReadFriends() {
        Friendship friendship = new Friendship();
        friendship.setFriend(user2);

        Auth auth = new Auth(user1.getUsername(), "Password");
        Token token = iamHelper.authenticate(auth, HttpStatus.OK);
        HttpHeaders authHeaders = TestUtils.buildAuthHeader(token);

        Friendship createdFriendship = usersHelper.addFriend(friendship, HttpStatus.CREATED, authHeaders);
        Assertions.assertEquals(FriendshipStatus.PENDING, createdFriendship.getStatus());

        List<User> user1ExpectedFriends = new ArrayList<>();
        user1ExpectedFriends.add(user2);

        token = iamHelper.authenticate(auth, HttpStatus.OK);
        authHeaders = TestUtils.buildAuthHeader(token);
        ResourceList<User> user1ActualFriends = usersHelper.listFriends(user1.getId(), HttpStatus.OK, authHeaders);

        UsersListAsserter.assertListEquals(user1ExpectedFriends, user1ActualFriends.getData());
    }
}
