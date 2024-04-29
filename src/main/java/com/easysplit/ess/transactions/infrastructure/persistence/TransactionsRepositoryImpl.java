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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Date;

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
    public TransactionEntity createTransaction(TransactionEntity transaction, String createdByGuid) {
        if (transaction == null) {
            return null;
        }

        String transactionGuid = UUID.randomUUID().toString();
        Timestamp createdDate = infrastructureHelper.getCurrentDate();

        UserEntity creditor = null;
        UserEntity debtor = null;
        UserEntity createdBy = null;

        try {
            // Throws a NotFoundException if a user or group does not exist
            creditor = userRepository.getUser(transaction.getCreditor().getUserGuid());
            debtor = userRepository.getUser(transaction.getDebtor().getUserGuid());
            createdBy = userRepository.getUser(createdByGuid);

            jdbc.update(TransactionsQueries.INSERT_TRANSACTION,
                    transactionGuid,
                    null, // Transaction set guid
                    transaction.getName(),
                    transaction.getCurrency(),
                    null, // Group guid
                    creditor.getUserGuid(),
                    debtor.getUserGuid(),
                    createdBy.getUserGuid(),
                    createdDate,
                    createdBy.getUserGuid(),
                    createdDate
            );
        } catch (NotFoundException e) {
          throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".createTransaction() - Something went wrong while creating the transaction: " + transaction, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_TRANSACTION_ERROR_TITLE,
                    ErrorKeys.CREATE_TRANSACTION_ERROR_MESSAGE,
                    new Object[]{ transaction },
                    e.getCause()
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
    public void bulkCreateTransaction(List<TransactionEntity> transactions, String groupGuid, String createdByGuid) {
        if (EssUtils.isNullOrEmpty(transactions)) {
            return;
        }

        String transactionSetGuid = UUID.randomUUID().toString();
        Timestamp createdDate = infrastructureHelper.getCurrentDate();
        List<DebtEntity> debts = new ArrayList<>();

        try {
            // Throws a NotFoundException if a user or group does not exist
            UserEntity createdBy = userRepository.getUser(createdByGuid);
            GroupEntity group = groupsRepository.getGroup(groupGuid);

            jdbc.batchUpdate(TransactionsQueries.INSERT_TRANSACTION, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    String transactionGuid = UUID.randomUUID().toString();
                    TransactionEntity transaction = transactions.get(i);

                    // Throws a NotFoundException if a user does not exist
                    UserEntity creditor = userRepository.getUser(transaction.getCreditor().getUserGuid());
                    UserEntity debtor = userRepository.getUser(transaction.getDebtor().getUserGuid());

                    ps.setString(1, transactionGuid);
                    ps.setString(2, transactionSetGuid);
                    ps.setString(3, transaction.getName());
                    ps.setString(4, transaction.getCurrency());
                    ps.setString(5, group.getGroupGuid());
                    ps.setString(6, creditor.getUserGuid());
                    ps.setString(7, debtor.getUserGuid());
                    ps.setString(8, createdBy.getUserGuid());
                    ps.setTimestamp(9, createdDate);
                    ps.setString(10, createdBy.getUserGuid());
                    ps.setTimestamp(11, createdDate);

                    DebtEntity debt = transaction.getDebt();
                    debt.setTransactionGuid(transactionGuid);
                    debts.add(debt);
                }

                @Override
                public int getBatchSize() {
                    return transactions.size();
                }
            });

            bulkCreateDebts(debts, createdBy, createdDate);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".bulkCreateTransaction() - Something went wrong while creating set of transactions", e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.BULK_CREATE_TRANSACTION_ERROR_TITLE,
                    ErrorKeys.BULK_CREATE_TRANSACTION_ERROR_MESSAGE,
                    null,
                    e.getCause()
            );
        }
    }

    @Override
    @Transactional
    public void bulkCreateDebts(List<DebtEntity> debts, UserEntity createdBy, Timestamp createdDate) {
        if (EssUtils.isNullOrEmpty(debts)) {
            return;
        }

        try {
            jdbc.batchUpdate(DebtsQueries.INSERT_DEBT, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    String debtGuid = UUID.randomUUID().toString();

                    DebtEntity debt = debts.get(i);
                    ps.setString(1, debtGuid);
                    ps.setString(2, debt.getTransactionGuid());
                    ps.setBigDecimal(3, debt.getTotalAmount());
                    ps.setBigDecimal(4, debt.getDebt());
                    ps.setBoolean(5, false);
                    ps.setInt(6, 1);
                    ps.setString(7, createdBy.getUserGuid());
                    ps.setTimestamp(8, createdDate);
                }

                @Override
                public int getBatchSize() {
                    return debts.size();
                }
            });
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".bulkCreateDebts() - Something went wrong while creating set of debts", e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.BULK_INSERT_NEW_DEBT_ERROR_TITLE,
                    ErrorKeys.BULK_INSERT_NEW_DEBT_ERROR_MESSAGE,
                    null,
                    e.getCause()
            );
        }

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
                    e.getCause()
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
                    e.getCause()
            );
        }

        return revision;
    }

    @Override
    public TransactionEntity getTransaction(String transactionGuid){
        TransactionEntity transactionEntity = null;

        try{
            transactionEntity = jdbc.query(TransactionsQueries.GET_TRANSACTION,
                    this::toTransactionEntity,
                    transactionGuid);
        }catch(NotFoundException e){
            //Catching NotFoundException thrown from toTransactionEntity method
            logger.debug(CLASS_NAME + ".getTransaction() - NotFoundException while reading the transaction: " + transactionGuid, e);
            throw e;
        }catch(Exception e){
            logger.error(CLASS_NAME + ".getTransaction() - Something went wrong while reading the transaction with id: "+ transactionGuid, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.GET_TRANSACTION_ERROR_TITLE,
                    ErrorKeys.GET_TRANSACTION_ERROR_MESSAGE,
                    new Object[] {transactionGuid},
                    e.getCause()
            );
        }

        return transactionEntity;
    }

    @Override
    public DebtEntity getDebt(String transactionGuid){
        DebtEntity debtEntity = null;

        try{
            debtEntity = jdbc.query(DebtsQueries.GET_DEBT,
                    this::toDebtEntity,
                    transactionGuid);
        }catch(NotFoundException e){
            //Catching NotFoundException thrown from toDebtEntity method
            logger.debug(CLASS_NAME + ".getDebt() - NotFoundException while reading the debt of the transaction: " + transactionGuid, e);
            throw e;
        }catch(Exception e){
            logger.error(CLASS_NAME + ".getDebt() - Something went wrong while reading the debt of the transaction with id: "+ transactionGuid, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.GET_DEBT_ERROR_TITLE,
                    ErrorKeys.GET_DEBT_ERROR_MESSAGE,
                    new Object[] {transactionGuid},
                    e.getCause()
            );
        }

        return debtEntity;

    }

    private TransactionEntity toTransactionEntity(ResultSet rs) throws SQLException {

        TransactionEntity transactionEntity = null;

        if(rs.next()) {
            transactionEntity = new TransactionEntity();

            transactionEntity.setTransactionGuid(rs.getString(TransactionsQueries.TRANSACTIONGUID_COLUMN.toLowerCase()));
            transactionEntity.setName(rs.getString(TransactionsQueries.NAME_COLUMN.toLowerCase()));

            String creditorByGuid = rs.getString(TransactionsQueries.CREDITOR_COLUMN.toLowerCase());
            UserEntity creditor = userRepository.getUser(creditorByGuid);
            transactionEntity.setCreditor(creditor);

            String debtorByGuid = rs.getString(TransactionsQueries.DEBTOR_COLUMN.toLowerCase());
            UserEntity debtor = userRepository.getUser(debtorByGuid);
            transactionEntity.setDebtor(debtor);


            String createdByGuid = rs.getString((TransactionsQueries.CREATED_BY_COLUMN.toLowerCase()));
            UserEntity createdBy = userRepository.getUser(createdByGuid);
            transactionEntity.setCreatedBy(createdBy);

            String date = rs.getString((TransactionsQueries.CREATED_DATE_COLUMN.toLowerCase()));
            Timestamp createdDate = Timestamp.valueOf(date);
            transactionEntity.setCreatedDate(createdDate);

            String updatedByGuid = rs.getString((TransactionsQueries.UPDATED_BY_COLUMN.toLowerCase()));
            UserEntity updatedBy = userRepository.getUser(updatedByGuid);
            transactionEntity.setUpdatedBy(updatedBy);


            Date currentDate = new Date();
            Timestamp updatedDate = new Timestamp(currentDate.getTime());
            transactionEntity.setUpdatedDate(updatedDate);
        }


        return transactionEntity;
    }

    private DebtEntity toDebtEntity(ResultSet rs) throws SQLException{

        DebtEntity debtEntity = null;

        if(rs.next()){
            debtEntity = new DebtEntity();

            debtEntity.setDebtGuid(rs.getString(DebtsQueries.DEBTGUID_COLUMN.toLowerCase()));
            debtEntity.setTotalAmount(rs.getBigDecimal(DebtsQueries.TOTAL_AMOUNT_COLUMN.toLowerCase()));
            debtEntity.setDebt(rs.getBigDecimal(DebtsQueries.DEBT_COLUMN.toLowerCase()));
            debtEntity.setDebtSettled(rs.getBoolean(DebtsQueries.DEBT_SETTLED_COLUMN.toLowerCase()));
            debtEntity.setRevision(rs.getInt(DebtsQueries.REVISION_COLUMN.toLowerCase()));

            String createdByGuid = rs.getString((TransactionsQueries.CREATED_BY_COLUMN.toLowerCase()));
            UserEntity createdBy = userRepository.getUser(createdByGuid);
            debtEntity.setCreatedBy(createdBy);

            debtEntity.setCreatedDate(rs.getTimestamp(DebtsQueries.CREATED_DATE_COLUMN.toLowerCase()));

        }
        return debtEntity;
    }
}
