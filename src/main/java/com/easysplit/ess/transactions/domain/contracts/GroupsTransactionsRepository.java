package com.easysplit.ess.transactions.domain.contracts;

import com.easysplit.ess.transactions.domain.models.TransactionEntity;

import java.util.List;

/**
 * Interface that represents the database operation for group's transactions
 */
public interface GroupsTransactionsRepository {
    /**
     * Takes a list of transactions and perform a bulk insert.
     *
     * @param transactions transactions to be created
     * @param groupGuid group to which the transaction belong
     * @param createdByGuid user who started the transaction
     */
    void bulkCreateTransactions(List<TransactionEntity> transactions, String groupGuid, String createdByGuid);

    /**
     * Count transactions by group guid
     *
     * @param groupGuid group guid to get the count for
     * @return total number of transactions for the given group
     */
    int countTransactionsByGroup(String groupGuid);

    /**
     * Loads transactions by group guid, also loads debt data
     *
     * @param groupGuid group id to get the transactions for
     * @param limit limit of transactions to retrieve
     * @param offset from which number start retrieving
     * @return list of transactions
     */
    List<TransactionEntity> loadTransactionsByGroup(String groupGuid, int limit, int offset);
}
