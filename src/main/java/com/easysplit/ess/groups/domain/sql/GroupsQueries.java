package com.easysplit.ess.groups.domain.sql;

/**
 * Class that holds string variables with the queries for the <i>groups</i> table
 */
public final class GroupsQueries {
    /**
     * Private constructor to prevent instantiation of the class.
     * This class should only hold string constants and should not be instantiated
     */
    private GroupsQueries() {

    }

    /**
     * Schema
     */
    public static final String ESS_SCHEMA = "ESS_SCHEMA.";

    /**
     * Groups table
     */
    public static final String TABLE_NAME = ESS_SCHEMA + "GROUPS";
    public static final String GROUPGUID_COLUMN = "GROUPGUID";
    public static final String NAME_COLUMN = "NAME";
    public static final String DESCRIPTION_COLUMN = "DESCRIPTION";
    public static final String CREATED_BY_COLUMN = "CREATED_BY";
    public static final String CREATED_DATE_COLUMN = "CREATED_DATE";
    public static final String UPDATED_BY_COLUMN = "UPDATED_BY";
    public static final String UPDATED_DATE_COLUMN = "UPDATED_DATE";

    /**
     * Insert group
     */
    public static final String INSERT_GROUP = "INSERT INTO " + TABLE_NAME + " ( "
            + GROUPGUID_COLUMN + ", "
            + NAME_COLUMN + ", "
            + DESCRIPTION_COLUMN + ", "
            + CREATED_BY_COLUMN + ", "
            + CREATED_DATE_COLUMN + ", "
            + UPDATED_BY_COLUMN + ", "
            + UPDATED_DATE_COLUMN + " ) VALUES (?, ?, ?, ?, ?, ?, ?)";

    /**
     * Group Members table
     */
    public static final String GROUPMEMBERS_TABLE_NAME = ESS_SCHEMA + "GROUP_MEMBERS";
    public static final String GROUPMEMBERS_GROUPGUID_COLUMN = "GROUPGUID";
    public static final String GROUPMEMBERS_MEMBERGUID_COLUMN = "MEMBERGUID";

    /**
     * Insert group member
     */
    public static final String INSERT_GROUP_MEMBER = "INSERT INTO " + GROUPMEMBERS_TABLE_NAME + " ( "
            + GROUPMEMBERS_GROUPGUID_COLUMN + ", "
            + GROUPMEMBERS_MEMBERGUID_COLUMN + " ) VALUES (?, ?)";

    /**
     * Delete group member
     */
    public static final String DELETE_GROUP_MEMBER = "DELETE FROM " + GROUPMEMBERS_TABLE_NAME
        + " WHERE " + GROUPMEMBERS_MEMBERGUID_COLUMN + " = ?";
}
