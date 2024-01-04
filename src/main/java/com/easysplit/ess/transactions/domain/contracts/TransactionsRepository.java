package com.easysplit.ess.transactions.domain.contracts;

import com.easysplit.ess.transactions.domain.models.DebtEntity;
import com.easysplit.ess.transactions.domain.models.TransactionEntity;

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
     * @return transaction created
     */
    TransactionEntity createTransaction(TransactionEntity transactionEntity);

    /**
     *Gets a transaction by is guid
     *
     * @param transactionGuid transaction id
     * @return transactionEntity
     */
    TransactionEntity getTransaction(String transactionGuid);

    /**
     *Gets a debt by the transaction guid
     *
     * @param transactionGuid transaction id
     * @return debtEntity
     */
    DebtEntity getDebt(String transactionGuid);
}
