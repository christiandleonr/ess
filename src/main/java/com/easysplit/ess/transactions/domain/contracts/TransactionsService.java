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
     * @param transactionGuid transaction id
     * @return transaction
     */
    Transaction getTransaction(String transactionGuid);
}
