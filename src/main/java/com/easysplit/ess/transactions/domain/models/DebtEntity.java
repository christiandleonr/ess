package com.easysplit.ess.transactions.domain.models;

import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.ess.user.domain.models.UserMapper;
import com.easysplit.shared.domain.models.Money;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Class that represents the table <i>debts</i>
 */
public class DebtEntity {
    private String debtGuid;
    private BigDecimal totalAmount;
    private BigDecimal debt;
    private boolean debtSettled;
    private int revision;
    private UserEntity createdBy;
    private Timestamp createdDate;

    public DebtEntity() {}

    public String getDebtGuid() {
        return debtGuid;
    }

    public void setDebtGuid(String debtGuid) {
        this.debtGuid = debtGuid;
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDebt() {
        return debt;
    }

    public void setDebt(BigDecimal debt) {
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
        return "Debt ( id : " + this.debtGuid + " | "
                + "totalAmount : " + this.totalAmount + " | "
                + "debt : " + this.debt + " | "
                + "debtSettled : " + this.debtSettled + " | "
                + "revision : " + this.revision + " | "
                + "createdBy : " + this.createdBy + " | "
                + "createdDate : " + this.createdDate + " )";
    }

    /**
     * Generates a debt object from this entity class
     *
     * @return equivalent debt
     */
    public Debt toDebt() {
        Debt debt = DebtMapper.INSTANCE.toDebt(this);

        debt.setTotalAmount(new Money(this.totalAmount));
        debt.setDebt(new Money(this.debt));

        User createdBy = UserMapper.INSTANCE.toUser(this.createdBy);
        debt.setCreatedBy(createdBy);

        return debt;
    }
}
