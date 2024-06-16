package com.easysplit.ess.transactions;

import com.easysplit.ess.groups.domain.models.Group;
import com.easysplit.ess.groups.utils.TestGroupsHelper;
import com.easysplit.ess.transactions.asserters.TransactionsListAsserter;
import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.ess.transactions.utils.TestTransactionsHelper;
import com.easysplit.ess.transactions.utils.TestTransactionsUtils;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.users.utils.TestUsersHelper;
import com.easysplit.shared.domain.models.Money;
import com.easysplit.shared.domain.models.ResourceList;
import com.easysplit.shared.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestGroupTransactions {
    @Autowired
    private TestTransactionsHelper transactionsHelper;
    @Autowired
    private TestGroupsHelper groupsHelper;

    private static User member1, member2, member3;
    private static List<User> members = new ArrayList<>();

    private static Group group;

    @BeforeAll
    public static void setUp(@Autowired TestUsersHelper usersHelper, @Autowired TestGroupsHelper groupsHelper) {
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

        group = new Group();
        group.setName("Name-" + uniqueString);
        group.setDescription("Description" + uniqueString);
        group.setMembers(members);
        group = groupsHelper.createGroup(group, HttpStatus.CREATED);
    }

    @AfterAll
    @Disabled
    public static void tearDown(@Autowired TestUsersHelper usersHelper, TestGroupsHelper groupsHelper) {
        for (User user: members) {
            if (user.getId() != null) {
                usersHelper.deleteUser(user.getId());
            }
        }

        if (group != null && group.getId() != null) {
            groupsHelper.deleteGroup(group.getId());
        }
    }

    /**
     * TODO Add comments
     */
    @Test
    public void createAndReadGroupTransactionsBetweenAllMembers() {
        List<Transaction> expectedTransactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(300));

        transactionsHelper.createGroupTransactions(group.getId(), expectedTransactions, HttpStatus.CREATED);
        ResourceList<Transaction> createdTransactions = transactionsHelper.listGroupTransactions(group.getId(), HttpStatus.OK);

        Assertions.assertEquals(3, createdTransactions.getCount());
        Assertions.assertFalse(createdTransactions.isHasMore());

        TransactionsListAsserter.assertListEquals(expectedTransactions, createdTransactions.getData());
    }

    /**
     * TODO - Keep working on transactions resource automation, this is the most important part of the backend, we must ensure every single case is cover with tests
     */
    @Test
    public void createAndReadGroupTransactionsBetweenSomeMembers() {

    }
}
