package com.easysplit.ess.user.domain.sql;

/**
 * Class that holds string variables with the queries for the <i>User_Roles</i> table
 */
public class UserRolesQueries {
    /**
     * Private constructor to prevent instantiation of the class.
     * This class should only hold string constants and should not be instantiated
     */
    private UserRolesQueries() {

    }

    public static final String ESS_SCHEMA = "ESS_SCHEMA.";

    /**
     * User_Roles table columns
     */
    public static final String TABLE_NAME = ESS_SCHEMA + "USER_ROLES";
    public static final String USERGUID_COLUMN = "USERGUID";
    public static final String ROLEGUID_COLUMN = "ROLEGUID";

    /**
     * Insert new user role relation
     */
    public static final String INSERT_USER_ROLE = "INSERT INTO " + TABLE_NAME
            + " ( " + USERGUID_COLUMN + " ," + ROLEGUID_COLUMN + " ) VALUES (?, ?)";
}
