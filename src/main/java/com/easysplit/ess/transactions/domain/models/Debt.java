package com.easysplit.ess.transactions.domain.models;

import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.models.Link;
import com.easysplit.shared.domain.models.Money;

import java.sql.Timestamp;
import java.util.List;

/**
 * Debt object to be serialized
 */
public class Debt {
    private String id;
    private Money totalAmount;
    private Money debt;
    private boolean debtSettled;
    private int revision;
    private User createdBy;
    private Timestamp createdDate;
    private List<Link> links;

    public Debt() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Money getDebt() {
        return debt;
    }

    public void setDebt(Money debt) {
        this.debt = debt;
    }

    public boolean isDebtSettled() {
        return debtSettled;
    }

    public void setDebtSettled(boolean debtSettled) {
        this.debtSettled = debtSettled;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
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

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * Generates a debt entity from this class
     *
     * @return equivalent debt entity
     */
    public DebtEntity toDebtEntity() {
        return DebtMapper.INSTANCE.toDebtEntity(this);
    }
}