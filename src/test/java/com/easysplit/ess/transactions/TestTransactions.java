package com.easysplit.ess.transactions;

import com.easysplit.ess.transactions.asserters.TransactionsAsserter;
import com.easysplit.ess.transactions.domain.models.Debt;
import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.ess.transactions.utils.TestTransactionsHelper;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.users.builders.UserBuilder;
import com.easysplit.ess.users.utils.TestUsersHelper;
import com.easysplit.shared.domain.models.Money;
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
public class TestTransactions {
    @Autowired
    private TestTransactionsHelper transactionsHelper;

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

    /**
     * Proves that a transaction can be created and also deleted using its id
     */
    @Test
    public void testCreateAndDeleteTransaction() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(200));
        debt.setDebt(new Money(150));
        transaction.setDebt(debt);

        Transaction createdTransaction = transactionsHelper.createTransaction(transaction, HttpStatus.CREATED);

        debt.setDebtSettled(false); // we expect this attribute to be false
        debt.setRevision(1); // since this is a new transaction the revision must be the number 1
        new TransactionsAsserter(createdTransaction).assertTransaction(transaction);

        transactionsHelper.deleteTransaction(createdTransaction.getId());
    }

    /**
     * Proves that a transaction can be created, retrieved and deleted using its id
     */
    @Test
    public void testCreateGetAndDeleteTransaction() {
        String uniqueString = TestUtils.generateUniqueString();

        Transaction transaction = new Transaction();
        transaction.setName("Transaction-" + uniqueString);
        transaction.setCurrency(Currency.getInstance("MXN"));
        transaction.setCreditor(user1);
        transaction.setDebtor(user2);

        Debt debt = new Debt();
        debt.setTotalAmount(new Money(200));
        debt.setDebt(new Money(150));
        transaction.setDebt(debt);

        String createdTransactionId = transactionsHelper.createTransaction(transaction, HttpStatus.CREATED).getId();
        Transaction retrievedTransaction = transactionsHelper.getTransaction(createdTransactionId, Transaction.class, HttpStatus.OK);

        debt.setDebtSettled(false); // we expect this attribute to be false
        debt.setRevision(1); // since this is a new transaction the revision must be the number 1
        new TransactionsAsserter(retrievedTransaction).assertTransaction(transaction);

        transactionsHelper.deleteTransaction(createdTransactionId);
    }
}
