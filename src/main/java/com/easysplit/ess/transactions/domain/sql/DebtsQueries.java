package com.easysplit.ess.transactions.domain.sql;

/**
 * Class that holds string variables with the queries for the <i>transactions</i> table
 */
public class DebtsQueries {

    /**
     * Private constructor to prevent instantiation of the class.
     * This class should only hold string constants and should not be instantiated
     */
    private DebtsQueries() {

    }

    public static final String ESS_SCHEMA = "ESS_SCHEMA.";

    /**
     * Debts table - all column
     */
    public static final String TABLE_NAME = ESS_SCHEMA + "DEBTS";
    public static final String DEBTGUID_COLUMN = "DEBTGUID";
    public static final String TRANSACTIONGUID_COLUMN = TransactionsQueries.TRANSACTIONGUID_COLUMN;
    public static final String TOTAL_AMOUNT_COLUMN = "TOTALAMOUNT";
    public static final String DEBT_COLUMN = "DEBT";
    public static final String DEBT_SETTLED_COLUMN = "DEBTSETTLED";
    public static final String REVISION_COLUMN = "REVISION";
    public static final String CREATED_BY_COLUMN = "CREATED_BY";
    public static final String CREATED_DATE_COLUMN = "CREATED_DATE";

    /**
     * Inserts new a new debt or a new revision of a debt
     */
    public static final String INSERT_DEBT = "INSERT INTO " + TABLE_NAME + " ( "
            + DEBTGUID_COLUMN + ", "
            + TRANSACTIONGUID_COLUMN + ", "
            + TOTAL_AMOUNT_COLUMN + ", "
            + DEBT_COLUMN + ", "
            + DEBT_SETTLED_COLUMN + ", "
            + REVISION_COLUMN + ", "
            + CREATED_BY_COLUMN + ", "
            + CREATED_DATE_COLUMN + " ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Get the last revision of a debt
     */
    public static final String GET_LAST_REVISION = "SELECT " + REVISION_COLUMN + " FROM " + TABLE_NAME
            + " WHERE " + DEBTGUID_COLUMN + " = ? ORDER BY " + REVISION_COLUMN + " DESC LIMIT 1";

    /**
     * Get the debt by transaction guid
     */
    public static final String GET_DEBT = "SELECT " + DEBTGUID_COLUMN + ", "
            + TOTAL_AMOUNT_COLUMN + ", "
            + DEBT_COLUMN + ", "
            + DEBT_SETTLED_COLUMN + ", "
            + REVISION_COLUMN + ", "
            + CREATED_BY_COLUMN + ", "
            + CREATED_DATE_COLUMN + " FROM " + TABLE_NAME + " WHERE " + TRANSACTIONGUID_COLUMN + " = ? ORDER BY " + REVISION_COLUMN + " DESC LIMIT 1";

}