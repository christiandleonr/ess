package com.easysplit.ess.transactions.domain.contracts;

import com.easysplit.ess.transactions.domain.models.DebtEntity;
import com.easysplit.ess.transactions.domain.models.TransactionEntity;

import java.util.List;

/**
 * Class that handle the database operations for the transactions resource
 */
public interface TransactionsRepository {
    /**
     * Creates a new transaction between two users,
     * the transaction could belong to a group too (when the field <i>groupGuid</i> is not empty),
     * but it's always represented as a transaction between two user.
     *
     * @param transactionEntity transaction
     * @param createdByGuid id of the user who is creating the transaction
     * @return transaction created
     */
    TransactionEntity createTransaction(TransactionEntity transactionEntity, String createdByGuid);

    /**
     * Gets a transaction by guid
     *
     * @param transactionGuid transaction id
     * @param throwException whether to throw a NotFoundException or not
     * @return transactionEntity
     */
    TransactionEntity getTransaction(String transactionGuid, boolean throwException);

    /**
     * Delete transaction by guid.
     *
     * @param transactionGuid the transaction id
     */
    void deleteTransaction(String transactionGuid);

    /**
     * Gets a debt by the transaction guid
     *
     * @param transactionGuid transaction id
     * @return debtEntity
     */
    DebtEntity getDebt(String transactionGuid);
}
