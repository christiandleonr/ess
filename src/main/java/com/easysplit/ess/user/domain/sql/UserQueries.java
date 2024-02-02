package com.easysplit.ess.user.domain.sql;

/**
 * Class that holds string variables with the queries for the <i>users</i> table
 */
public final class UserQueries {

    /**
     * Private constructor to prevent instantiation of the class.
     * This class should only hold string constants and should not be instantiated
     */
    private UserQueries() {

    }

    public static final String ESS_SCHEMA = "ESS_SCHEMA.";

    /**
     * Users table - all columns
     */
    public static final String TABLE_NAME = ESS_SCHEMA + "USERS";
    public static final String USERGUID_COLUMN = "USERGUID";
    public static final String NAME_COLUMN = "NAME";
    public static final String LASTNAME_COLUMN = "LASTNAME";
    public static final String USERNAME_COLUMN = "USERNAME";
    public static final String PASSWORD_COLUMN = "PASSWORD";
    public static final String EMAIL_COLUMN = "EMAIL";
    public static final String PHONE_COLUMN = "PHONE";
    public static final String CREATE_DATE_COLUMN = "CREATED_DATE";

    /**
     * Creates a new user
     */
    public static final String INSERT_USER = "INSERT INTO " + TABLE_NAME + " ( " + USERGUID_COLUMN + ", "
            + NAME_COLUMN + ", "
            + LASTNAME_COLUMN + ", "
            + USERNAME_COLUMN + ", "
            + PASSWORD_COLUMN + ", "
            + EMAIL_COLUMN + ", "
            + PHONE_COLUMN + ", "
            + CREATE_DATE_COLUMN + " ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Gets user by id
     */
    public static final String GET_USER = "SELECT " + USERGUID_COLUMN + ", "
            + NAME_COLUMN + ", "
            + LASTNAME_COLUMN + ", "
            + USERNAME_COLUMN + ", "
            + PASSWORD_COLUMN + ", "
            + EMAIL_COLUMN + ", "
            + PHONE_COLUMN + ", "
            + CREATE_DATE_COLUMN + " FROM " + TABLE_NAME + " WHERE " + USERGUID_COLUMN + " = ?";

    /**
     * Gets user by username
     */
    public static final String GET_USER_BY_USERNAME = "SELECT " + USERGUID_COLUMN + ", "
            + NAME_COLUMN + ", "
            + LASTNAME_COLUMN + ", "
            + USERNAME_COLUMN + ", "
            + PASSWORD_COLUMN + ", "
            + EMAIL_COLUMN + ", "
            + PHONE_COLUMN + ", "
            + CREATE_DATE_COLUMN + " FROM " + TABLE_NAME + " WHERE " + USERNAME_COLUMN + " = ?";

    /**
     * Gets user by email
     */
    public static final String GET_USER_BY_EMAIL = "SELECT " + USERGUID_COLUMN + ", "
            + NAME_COLUMN + ", "
            + LASTNAME_COLUMN + ", "
            + USERNAME_COLUMN + ", "
            + EMAIL_COLUMN + ", "
            + PHONE_COLUMN + ", "
            + CREATE_DATE_COLUMN + " FROM " + TABLE_NAME + " WHERE " + EMAIL_COLUMN + " = ?";

    /**
     * Gets user by phone number
     */
    public static final String GET_USER_BY_PHONE = "SELECT " + USERGUID_COLUMN + ", "
            + NAME_COLUMN + ", "
            + LASTNAME_COLUMN + ", "
            + USERNAME_COLUMN + ", "
            + EMAIL_COLUMN + ", "
            + PHONE_COLUMN + ", "
            + CREATE_DATE_COLUMN + " FROM " + TABLE_NAME + " WHERE " + PHONE_COLUMN + " = ?";

    /**
     * Deletes user by id
     */
    public static final String DELETE_USER_BY_ID = "DELETE FROM " + TABLE_NAME + " WHERE " + USERGUID_COLUMN + " = ?";
}
