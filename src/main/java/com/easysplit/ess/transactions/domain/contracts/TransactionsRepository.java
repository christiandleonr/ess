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
     * Takes a list of transactions and perform a bulk insert.
     *
     * @param transactions transactions to be created
     * @param groupGuid group to which the transaction belong
     * @param createdByGuid user who started the transaction
     */
    void bulkCreateTransaction(List<TransactionEntity> transactions, String groupGuid, String createdByGuid);

    /**
     * Gets a transaction by is guid
     *
     * @param transactionGuid transaction id
     * @return transactionEntity
     */
    TransactionEntity getTransaction(String transactionGuid);

    /**
     * Gets a debt by the transaction guid
     *
     * @param transactionGuid transaction id
     * @return debtEntity
     */
    DebtEntity getDebt(String transactionGuid);
}
