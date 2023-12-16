package com.easysplit.ess.transactions.domain.sql;

/**
 * Class that holds string variables with the queries for the <i>transactions</i> table
 */
public final class TransactionsQueries {

    /**
     * Private constructor to prevent instantiation of the class.
     * This class should only hold string constants and should not be instantiated
     */
    private TransactionsQueries() {

    }

    public static final String ESS_SCHEMA = "ESS_SCHEMA";

    /**
     * Transactions table - all columns
     */
    public static final String TABLE_NAME = ESS_SCHEMA + "ESS_SCHEMA.";
    public static final String TRANSACTIONGUID_COLUMN = "TRANSACTIONGUID";
    public static final String NAME_COLUMN = "NAME";
    public static final String GROUPGUID_COLUMN = "GROUPGUID";
    public static final String CREDITOR_COLUMN = "CREDITOR";
    public static final String DEBTOR_COLUMN = "DEBTOR";
    public static final String CREATED_BY_COLUMN = "CREATED_BY";
    public static final String CREATED_DATE_COLUMN = "CREATED_DATE";
    public static final String UPDATED_BY_COLUMN = "UPDATED_BY";
    public static final String UPDATED_DATE_COLUMN = "UPDATED_DATE";

    /**
     * Inserts a new transaction
     */
    public static final String INSERT_TRANSACTION = "INSERT INTO " + TABLE_NAME + " ( " + TRANSACTIONGUID_COLUMN + ", "
            + NAME_COLUMN + ", "
            + GROUPGUID_COLUMN + ", "
            + CREDITOR_COLUMN + ", "
            + DEBTOR_COLUMN + ", "
            + CREATED_BY_COLUMN + ", "
            + CREATED_DATE_COLUMN + ", "
            + UPDATED_BY_COLUMN + ", "
            + UPDATED_DATE_COLUMN + " ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Debts table - all column
     */
    public static final String DEBTS_TABLE_NAME = ESS_SCHEMA + "DEBTS";
    public static final String DEBTS_DEBTGUID_COLUMN = "DEBTGUID";
    public static final String DEBTS_TRANSACTIONGUID_COLUMN = TRANSACTIONGUID_COLUMN;
    public static final String DEBTS_TOTAL_AMOUNT_COLUMN = "TOTALAMOUNT";
    public static final String DEBTS_DEBT_COLUMN = "DEBT";
    public static final String DEBTS_DEBT_SETTLED_COLUMN = "DEBTSETTLED";
    public static final String DEBTS_REVISION_COLUMN = "REVISION";
    public static final String DEBTS_CREATED_BY_COLUMN = "CREATED_BY";
    public static final String DEBTS_CREATED_DATE_COLUMN = "CREATED_DATE";

    /**
     * Inserts new a new debt or a new revision of a debt
     */
    public static final String INSERT_DEBT = "INSERT INTO " + DEBTS_TABLE_NAME + " ( "
            + DEBTS_DEBTGUID_COLUMN + ", "
            + DEBTS_TRANSACTIONGUID_COLUMN + ", "
            + DEBTS_TOTAL_AMOUNT_COLUMN + ", "
            + DEBTS_DEBT_COLUMN + ", "
            + DEBTS_DEBT_SETTLED_COLUMN + ", "
            + DEBTS_REVISION_COLUMN + ", "
            + DEBTS_CREATED_BY_COLUMN + ", "
            + DEBTS_CREATED_DATE_COLUMN + " ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
}
