package com.easysplit.ess.transactions.application;

import com.easysplit.ess.groups.domain.contracts.GroupsRepository;
import com.easysplit.ess.transactions.domain.contracts.*;
import com.easysplit.ess.transactions.domain.models.DebtEntity;
import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.ess.transactions.domain.models.TransactionEntity;
import com.easysplit.ess.transactions.domain.validators.TransactionsValidator;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.helpers.DomainHelper;
import com.easysplit.shared.domain.models.ResourceList;
import com.easysplit.shared.utils.EssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionsServiceImpl implements TransactionsService, GroupsTransactionsService {
    private final TransactionsRepository transactionsRepository;
    private final GroupsRepository groupsRepository;
    private final GroupsTransactionsRepository groupsTransactionsRepository;
    private final DebtsRepository debtsRepository;
    private final TransactionsValidator transactionsValidator;
    private final DomainHelper domainHelper;

    @Autowired
    public TransactionsServiceImpl(TransactionsRepository transactionsRepository,
                                   GroupsRepository groupsRepository,
                                   GroupsTransactionsRepository groupsTransactionsRepository,
                                   DebtsRepository debtsRepository,
                                   TransactionsValidator transactionsValidator,
                                   DomainHelper domainHelper) {
        this.transactionsRepository = transactionsRepository;
        this.groupsRepository = groupsRepository;
        this.groupsTransactionsRepository = groupsTransactionsRepository;
        this.debtsRepository = debtsRepository;
        this.transactionsValidator = transactionsValidator;
        this.domainHelper = domainHelper;
    }

    @Override
    @Transactional
    public Transaction createNewTransaction(Transaction transaction, String createdById) {
        transactionsValidator.validate(transaction);

        TransactionEntity createdTransaction = transactionsRepository.createTransaction(
                transaction.toTransactionEntity(),
                createdById
        );
        DebtEntity createdDebt = debtsRepository.insertNewDebt(
                createdTransaction.getDebt(),
                createdTransaction.getTransactionGuid(),
                createdTransaction.getCreatedBy()
        );

        createdTransaction.setDebt(createdDebt);

        return createdTransaction.toTransaction();
    }

    @Override
    public void bulkCreateTransaction(List<Transaction> transactions, String groupId, String createdById) {
        transactionsValidator.validate(transactions);
        groupsTransactionsRepository.bulkCreateTransactions(toTransactionEntities(transactions), groupId, createdById);
    }

    @Override
    public Transaction getTransaction(String transactionGuid){
        TransactionEntity transaction = transactionsRepository.getTransaction(transactionGuid, true);

        DebtEntity debt = transactionsRepository.getDebt(transactionGuid);
        transaction.setDebt(debt);

        return transaction.toTransaction();
    }

    @Override
    public void deleteTransaction(String transactionId, String authenticatedUserId) {
        TransactionEntity transaction = transactionsRepository.getTransaction(transactionId, true);

        if (!transaction.getCreatedBy().getUserGuid().equals(authenticatedUserId)) {
            domainHelper.throwUnauthorizedException(
                    ErrorKeys.DELETE_TRANSACTION_UNAUTHORIZED_TITLE,
                    ErrorKeys.DELETE_TRANSACTION_CREATED_BY_REQUIRED,
                    null
            );
        }

        debtsRepository.deleteDebts(transactionId);
        transactionsRepository.deleteTransaction(transactionId);
    }

    @Override
    public ResourceList<Transaction> listTransactionsByGroup(String groupId, int limit, int offset, boolean countTransactions) {
        ResourceList<Transaction> transactionList = new ResourceList<>();

        // Throws exception if the group do not exist
        groupsRepository.getGroup(groupId);

        int totalCount = 0;
        if (countTransactions) {
            totalCount = groupsTransactionsRepository.countTransactionsByGroup(groupId);
        }

        List<TransactionEntity> transactionsEntities = groupsTransactionsRepository.loadTransactionsByGroup(groupId, limit, offset);
        int count = transactionsEntities.size();

        transactionList.setLimit(limit);
        transactionList.setOffset(offset);
        transactionList.setCount(count);
        transactionList.setTotalCount(totalCount);
        transactionList.setHasMore(count > limit);
        transactionList.setData(toTransactions(transactionsEntities));

        return transactionList;
    }

    /**
     * Take a list of transactions and its entity form
     *
     * @param transactions transactions to be converted to transaction entities
     * @return list of transactions entities
     */
    private List<TransactionEntity> toTransactionEntities(List<Transaction> transactions) {
        List<TransactionEntity> transactionsEntities = new ArrayList<>();

        if (EssUtils.isNullOrEmpty(transactions)) {
            return transactionsEntities;
        }

        for (Transaction transaction: transactions) {
            transactionsEntities.add(transaction.toTransactionEntity());
        }

        return transactionsEntities;
    }

    /**
     * Generates a list of transactions from a list of transaction entities
     *
     * @param transactionsEntities transactions entities
     * @return list of transactions
     */
    private List<Transaction> toTransactions(List<TransactionEntity> transactionsEntities) {
        List<Transaction> transactions = new ArrayList<>();

        if (EssUtils.isNullOrEmpty(transactionsEntities)) {
            return transactions;
        }

        for (TransactionEntity transactionEntity: transactionsEntities) {
            transactions.add(transactionEntity.toTransaction());
        }

        return transactions;
    }
}
