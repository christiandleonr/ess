package com.easysplit.ess.transactions;

import com.easysplit.ess.groups.domain.models.Group;
import com.easysplit.ess.groups.utils.TestGroupsHelper;
import com.easysplit.ess.transactions.domain.models.Debt;
import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.ess.transactions.domain.validators.TransactionsValidator;
import com.easysplit.ess.transactions.utils.TestTransactionsHelper;
import com.easysplit.ess.transactions.utils.TestTransactionsUtils;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.users.utils.TestUsersHelper;
import com.easysplit.shared.asserters.ErrorAsserter;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.models.ErrorResponse;
import com.easysplit.shared.domain.models.Money;
import com.easysplit.shared.utils.MessageHelper;
import com.easysplit.shared.utils.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestGroupTransactionsNegative {
    @Autowired
    private static TestTransactionsHelper transactionsHelper;
    @Autowired
    private static TestUsersHelper usersHelper;
    @Autowired
    private static TestGroupsHelper groupsHelper;
    @Autowired
    private MessageHelper messageHelper;

    private static User member1, member2, member3, member4;
    private static List<User> members = new ArrayList<>();

    private static Group group;

    @BeforeAll
    public static void setUp() {
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
    public static void tearDown() {
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
    public void testCreateGroupsTransactionsATransactionWithNullName() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).setName(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_NAME_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithEmptyName() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).setName("");
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_NAME_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNameTooLong() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).setName(TestUtils.generateString(TransactionsValidator.TRANSACTION_NAME_LENGTH_LIMIT + 1));
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NAME_TOOLONG_MESSAGE, new Object[] { TransactionsValidator.TRANSACTION_NAME_LENGTH_LIMIT })
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNullCurrency() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).setCurrency(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CURRENCY_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithEmptyCurrency() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).setCurrency(Currency.getInstance(""));
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CURRENCY_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithCurrencyTooLong() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).setCurrency(Currency.getInstance("AASSS"));
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_CURRENCY_TOOLONG_MESSAGE, new Object[] { TransactionsValidator.TRANSACTION_CURRENCY_CODE_LENGTH_LIMIT })
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNullDebt() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).setDebt(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_DEBT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionDebtTotalAmountMoneyObjectNull() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).getDebt().setTotalAmount(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_TOTAL_AMOUNT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionDebtTotalAmountAmountNull() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).getDebt().setTotalAmount(new Money());
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_TOTAL_AMOUNT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithTotalAmountNotGreaterThanZero() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).getDebt().setTotalAmount(new Money(0));
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_ZERO_TOTAL_AMOUNT_MESSAGE,
                        new Object[]{ TransactionsValidator.TRANSACTION_DEBT_TOTAL_AMOUNT_LOWER_LIMIT }
                )
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithDebtObjectDebtNull() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).getDebt().setDebt(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_DEBT_AMOUNT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionDebtObjectDebtAmountNull() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).getDebt().setDebt(new Money());
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_DEBT_AMOUNT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithDebtNotGreaterThanZero() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).getDebt().setDebt(new Money(0));
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_ZERO_DEBT_MESSAGE, new Object[]{ TransactionsValidator.TRANSACTION_DEBT_LOWER_LIMIT })
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithDebtGreaterThanTotalAmount() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).getDebt().setDebt(new Money(1000));
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_DEBT_GREATERTHAN_TOTALAMOUNT)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNullCreditor() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).setCreditor(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CREDITOR_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNullCreditorId() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).getCreditor().setId(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CREDITOR_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNullDebtor() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).setDebtor(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_DEBTOR_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNullDebtorId() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        transactions.get(0).getDebtor().setId(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_DEBTOR_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateGroupTransactionsGroupNotFound() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.GET_GROUP_NOT_FOUND_TITLE),
                messageHelper.getMessage(ErrorKeys.GET_GROUP_NOT_FOUND_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions("InvalidGroupId", transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testReadGroupTransactionsGroupNotFound() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, member1, new Money(150));

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.GET_GROUP_NOT_FOUND_TITLE),
                messageHelper.getMessage(ErrorKeys.GET_GROUP_NOT_FOUND_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failListGroupTransactions("InvalidGroupId", HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }
}
