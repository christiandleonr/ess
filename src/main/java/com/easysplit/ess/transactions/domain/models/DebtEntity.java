package com.easysplit.ess.transactions.domain.models;

import com.easysplit.ess.user.domain.models.UserEntity;
import org.postgresql.util.PGmoney;

import java.sql.Timestamp;

/**
 * Class that represents the table <i>debts</i>
 */
public class DebtEntity {
    private String debtGuid;
    private PGmoney totalAmount;
    private PGmoney debt;
    private boolean debtSettled;
    private int revision;
    private UserEntity createdBy;
    private Timestamp createdDate;

    public DebtEntity() {

    }

    public String getDebtGuid() {
        return debtGuid;
    }

    public void setDebtGuid(String debtGuid) {
        this.debtGuid = debtGuid;
    }

    public PGmoney getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(PGmoney totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PGmoney getDebt() {
        return debt;
    }

    public void setDebt(PGmoney debt) {
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

    @Override
    public String toString() {
        return "debtGuid : " + this.debtGuid + " | "
                + "totalAmount : " + this.totalAmount + " | "
                + "debt : " + this.debt + " | "
                + "debtSettled : " + this.debtSettled + " | "
                + "revision : " + this.revision + " | "
                + "createdBy : " + this.createdBy + " | "
                + "createdDate : " + this.createdDate;
    }
}
