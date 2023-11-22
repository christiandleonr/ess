package com.easysplit.ess.friendships.domain.models;

import java.sql.Timestamp;

/**
 * Friendship object to be serialized and pure data validation
 */
public class Friendship {
    private String id;
    private String friend;
    private FriendshipStatus status;
    private Timestamp createdDate;
    private String addedBy;

    public Friendship() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }
}
