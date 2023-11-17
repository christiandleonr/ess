package com.easysplit.ess.friendships.domain.models;

import java.sql.Timestamp;

/**
 * Friendship object to be used for database operations
 */
public class FriendshipEntity {
    private String friendshipGuid;
    private String user1;
    private String user2;
    private String status;
    private Timestamp createdDate;
    private String createdBy;

    public FriendshipEntity() {

    }

    public String getFriendshipGuid() {
        return friendshipGuid;
    }

    public void setFriendshipGuid(String friendshipGuid) {
        this.friendshipGuid = friendshipGuid;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
