package com.easysplit.ess.transactions.domain.models;

import com.easysplit.ess.groups.domain.models.GroupEntity;
import com.easysplit.ess.user.domain.models.UserEntity;

import java.sql.Timestamp;

/**
 * Class that represents the table <i>transactions</i>
 */
public class TransactionEntity {
    private String transactionGuid;
    private String name;
    private String currency;
    private DebtEntity debt;
    private GroupEntity group;
    private UserEntity creditor;
    private UserEntity debtor;
    private UserEntity createdBy;
    private Timestamp createdDate;
    private UserEntity updatedBy;
    private Timestamp updatedDate;

    public TransactionEntity() {

    }

    public String getTransactionGuid() {
        return transactionGuid;
    }

    public void setTransactionGuid(String transactionGuid) {
        this.transactionGuid = transactionGuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public DebtEntity getDebt() {
        return debt;
    }

    public void setDebt(DebtEntity debt) {
        this.debt = debt;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public UserEntity getCreditor() {
        return creditor;
    }

    public void setCreditor(UserEntity creditor) {
        this.creditor = creditor;
    }

    public UserEntity getDebtor() {
        return debtor;
    }

    public void setDebtor(UserEntity debtor) {
        this.debtor = debtor;
    }

    public UserEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserEntity createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public UserEntity getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UserEntity updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "transactionGuid : " + this.transactionGuid + " | "
                + "name : " + this.name + " | "
                + "debt : " + this.debt + " | "
                + "group : " + this.group + " | "
                + "creditor : " + this.creditor + " | "
                + "debtor : " + this.debtor + " | "
                + "createdBy : " + this.createdBy + " | "
                + "createdDate : " + this.createdDate + " | "
                + "updatedBy : " + this.updatedBy + " | "
                + "updatedDate : " + this.updatedDate;
    }

    /**
     * Generates a transaction from this entity class
     * @return equivalent transaction
     */
    public Transaction toTransaction() {
        Transaction transaction = TransactionMapper.INSTANCE.toTransaction(this);

        transaction.setDebt(this.debt.toDebt());
        transaction.setGroup(this.group.toGroup());
        transaction.setCreditor(this.creditor.toUser());
        transaction.setDebtor(this.debtor.toUser());
        transaction.setCreatedBy(this.createdBy.toUser());
        transaction.setUpdatedBy(this.updatedBy.toUser());

        return transaction;
    }
}
