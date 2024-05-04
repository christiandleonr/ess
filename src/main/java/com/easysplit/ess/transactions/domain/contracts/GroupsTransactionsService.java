package com.easysplit.ess.transactions.domain.contracts;

import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.shared.domain.models.ResourceList;

import java.util.List;

/**
 * Contract that holds the method that manage the business logic for the groups resource
 */
public interface GroupsTransactionsService {
    /**
     * List transactions by group
     *
     * @param groupId group to retrieve the transactions for
     * @return list of transactions for the given group
     */
    ResourceList<Transaction> listTransactionsByGroup(String groupId, int limit, int offset, boolean countTransactions);

    /**
     * Creates a set of transactions related to a group, performs pure data validation
     *
     * @param transactions transactions to be created
     * @param groupId group to which the transaction belong
     * @param createdById user who started the transaction
     */
    void bulkCreateTransaction(List<Transaction> transactions, String groupId, String createdById);
}
