package com.easysplit.ess.transactions;

import com.easysplit.ess.transactions.domain.models.Debt;
import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.ess.transactions.domain.validators.TransactionsValidator;
import com.easysplit.ess.transactions.utils.TestTransactionsHelper;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.users.builders.UserBuilder;
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
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class TestTransactionsNegative {
    @Autowired
    private static TestTransactionsHelper transactionsHelper;
    @Autowired
    private static TestUsersHelper usersHelper;
    @Autowired
    private MessageHelper messageHelper;

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

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionNullName() {
        Transaction transaction = new Transaction();
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(200));
        debt.setDebt(new Money(150));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_NAME_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionEmptyName() {
        Transaction transaction = new Transaction();
        transaction.setName("");
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(200));
        debt.setDebt(new Money(150));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_NAME_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionNameTooLong() {
        Transaction transaction = new Transaction();
        transaction.setName(TestUtils.generateString(TransactionsValidator.TRANSACTION_NAME_LENGTH_LIMIT + 1));
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(200));
        debt.setDebt(new Money(150));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NAME_TOOLONG_MESSAGE, new Object[] { TransactionsValidator.TRANSACTION_NAME_LENGTH_LIMIT })
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionNullCurrency() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(200));
        debt.setDebt(new Money(150));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CURRENCY_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionEmptyCurrency() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance(""));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(200));
        debt.setDebt(new Money(150));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CURRENCY_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionCurrencyTooLong() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("AASSS"));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(200));
        debt.setDebt(new Money(150));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_CURRENCY_TOOLONG_MESSAGE, new Object[] { TransactionsValidator.TRANSACTION_CURRENCY_CODE_LENGTH_LIMIT })
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionDebtNull() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_DEBT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionTotalAmountMoneyObjectNull() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setDebt(new Money(150));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_TOTAL_AMOUNT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionTotalAmountAmountNull() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money());
        debt.setDebt(new Money(150));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_TOTAL_AMOUNT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionTotalAmountGreaterThanZero() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(0));
        debt.setDebt(new Money(150));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_ZERO_TOTAL_AMOUNT_MESSAGE,
                        new Object[]{ TransactionsValidator.TRANSACTION_DEBT_TOTAL_AMOUNT_LOWER_LIMIT }
                )
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionDebtObjectDebtNull() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(200));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_DEBT_AMOUNT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionDebtObjectDebtAmountNull() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(200));
        debt.setDebt(new Money());
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_DEBT_AMOUNT_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionDebtGreaterThanZero() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(200));
        debt.setDebt(new Money(0));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_ZERO_DEBT_MESSAGE, new Object[]{ TransactionsValidator.TRANSACTION_DEBT_LOWER_LIMIT })
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionDebtGreaterThanTotalAmount() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(200));
        debt.setDebt(new Money(300));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.INSERT_NEW_DEBT_DEBT_GREATERTHAN_TOTALAMOUNT)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionNullCreditor() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(300));
        debt.setDebt(new Money(200));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CREDITOR_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionNullCreditorId() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(new User());
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(300));
        debt.setDebt(new Money(200));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CREDITOR_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionNullDebtor() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(300));
        debt.setDebt(new Money(200));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_DEBTOR_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }

    /**
     * TODO Add comments
     */
    @Test
    public void testCreateTransactionNullDebtorId() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);
        transaction.setDebtor(new User());

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(300));
        debt.setDebt(new Money(200));
        transaction.setDebt(debt);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE),
                messageHelper.getMessage(ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_DEBTOR_MESSAGE)
        );

        ErrorResponse actualErrorResponse = transactionsHelper.failCreateTransaction(transaction, HttpStatus.BAD_REQUEST);
        new ErrorAsserter(actualErrorResponse).assertError(expectedErrorResponse);
    }
}
