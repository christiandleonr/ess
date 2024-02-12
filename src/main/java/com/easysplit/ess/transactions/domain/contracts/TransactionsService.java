package com.easysplit.ess.transactions.domain.contracts;

import com.easysplit.ess.transactions.domain.models.Transaction;

/**
 * Class that handle the business logic for the user resource
 */
public interface TransactionsService {
    /**
     * Creates a new transaction between two users, perform pure
     * data validations
     *
     * @param transaction transaction to be created
     * @param createdById id of the user who is creating the transaction
     * @return created transaction
     */
    Transaction createNewTransaction(Transaction transaction, String createdById);

    /**
     * Gets a Transaction by its guid
     *
     * @param transactionGuid transaction id
     * @return transaction
     */
    Transaction getTransaction(String transactionGuid);
}
