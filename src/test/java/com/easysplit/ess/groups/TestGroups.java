package com.easysplit.ess.groups;

import com.easysplit.ess.groups.asserters.GroupsAsserter;
import com.easysplit.ess.groups.domain.models.Group;
import com.easysplit.ess.groups.utils.TestGroupsHelper;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.users.utils.TestUsersHelper;
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
public class TestGroups {
    @Autowired
    private TestGroupsHelper groupsHelper;

    private static User member1, member2, member3, member4, systemGeneratedUser;
    private static List<User> members = new ArrayList<>();

    /**
     * TODO Add comments
     */
    @BeforeAll
    public static void setUp(@Autowired TestUsersHelper usersHelper) {
        String uniqueString = TestUtils.generateUniqueString();

        // Create list of group members
        member1 = new User();
        member1.setName("Member 1-" + uniqueString);
        member1.setLastname("Member 1-" + uniqueString);
        member1.setUsername("Member 1-" + uniqueString);
        member1.setPassword("Password");
        member1.setPhone(TestUtils.generate10DigitNumber() + "");
        member1.setEmail("member1_" + uniqueString + "@gmail.com");
        member1 = usersHelper.createUser(member1, HttpStatus.CREATED);
        members.add(member1);

        member2 = new User();
        member2.setName("Member 2-" + uniqueString);
        member2.setLastname("Member 2-" + uniqueString);
        member2.setUsername("Member 2-" + uniqueString);
        member2.setPassword("Password");
        member2.setPhone(TestUtils.generate10DigitNumber() + "");
        member2.setEmail("member2_" + uniqueString + "@gmail.com");
        member2 = usersHelper.createUser(member2, HttpStatus.CREATED);
        members.add(member2);

        member3 = new User();
        member3.setName("Member 3-" + uniqueString);
        member3.setLastname("Member 3-" + uniqueString);
        member3.setUsername("Member 3-" + uniqueString);
        member3.setPassword("Password");
        member3.setPhone(TestUtils.generate10DigitNumber() + "");
        member3.setEmail("member3_" + uniqueString + "@gmail.com");
        member3 = usersHelper.createUser(member3, HttpStatus.CREATED);
        members.add(member3);

        member4 = new User();
        member4.setName("Member 4-" + uniqueString);
        member4.setLastname("Member 4-" + uniqueString);
        member4.setUsername("Member 4-" + uniqueString);
        member4.setPassword("Password");
        member4.setPhone(TestUtils.generate10DigitNumber() + "");
        member4.setEmail("member4_" + uniqueString + "@gmail.com");
        member4 = usersHelper.createUser(member4, HttpStatus.CREATED);
        members.add(member4);

        systemGeneratedUser = TestUtils.getSystemUser();
    }

    @AfterAll
    public static void tearDown(@Autowired TestUsersHelper usersHelper) {
        for (User user: members) {
            if (systemGeneratedUser.getId().equals(user.getId())) {
                continue;
            }

            if (user.getId() != null) {
                usersHelper.deleteUser(user.getId());
            }
        }
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroup() {
        String uniqueString = TestUtils.generateUniqueString();

        Group group = new Group();
        group.setName("Name-" + uniqueString);
        group.setDescription("Description" + uniqueString);
        group.setMembers(members);

        Group createdGroup = groupsHelper.createGroup(group, HttpStatus.CREATED);

        group.getMembers().add(systemGeneratedUser);
        new GroupsAsserter(createdGroup).assertGroup(group);

        groupsHelper.deleteGroup(createdGroup.getId());
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupNullDescription() {
        String uniqueString = TestUtils.generateUniqueString();

        Group group = new Group();
        group.setName("Name-" + uniqueString);
        group.setMembers(members);

        Group createdGroup = groupsHelper.createGroup(group, HttpStatus.CREATED);

        group.getMembers().add(systemGeneratedUser);
        new GroupsAsserter(createdGroup).assertGroup(group);

        groupsHelper.deleteGroup(createdGroup.getId());
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testDeleteGroup() {
        String uniqueString = TestUtils.generateUniqueString();

        Group group = new Group();
        group.setName("Name-" + uniqueString);
        group.setDescription("Description" + uniqueString);
        group.setMembers(members);

        Group createdGroup = groupsHelper.createGroup(group, HttpStatus.CREATED);
        groupsHelper.deleteGroup(createdGroup.getId());
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testGetGroup() {
        String uniqueString = TestUtils.generateUniqueString();

        Group group = new Group();
        group.setName("Name-" + uniqueString);
        group.setDescription("Description" + uniqueString);
        group.setMembers(members);

        Group createdGroup = groupsHelper.createGroup(group, HttpStatus.CREATED);
        Group retrievedGroup = groupsHelper.getGroup(createdGroup.getId(), Group.class, HttpStatus.OK);

        group.getMembers().add(systemGeneratedUser);
        new GroupsAsserter(retrievedGroup).assertGroup(group);

        groupsHelper.deleteGroup(retrievedGroup.getId());
    }
}
