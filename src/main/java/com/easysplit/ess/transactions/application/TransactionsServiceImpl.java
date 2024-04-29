package com.easysplit.ess.transactions.application;

import com.easysplit.ess.transactions.domain.contracts.DebtsRepository;
import com.easysplit.ess.transactions.domain.contracts.TransactionsRepository;
import com.easysplit.ess.transactions.domain.contracts.TransactionsService;
import com.easysplit.ess.transactions.domain.models.DebtEntity;
import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.ess.transactions.domain.models.TransactionEntity;
import com.easysplit.ess.transactions.domain.validators.TransactionsValidator;
import com.easysplit.shared.utils.EssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionsServiceImpl implements TransactionsService {
    private final TransactionsRepository transactionsRepository;
    private final DebtsRepository debtsRepository;
    private final TransactionsValidator transactionsValidator;

    @Autowired
    public TransactionsServiceImpl(TransactionsRepository transactionsRepository,
                                   DebtsRepository debtsRepository,
                                   TransactionsValidator transactionsValidator) {
        this.transactionsRepository = transactionsRepository;
        this.debtsRepository = debtsRepository;
        this.transactionsValidator = transactionsValidator;
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
        transactionsRepository.bulkCreateTransaction(toTransactionEntities(transactions), groupId, createdById);
    }

    @Override
    public Transaction getTransaction(String transactionGuid){

        TransactionEntity transaction = transactionsRepository.getTransaction(transactionGuid);

        DebtEntity debt = transactionsRepository.getDebt(transactionGuid);
        transaction.setDebt(debt);

        return transaction.toTransaction();
    }

    private List<TransactionEntity> toTransactionEntities(List<Transaction> transactions) {
        List<TransactionEntity> transactionEntities = new ArrayList<>();

        if (EssUtils.isNullOrEmpty(transactions)) {
            return transactionEntities;
        }

        for (Transaction transaction: transactions) {
            transactionEntities.add(transaction.toTransactionEntity());
        }

        return transactionEntities;
    }
}
