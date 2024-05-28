package com.easysplit.ess.users.asserters;

import com.easysplit.ess.user.domain.models.User;
import org.junit.jupiter.api.Assertions;

import java.util.Comparator;
import java.util.List;

public class UsersListAsserter {
    private static Comparator<User> getComparator() {
        return Comparator.comparing(User::getUsername);
    }

    public static void assertListEquals(List<User> expected, List<User> actual) {
        List<User> sortedActual = actual.stream().sorted(getComparator()).toList();
        List<User> sortedExpected = expected.stream().sorted(getComparator()).toList();

        int actualSize = sortedActual.size();
        int expectedSize = sortedExpected.size();

        Assertions.assertEquals(actualSize, expectedSize, "The size of the expected list and actual list do not match");
        for (int i=0; i<actualSize; i++) {
            new UserAsserter(sortedActual.get(i)).assertUser(sortedExpected.get(i));
        }
    }
}
