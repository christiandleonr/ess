package com.easysplit.ess.friendships.domain.sql;

/**
 * Class that holds string variables with the queries for the <i>friendships</i> table
 */
public final class FriendshipsQueries {
    /**
     * Private constructor to prevent instantiation of the class.
     * This class should only hold string constants and should not be instantiated
     */
    private FriendshipsQueries() {

    }

    public static final String ESS_SCHEMA = "ESS_SCHEMA.";

    /**
     * Friendships table columns
     */
    public static final String TABLE_NAME = ESS_SCHEMA + "FRIENDSHIPS";
    public static final String FRIENDSHIPGUID_COLUMN = "FRIENDSHIPGUID";
    public static final String FRIEND_COLUMN = "FRIEND";
    public static final String STATUS_COLUMN = "STATUS";
    public static final String CREATED_DATE_COLUMN = "CREATED_DATE";
    public static final String CREATED_BY_COLUMN = "CREATED_BY";

    /**
     * Creates new friendship
     */
    public static final String CREATE_FRIENDSHIP = "INSERT INTO " + TABLE_NAME + " ( "
            + FRIENDSHIPGUID_COLUMN + ", "
            + FRIEND_COLUMN + ", "
            + STATUS_COLUMN + ", "
            + CREATED_DATE_COLUMN + ", "
            + CREATED_BY_COLUMN + " ) VALUES (?, ?, ?, ?, ?)";
}
