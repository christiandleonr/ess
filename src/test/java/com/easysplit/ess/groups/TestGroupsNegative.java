package com.easysplit.ess.groups;

import com.easysplit.ess.groups.domain.models.Group;
import com.easysplit.ess.groups.domain.validators.GroupValidator;
import com.easysplit.ess.groups.utils.TestGroupsHelper;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.users.utils.TestUsersHelper;
import com.easysplit.shared.asserters.ErrorAsserter;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.models.ErrorResponse;
import com.easysplit.shared.utils.MessageHelper;
import com.easysplit.shared.utils.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestGroupsNegative {
    @Autowired
    private TestGroupsHelper groupsHelper;
    @Autowired
    private TestUsersHelper usersHelper;
    @Autowired
    private MessageHelper messageHelper;

    private static final List<User> members = new ArrayList<>();

    /**
     * Initialize objects that are going to be used in the tests
     */
    @BeforeAll
    public static void setUp(@Autowired TestUsersHelper usersHelper) {
        String uniqueString = TestUtils.generateUniqueString();

        // Create list of group members

        User member1 = new User();
        member1.setName("Member 1-" + uniqueString);
        member1.setLastname("Member 1-" + uniqueString);
        member1.setUsername("Member 1-" + uniqueString);
        member1.setPassword("Password");
        member1.setPhone(TestUtils.generate10DigitNumber() + "");
        member1.setEmail("member1_" + uniqueString + "@gmail.com");
        member1 = usersHelper.createUser(member1, HttpStatus.CREATED);
        members.add(member1);

        User member2 = new User();
        member2.setName("Member 2-" + uniqueString);
        member2.setLastname("Member 2-" + uniqueString);
        member2.setUsername("Member 2-" + uniqueString);
        member2.setPassword("Password");
        member2.setPhone(TestUtils.generate10DigitNumber() + "");
        member2.setEmail("member2_" + uniqueString + "@gmail.com");
        member2 = usersHelper.createUser(member2, HttpStatus.CREATED);
        members.add(member2);

        User member3 = new User();
        member3.setName("Member 3-" + uniqueString);
        member3.setLastname("Member 3-" + uniqueString);
        member3.setUsername("Member 3-" + uniqueString);
        member3.setPassword("Password");
        member3.setPhone(TestUtils.generate10DigitNumber() + "");
        member3.setEmail("member3_" + uniqueString + "@gmail.com");
        member3 = usersHelper.createUser(member3, HttpStatus.CREATED);
        members.add(member3);

        User member4 = new User();
        member4.setName("Member 4-" + uniqueString);
        member4.setLastname("Member 4-" + uniqueString);
        member4.setUsername("Member 4-" + uniqueString);
        member4.setPassword("Password");
        member4.setPhone(TestUtils.generate10DigitNumber() + "");
        member4.setEmail("member4_" + uniqueString + "@gmail.com");
        member4 = usersHelper.createUser(member4, HttpStatus.CREATED);
        members.add(member4);
    }

    @AfterAll
    public static void tearDown(@Autowired TestUsersHelper usersHelper) {
        for (User user: members) {
            if (user.getId() != null) {
                usersHelper.deleteUser(user.getId());
            }
        }
    }

    /**
     * Attempts to create a group with null name, null name is not allowed and server should throw proper exception
     */
    @Test
    public void testCreateGroupNullName() {
        String uniqueString = TestUtils.generateUniqueString();

        Group group = new Group();
        group.setDescription("Description-" + uniqueString);
        group.setMembers(members);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_GROUP_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_GROUP_EMPTYNAME_MESSAGE, null)
        );

        ErrorResponse actualErrorResponse = groupsHelper.failCreateGroup(group, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a group with an empty name, empty names are not allowed and server should
     * throw proper exception
     */
    @Test
    public void testCreateGroupEmptyName() {
        String uniqueString = TestUtils.generateUniqueString();

        Group group = new Group();
        group.setName("");
        group.setDescription("Description-" + uniqueString);
        group.setMembers(members);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_GROUP_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(ErrorKeys.CREATE_GROUP_EMPTYNAME_MESSAGE, null)
        );

        ErrorResponse actualErrorResponse = groupsHelper.failCreateGroup(group, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a group name with a name that exceeds the number of characters allowed,
     * server must throw the proper exception
     */
    @Test
    public void testCreateGroupNameExceedCharactersLimit() {
        String uniqueString = TestUtils.generateUniqueString();

        Group group = new Group();
        group.setName(generateLongString(GroupValidator.GROUP_NAME_LENGTH_LIMIT + 1));
        group.setDescription("Description-" + uniqueString);
        group.setMembers(members);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_GROUP_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(
                        ErrorKeys.CREATE_GROUP_NAMETOOLONG_MESSAGE,
                        new Object[] { GroupValidator.GROUP_NAME_LENGTH_LIMIT }
                )
        );

        ErrorResponse actualErrorResponse = groupsHelper.failCreateGroup(group, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a group with a description that exceeds the number of characters allowed. server must throw
     * proper exception
     */
    @Test
    public void testCreateGroupDescriptionExceedCharactersLimit() {
        String uniqueString = TestUtils.generateUniqueString();

        Group group = new Group();
        group.setName("Name-" + uniqueString);
        group.setDescription(generateLongString(GroupValidator.GROUP_DESCRIPTION_LENGTH_LIMIT + 1));
        group.setMembers(members);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_GROUP_ILLEGALARGUMENT_TITLE, null),
                messageHelper.getMessage(
                        ErrorKeys.CREATE_GROUP_DESCRIPTIONTOOLONG_MESSAGE,
                        new Object[] { GroupValidator.GROUP_DESCRIPTION_LENGTH_LIMIT }
                )
        );

        ErrorResponse actualErrorResponse = groupsHelper.failCreateGroup(group, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a group providing a user to be added as member that do not exist, server should throw
     * not found user exception
     */
    @Test
    public void testCreateGroupNotFoundMember() {
        String uniqueString = TestUtils.generateUniqueString();

        Group group = new Group();
        group.setName("Name-" + uniqueString);
        group.setDescription("Description-" + uniqueString);
        group.setMembers(new ArrayList<>(members));

        String memberId = members.get(0).getId();
        usersHelper.deleteUser(memberId);
        members.remove(0);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.GET_USER_NOT_FOUND_TITLE, null),
                messageHelper.getMessage(ErrorKeys.GET_USER_NOT_FOUND_MESSAGE, new Object[]{ memberId })
        );

        ErrorResponse actualErrorResponse = groupsHelper.failCreateGroup(group, HttpStatus.NOT_FOUND);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to retrieve group info for a group id that do not exist, server must throw proper exception
     */
    @Test
    public void testGetGroupNotFound() {
        String groupId = "NotFoundId";

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.GET_GROUP_NOT_FOUND_TITLE, null),
                messageHelper.getMessage(ErrorKeys.GET_GROUP_NOT_FOUND_MESSAGE, new Object[]{ groupId })
        );

        ErrorResponse actualErrorResponse = groupsHelper.failGet(groupId, HttpStatus.NOT_FOUND);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to delete a group with a group id that do not exist, server must throw proper exception
     */
    @Test
    public void testDeleteGroupNotFound() {
        String groupId = "NotFoundId";

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.GET_GROUP_NOT_FOUND_TITLE, null),
                messageHelper.getMessage(ErrorKeys.GET_GROUP_NOT_FOUND_MESSAGE, new Object[]{ groupId })
        );

        ErrorResponse actualErrorResponse = groupsHelper.failDelete(groupId, HttpStatus.NOT_FOUND);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Helper method that creates strings with a given length
     *
     * @param length resulting string size
     * @return string with the given length
     */
    private String generateLongString(int length) {
        return "a".repeat(Math.max(0, length));
    }
}
