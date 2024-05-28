package com.easysplit.ess.transactions.asserters;

import com.easysplit.ess.transactions.domain.models.Transaction;
import org.junit.jupiter.api.Assertions;

import java.util.Comparator;
import java.util.List;

public class TransactionsListAsserter {
    private static Comparator<Transaction> getComparator() {
        return Comparator.comparing(Transaction::getName);
    }

    public static void assertListEquals(List<Transaction> expected, List<Transaction> actual) {
        List<Transaction> sortedActual = actual.stream().sorted(getComparator()).toList();
        List<Transaction> sortedExpected = expected.stream().sorted(getComparator()).toList();

        int actualSize = sortedActual.size();
        int expectedSize = sortedExpected.size();

        Assertions.assertEquals(actualSize, expectedSize, "The size of the expected list and actual list do not match");
        for (int i=0; i<actualSize; i++) {
            new TransactionsAsserter(sortedActual.get(i)).assertTransaction(sortedExpected.get(i));
        }
    }
}
