package com.easysplit.ess.transactions.domain.contracts;

import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.shared.domain.models.ResourceList;

import java.util.List;

/**
 * Class that handle the business logic for the transactions resource
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
     * Gets a Transaction by its guid
     *
     * @param transactionId transaction id
     * @return transaction
     */
    Transaction getTransaction(String transactionId);

    /**
     * Delete a transaction by its id.
     * Throws an <i>IllegalArgumentException</i> if the user who wants to delete the transaction
     * is not the same as the user who created it.
     *
     * @param transactionId the transaction id
     * @param authenticatedUserId user who is trying to delete the transaction
     */
    void deleteTransaction(String transactionId, String authenticatedUserId);
}
