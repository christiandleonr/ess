package com.easysplit.ess.transactions.domain.contracts;

import com.easysplit.ess.transactions.domain.models.DebtEntity;

/**
 * Class that handle the database operations for the debts resource
 */
public interface DebtsRepository {
    /**
     * Insert a new debt related to a transaction or a new revision
     * of a debt if the debt previously existed.
     *
     * @param debt debt to be created
     * @param transactionGuid transaction id to which the debt belongs to
     * @return created debt
     */
    DebtEntity insertNewDebt(DebtEntity debt, String transactionGuid);

    /**
     * Get the last revision for a specific debt
     *
     * @param debtGuid debt we want to look for
     * @return last revision of the debt
     */
    int getLastRevision(String debtGuid);
}
