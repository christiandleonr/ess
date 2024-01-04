package com.easysplit.ess.transactions.application;

import com.easysplit.ess.transactions.domain.contracts.DebtsRepository;
import com.easysplit.ess.transactions.domain.contracts.TransactionsRepository;
import com.easysplit.ess.transactions.domain.contracts.TransactionsService;
import com.easysplit.ess.transactions.domain.models.DebtEntity;
import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.ess.transactions.domain.models.TransactionEntity;
import com.easysplit.ess.transactions.domain.validators.TransactionsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Transaction createNewTransaction(Transaction transaction) {
        transactionsValidator.validate(transaction);

        TransactionEntity createdTransaction = transactionsRepository.createTransaction(transaction.toTransactionEntity());
        DebtEntity createdDebt = debtsRepository.insertNewDebt(createdTransaction.getDebt(), createdTransaction.getTransactionGuid(), createdTransaction.getCreatedBy());

        createdTransaction.setDebt(createdDebt);

        return createdTransaction.toTransaction();
    }

    public Transaction getTransaction(String transactionGuid){

        TransactionEntity transaction = transactionsRepository.getTransaction(transactionGuid);

        DebtEntity debt = transactionsRepository.getDebt(transactionGuid);
        transaction.setDebt(debt);

        return transaction.toTransaction();
    }

}
