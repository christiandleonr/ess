package com.easysplit.ess.transactions.infrastructure.persistence;

import com.easysplit.ess.groups.domain.contracts.GroupsRepository;
import com.easysplit.ess.groups.domain.models.GroupEntity;
import com.easysplit.ess.transactions.domain.contracts.DebtsRepository;
import com.easysplit.ess.transactions.domain.contracts.TransactionsRepository;
import com.easysplit.ess.transactions.domain.models.DebtEntity;
import com.easysplit.ess.transactions.domain.models.TransactionEntity;
import com.easysplit.ess.transactions.domain.sql.DebtsQueries;
import com.easysplit.ess.transactions.domain.sql.TransactionsQueries;
import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.NotFoundException;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import com.easysplit.shared.utils.EssUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.UUID;

@Repository
public class TransactionsRepositoryImpl implements TransactionsRepository, DebtsRepository {
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

        try {
            // Throws a NotFoundException if a user or group does not exist
            creditor = userRepository.getUser(transaction.getCreditor().getUserGuid());
            debtor = userRepository.getUser(transaction.getDebtor().getUserGuid());
            createdBy = userRepository.getUser(transaction.getCreatedBy().getUserGuid());

            jdbc.update(TransactionsQueries.INSERT_TRANSACTION,
                    transactionGuid,
                    transaction.getName(),
                    transaction.getCurrency(),
                    null,
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
        transaction.setCreatedBy(createdBy);
        transaction.setCreatedDate(createdDate);
        transaction.setUpdatedBy(createdBy);
        transaction.setUpdatedDate(createdDate);

        return transaction;
    }

    @Override
    @Transactional
    public DebtEntity insertNewDebt(DebtEntity debt, String transactionGuid, UserEntity createdBy) {
        String debtGuid = debt.getDebtGuid();
        int revision = 0;

        // Creates new guid when there is no debt guid from a previously created debt
        if (EssUtils.isNullOrEmpty(debtGuid)) {
            debtGuid = UUID.randomUUID().toString();
            revision = 1; // The revision starts from 1
        } else {
            revision = getLastRevision(debtGuid) + 1;
        }
        Timestamp createdDate = infrastructureHelper.getCurrentDate();

        try {
            jdbc.update(DebtsQueries.INSERT_DEBT,
                    debtGuid,
                    transactionGuid,
                    debt.getTotalAmount(),
                    debt.getDebt(),
                    debt.isDebtSettled(),
                    revision,
                    createdBy.getUserGuid(),
                    createdDate
                    );
        } catch (NotFoundException e) {
            logger.info(CLASS_NAME + ".insertNewDebt() - Created by user not found " + debt.getCreatedBy().getUserGuid());
            throw e;
        } catch (Exception e) {
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.INSERT_NEW_DEBT_ERROR_TITLE,
                    ErrorKeys.INSERT_NEW_DEBT_ERROR_MESSAGE,
                    new Object[]{ debtGuid },
                    e
            );
        }

        debt.setDebtGuid(debtGuid);
        debt.setRevision(revision);
        debt.setCreatedBy(createdBy);
        debt.setCreatedDate(createdDate);

        return debt;
    }

    @Override
    public int getLastRevision(String debtGuid) {
        if (EssUtils.isNullOrEmpty(debtGuid)) {
            return 0;
        }

        int revision = 0;
        try {
            revision = jdbc.queryForObject(DebtsQueries.GET_LAST_REVISION, Integer.class);
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".getLastRevision() - Something went wrong while reading the last revision of the debt " + debtGuid);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.INSERT_NEW_DEBT_ERROR_TITLE,
                    ErrorKeys.READ_LAST_REVISION_ERROR_MESSAGE,
                    new Object[]{ debtGuid },
                    e
            );
        }

        return revision;
    }
}
