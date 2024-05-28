package com.easysplit.ess.transactions.utils;

import com.easysplit.ess.transactions.domain.models.Debt;
import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.models.Money;
import com.easysplit.shared.utils.TestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * Class with utility methods for testing
 */
public class TestTransactionsUtils {
    /**
     * Private constructor to prevent instantiation of the class.
     * This class should only hold utility methods and should not be instantiated
     */
    private TestTransactionsUtils() {}

    /**
     * TODO Add comments
     * @param members
     * @param creditor
     * @param totalAmount
     * @return
     */
    public static List<Transaction> divideTransactionEqually(List<User> members, User creditor, Money totalAmount) {
        List<Transaction> transactions = new ArrayList<>();

        // The size of the resulting array is equals to the number of people
        BigDecimal[] amountsDividedEqually = totalAmount.divideEqually(members.size());

        int i=0;
        for (User member: members) {
            String uniqueString = TestUtils.generateUniqueString();

            Transaction transaction = new Transaction();
            transaction.setName(member.getName() + "'s-Transaction-" + uniqueString);
            transaction.setCurrency(Currency.getInstance("MXN"));
            transaction.setCreditor(creditor);
            transaction.setDebtor(member);

            Debt debt = new Debt();
            debt.setTotalAmount(totalAmount);
            debt.setDebt(new Money(amountsDividedEqually[i++]));
            transaction.setDebt(debt);

            transactions.add(transaction);
        }

        return transactions;
    }
}
