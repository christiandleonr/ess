package com.easysplit.ess.iam.domain.sql;

/**
 * Class that holds string variables with the queries for the <i>refresh_tokens</i> table
 */
public class RefreshTokenQueries {
    /**
     * Private constructor to prevent instantiation of the class.
     * This class should only hold string constants and should not be instantiated
     */
    private RefreshTokenQueries() {

    }

    public static final String ESS_SCHEMA = "ESS_SCHEMA.";

    /**
     * Refresh tokens table - all columns
     */
    public static final String TABLE_NAME = ESS_SCHEMA + "REFRESH_TOKENS";
    public static final String ID_COLUMN = "ID";
    public static final String TOKEN_COLUMN = "TOKEN";
    public static final String USERGUID_COLUMN = "USERGUID";

    /**
     * Insert refresh token
     */
    public static final String INSERT_REFRESH_TOKEN = "INSERT INTO " + TABLE_NAME + " ( " + TOKEN_COLUMN + ", "
            + USERGUID_COLUMN +  " ) VALUES (?, ?)";

    /**
     * Get refresh token details by otken
     */
    public static final String GET_REFRESH_TOKEN_BY_TOKEN = "SELECT " + ID_COLUMN + ", "
            + TOKEN_COLUMN + ", "
            + USERGUID_COLUMN + " FROM " + TABLE_NAME + " WHERE " + TOKEN_COLUMN + " = ?";

    /**
     * Delete token
     */
    public static final String DELETE_REFRESH_TOKEN = "DELETE FROM " + TABLE_NAME
            + " WHERE " + TOKEN_COLUMN + " = ?";
}
