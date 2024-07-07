package com.easysplit.ess.transactions;

import com.easysplit.ess.groups.domain.models.Group;
import com.easysplit.ess.groups.utils.TestGroupsHelper;
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
import org.junit.jupiter.api.Disabled;
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
    private TestTransactionsHelper transactionsHelper;
    @Autowired
    private MessageHelper messageHelper;

    private static User creditor;
    private static final List<User> members = new ArrayList<>();

    private static Group group;

    @BeforeAll
    public static void setUp(@Autowired TestUsersHelper usersHelper, @Autowired TestGroupsHelper groupsHelper) {
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

        creditor = new User(member1);

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

        group = new Group();
        group.setName("Name-" + uniqueString);
        group.setDescription("Description" + uniqueString);
        group.setMembers(members);
        group = groupsHelper.createGroup(group, HttpStatus.CREATED);
    }

    @AfterAll
    @Disabled
    public static void tearDown(@Autowired TestUsersHelper usersHelper, @Autowired TestGroupsHelper groupsHelper) {
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
     * Attempts to create the transactions for a given group, one of the transactions in the list will have a null
     * name which is not allowed, the server must throw proper exception
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNullName() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).setName(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_NAME_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create the transactions for a given group, one of the transactions in the list will have an empty string
     * name which is not allowed, the server must throw proper exception
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithEmptyName() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).setName("");
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_NAME_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create the transactions for a given group, one of the transactions in the list will have a name to long
     * which is not allowed, the server must throw proper exception
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNameTooLong() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).setName(TestUtils.generateString(TransactionsValidator.TRANSACTION_NAME_LENGTH_LIMIT + 1));
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NAME_TOOLONG_MESSAGE, new Object[] { TransactionsValidator.TRANSACTION_NAME_LENGTH_LIMIT })
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create the transactions for a given group, one of the transactions in the list has null for its currency,
     * this is not allowed and server must throw proper exception.
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNullCurrency() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).setCurrency(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CURRENCY_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO - Figure out how to send an empty string for the currency
     */
    @Test
    @Disabled
    public void testCreateGroupsTransactionsATransactionWithEmptyCurrency() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).setCurrency(Currency.getInstance(""));
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CURRENCY_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO - Figure out a way to send a long currency code
     */
    @Test
    @Disabled
    public void testCreateGroupsTransactionsATransactionWithCurrencyTooLong() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).setCurrency(Currency.getInstance("AASSS"));
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_CURRENCY_TOOLONG_MESSAGE, new Object[] { TransactionsValidator.TRANSACTION_CURRENCY_CODE_LENGTH_LIMIT })
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a set of transactions for a group but one of the transactions has a null debt, this is not
     * allowed and server must throw proper exception
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNullDebt() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).setDebt(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_DEBT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a set of transactions for a group but one of the transactions has a null total amount
     * for the debt, this is not allowed and server must throw proper exception
     */
    @Test
    public void testCreateGroupsTransactionsATransactionDebtTotalAmountMoneyObjectNull() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).getDebt().setTotalAmount(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_TOTAL_AMOUNT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO - Figure out if the JSON parser exception that comes from the total amount being null can be resolved
     */
    @Test
    @Disabled
    public void testCreateGroupsTransactionsATransactionDebtTotalAmountAmountNull() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).getDebt().setTotalAmount(new Money());
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_TOTAL_AMOUNT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a set of transactions for a given group, one of the transaction will have 0 as total amount,
     * this is not allowed a server must throw the proper exception
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithTotalAmountNotGreaterThanZero() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

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
     * Attempts to create a set of transactions for a given group, one of the transactions has a null value for the debt amount,
     * this is not allowed and server must throw proper exception
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithDebtObjectDebtNull() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).getDebt().setDebt(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_DEBT_AMOUNT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO - Figure out if the JSON parser exception that comes from the debt amount being null can be resolved
     */
    @Test
    @Disabled
    public void testCreateGroupsTransactionsATransactionDebtObjectDebtAmountNull() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).getDebt().setDebt(new Money());
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_DEBT_AMOUNT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a set of transactions for a given group, one of the transactions has a debt as 0,
     * this is not allowed and server must throw proper exception
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithDebtNotGreaterThanZero() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).getDebt().setDebt(new Money(0));
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_ZERO_DEBT_MESSAGE, new Object[]{ TransactionsValidator.TRANSACTION_DEBT_LOWER_LIMIT })
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a set of transactions, one of the transactions has a debt greater than the total amount,
     * which is not allowed, server must throw proper exception
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithDebtGreaterThanTotalAmount() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).getDebt().setDebt(new Money(1000));
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_DEBT_GREATERTHAN_TOTALAMOUNT)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a set of transactions for a given group, a transaction has a null creditor which is not allowed,
     * server must throw proper exception
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNullCreditor() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).setCreditor(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CREDITOR_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a set of transaction linked to a given group, one of the transactions has a creditor with
     * null id which is not allowed, server must throw proper exception
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNullCreditorId() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        User substituteWithBadData = new User(transactions.get(0).getCreditor());
        substituteWithBadData.setId(null);

        transactions.get(0).setCreditor(substituteWithBadData);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CREDITOR_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a set of transactions linked to a given group, one of the transactions has a null debtor
     * which is not allowed, server must throw proper exception
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNullDebtor() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        transactions.get(0).setDebtor(null);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_DEBTOR_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a set of transactions linked to a given group, one of the transactions has a null id for the
     * debtor, this is not allowed and server must throw proper exception
     */
    @Test
    public void testCreateGroupsTransactionsATransactionWithNullDebtorId() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        User substituteWithBadData = new User(transactions.get(0).getDebtor());
        substituteWithBadData.setId(null);

        transactions.get(0).setDebtor(substituteWithBadData);
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_DEBTOR_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(group.getId(), transactions, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to create a set of transactions linked to a given group, but the group id provided does not exist,
     * server must throw not found exception
     */
    @Test
    public void testCreateGroupTransactionsGroupNotFound() {
        List<Transaction> transactions = TestTransactionsUtils.divideTransactionEqually(members, creditor, new Money(150));

        String invalidId = "InvalidGroupId";
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.GET_GROUP_NOT_FOUND_TITLE),
                messageHelper.getMessage(ErrorKeys.GET_GROUP_NOT_FOUND_MESSAGE, new Object[]{ invalidId })
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateGroupTransactions(invalidId, transactions, HttpStatus.NOT_FOUND);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * Attempts to read the transactions for a group that do not exist, server must throw back not found exception
     */
    @Test
    public void testReadGroupTransactionsGroupNotFound() {
        String invalidId = "InvalidGroupId";

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.GET_GROUP_NOT_FOUND_TITLE),
                messageHelper.getMessage(ErrorKeys.GET_GROUP_NOT_FOUND_MESSAGE, new Object[]{ invalidId })
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failListGroupTransactions(invalidId, HttpStatus.NOT_FOUND);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }
}
