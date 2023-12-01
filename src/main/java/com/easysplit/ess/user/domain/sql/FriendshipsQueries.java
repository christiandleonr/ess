package com.easysplit.ess.user.domain.sql;

import com.easysplit.ess.user.domain.models.FriendshipStatus;

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
    public static final String ADDED_BY_COLUMN = "ADDED_BY";

    /**
     * Creates new friendship
     */
    public static final String CREATE_FRIENDSHIP = "INSERT INTO " + TABLE_NAME + " ( "
            + FRIENDSHIPGUID_COLUMN + ", "
            + FRIEND_COLUMN + ", "
            + STATUS_COLUMN + ", "
            + CREATED_DATE_COLUMN + ", "
            + ADDED_BY_COLUMN + " ) VALUES (?, ?, ?, ?, ?)";

    /**
     * Get all friends
     */
    public static final String GET_FRIENDS = "SELECT CASE"
            + " WHEN " + FRIEND_COLUMN + " = ? THEN " + ADDED_BY_COLUMN
            + " WHEN " + ADDED_BY_COLUMN + " = ? THEN " + FRIEND_COLUMN
            + " END AS " + UserQueries.USERGUID_COLUMN + " FROM " + TABLE_NAME
            + " WHERE ? IN (" + FRIEND_COLUMN + ", " + ADDED_BY_COLUMN + ")"
            + " AND " + STATUS_COLUMN +  " NOT IN ('"+ FriendshipStatus.PENDING.getValue() +"', '" + FriendshipStatus.REJECTED.getValue() + "')"
            + " ORDER BY " + CREATED_DATE_COLUMN + " LIMIT ? OFFSET ?";

    /**
     * Count friends
     */

    public static final String COUNT_FRIENDS = "SELECT COUNT(*) FROM " + TABLE_NAME
            + " WHERE ? IN (" + FRIEND_COLUMN + ", " + ADDED_BY_COLUMN + ")"
            + " AND " + STATUS_COLUMN +  " NOT IN ('"+ FriendshipStatus.PENDING.getValue() +"', '" + FriendshipStatus.REJECTED.getValue() + "')";

}
