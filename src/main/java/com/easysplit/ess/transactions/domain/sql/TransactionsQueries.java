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
    public static final String CURRENCY_COLUMN = "CURRENCY";
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
            + CURRENCY_COLUMN + ", "
            + GROUPGUID_COLUMN + ", "
            + CREDITOR_COLUMN + ", "
            + DEBTOR_COLUMN + ", "
            + CREATED_BY_COLUMN + ", "
            + CREATED_DATE_COLUMN + ", "
            + UPDATED_BY_COLUMN + ", "
            + UPDATED_DATE_COLUMN + " ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
}
