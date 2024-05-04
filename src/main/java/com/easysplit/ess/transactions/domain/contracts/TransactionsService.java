package com.easysplit.ess.transactions.domain.contracts;

import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.shared.domain.models.ResourceList;

import java.util.List;

/**
 * Class that handle the business logic for the user resource
 */
public interface TransactionsService {
    /**
     * Creates a new transaction between two users, performs pure
     * data validations
     *
     * @param transaction transaction to be created
     * @param createdById id of the user who is creating the transaction
     * @return created transaction
     */
    Transaction createNewTransaction(Transaction transaction, String createdById);


    /**
     * Creates a set of transactions related to a group, performs pure data validation
     *
     * @param transactions transactions to be created
     * @param groupId group to which the transaction belong
     * @param createdById user who started the transaction
     */
    void bulkCreateTransaction(List<Transaction> transactions, String groupId, String createdById);

    /**
     * Gets a Transaction by its guid
     *
     * @param transactionGuid transaction id
     * @return transaction
     */
    Transaction getTransaction(String transactionGuid);

    /**
     * List transactions by group
     *
     * @param groupId group to retrieve the transactions for
     * @return list of transactions for the given group
     */
    ResourceList<Transaction> listTransactionsByGroup(String groupId, int limit, int offset, boolean countTransactions);
}
