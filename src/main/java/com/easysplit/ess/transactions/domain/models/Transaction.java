package com.easysplit.ess.transactions.domain.models;

import com.easysplit.ess.groups.domain.models.Group;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.models.Link;

import java.sql.Timestamp;
import java.util.Currency;
import java.util.List;

/**
 * Transaction object to be serialized
 */
public class Transaction {
    private String id;
    private String name;
    private Currency currency;
    private Debt debt;
    private Group group;
    private User creditor;
    private User debtor;
    private User createdBy;
    private Timestamp createdDate;
    private User updatedBy;
    private Timestamp updatedDate;
    private List<Link> links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Debt getDebt() {
        return debt;
    }

    public void setDebt(Debt debt) {
        this.debt = debt;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getCreditor() {
        return creditor;
    }

    public void setCreditor(User creditor) {
        this.creditor = creditor;
    }

    public User getDebtor() {
        return debtor;
    }

    public void setDebtor(User debtor) {
        this.debtor = debtor;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "Transaction ( id : " + this.id + " | "
                + "name : " + this.name + " | "
                + "currency : " + this.currency + " | "
                + "debt : " + this.debt + " | "
                + "group : " + this.group + " | "
                + "creditor : " + this.creditor + " | "
                + "debtor : " + this.debtor + " | "
                + "createdBy : " + this.createdBy + " | "
                + "createdDate : " + this.createdDate + " | "
                + "updatedBy : " + this.updatedBy + " | "
                + "updatedDate : " + this.updatedDate + " )";
    }

    /**
     * Generates a transaction entity from this class
     *
     * @return equivalent transaction entity
     */
    public TransactionEntity toTransactionEntity() {
        TransactionEntity transaction = TransactionMapper.INSTANCE.toTransactionEntity(this);

        transaction.setCurrency(this.currency.getCurrencyCode());

        transaction.setDebt(this.debt.toDebtEntity());
        transaction.setCreditor(this.creditor.toUserEntity());
        transaction.setDebtor(this.debtor.toUserEntity());

        if (this.createdBy != null) transaction.setCreatedBy(this.createdBy.toUserEntity());
        if (this.group != null) transaction.setGroup(this.group.toGroupEntity());
        if (this.updatedBy != null) transaction.setUpdatedBy(this.updatedBy.toUserEntity());

        return transaction;
    }
}
