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

    /**
     * Friendships table columns
     */
    public static final String TABLE_NAME = "FRIENDSHIPS";
    public static final String FRIENDSHIPGUID_COLUMN = "FRIENDSHIPGUID";
    public static final String USER1_COLUMN = "USER1";
    public static final String USER2_COLUMN = "USER2";
    public static final String STATUS_COLUMN = "STATUS";
    public static final String CREATED_DATE_COLUMN = "CREATED_DATE";
    public static final String CREATED_BY_COLUMN = "CREATED_BY";

    /**
     * Creates new friendship
     */
    public static final String CREATE_FRIENDSHIP = "INSERT INTO " + TABLE_NAME + " ( "
            + FRIENDSHIPGUID_COLUMN + ", "
            + USER1_COLUMN + ", "
            + USER2_COLUMN + ", "
            + STATUS_COLUMN + ", "
            + CREATED_DATE_COLUMN + ", "
            + CREATED_BY_COLUMN + " ) VALUES (?, ?, ?, ?, ?, ?)";
}
