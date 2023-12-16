package com.easysplit.ess.transactions.infrastructure.persistence;

import com.easysplit.ess.groups.domain.contracts.GroupsRepository;
import com.easysplit.ess.groups.domain.models.GroupEntity;
import com.easysplit.ess.transactions.domain.contracts.TransactionsRepository;
import com.easysplit.ess.transactions.domain.models.DebtEntity;
import com.easysplit.ess.transactions.domain.models.TransactionEntity;
import com.easysplit.ess.transactions.domain.sql.TransactionsQueries;
import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import com.easysplit.shared.utils.EssUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.UUID;

public class TransactionsRepositoryImpl implements TransactionsRepository {
    private static final String CLASS_NAME = TransactionsRepositoryImpl.class.getName();
    private final InfrastructureHelper infrastructureHelper;
    private final UserRepository userRepository;
    private final GroupsRepository groupsRepository;
    private final JdbcTemplate jdbc;
    private static final Logger logger = LoggerFactory.getLogger(TransactionsRepositoryImpl.class);

    @Autowired
    public TransactionsRepositoryImpl(InfrastructureHelper infrastructureHelper,
                                      UserRepository userRepository,
                                      GroupsRepository groupsRepository,
                                      JdbcTemplate jdbc) {
        this.infrastructureHelper = infrastructureHelper;
        this.userRepository = userRepository;
        this.groupsRepository = groupsRepository;
        this.jdbc = jdbc;
    }

    @Override
    @Transactional
    public TransactionEntity createTransaction(TransactionEntity transaction) {
        String transactionGuid = UUID.randomUUID().toString();
        Timestamp createdDate = infrastructureHelper.getCurrentDate();

        UserEntity creditor = null;
        UserEntity debtor = null;
        UserEntity createdBy = null;
        GroupEntity group = null;

        try {
            // Throws a NotFoundException if the user or group does not exist
            creditor = userRepository.getUser(transaction.getCreditor().getUserGuid());
            debtor = userRepository.getUser(transaction.getDebtor().getUserGuid());
            createdBy = userRepository.getUser(transaction.getCreatedBy().getUserGuid());
            group = groupsRepository.getGroup(transaction.getGroup().getGroupGuid());

            jdbc.update(TransactionsQueries.INSERT_TRANSACTION,
                    transactionGuid,
                    transaction.getName(),
                    group.getGroupGuid(),
                    creditor.getUserGuid(),
                    debtor.getUserGuid(),
                    createdBy.getUserGuid(),
                    createdDate,
                    createdBy.getUserGuid(),
                    createdDate
            );
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".createTransaction() - Something went wrong while creating the transaction: " + transaction, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_TRANSACTION_ERROR_TITLE,
                    ErrorKeys.CREATE_TRANSACTION_ERROR_MESSAGE,
                    new Object[]{ transaction },
                    e
            );
        }

        transaction.setTransactionGuid(transactionGuid);
        transaction.setCreditor(creditor);
        transaction.setDebtor(debtor);
        transaction.setGroup(group);
        transaction.setCreatedBy(createdBy);
        transaction.setCreatedDate(createdDate);
        transaction.setUpdatedBy(createdBy);
        transaction.setUpdatedDate(createdDate);

        return transaction;
    }

    @Override
    public DebtEntity insertNewDebt(DebtEntity debt, String transactionGuid) {
        String debtGuid = debt.getDebtGuid();
        int revision = 0;

        // Creates new guid when there is no debt guid from a previously created debt
        if (EssUtils.isNullOrEmpty(debtGuid)) {
            debtGuid = UUID.randomUUID().toString();
            revision = 1; // The revision starts from 1
        } else {
            // TODO work on get the last revision
        }
        Timestamp createdDate = infrastructureHelper.getCurrentDate();

        return debt;
    }
}
