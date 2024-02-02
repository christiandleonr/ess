package com.easysplit.ess.user.domain.sql;

/**
 * Class that holds string variables with the queries for the <i>User_Roles</i> table
 */
public class RolesQueries {
    /**
     * Private constructor to prevent instantiation of the class.
     * This class should only hold string constants and should not be instantiated
     */
    private RolesQueries() {

    }

    public static final String ESS_SCHEMA = "ESS_SCHEMA.";

    /**
     * User_Roles table columns
     */
    public static final String TABLE_NAME = ESS_SCHEMA + "ROLES";
    public static final String ROLEGUID_COLUMN = "ROLEGUID";
    public static final String NAME_COLUMN = "NAME";

    /**
     * Get the role names by user guid
     */
    public static final String GET_USER_ROLES = "SELECT " + ROLEGUID_COLUMN + ", " + NAME_COLUMN + " FROM " + TABLE_NAME
            + " WHERE " + ROLEGUID_COLUMN + " IN ( SELECT " + UserRolesQueries.ROLEGUID_COLUMN
            + " FROM " + UserRolesQueries.TABLE_NAME + " WHERE " + UserRolesQueries.USERGUID_COLUMN + " = ? )";
}
