package com.easysplit.ess.user.domain.models;

import java.sql.Timestamp;

/**
 * Friendship object to be used for database operations
 */
public class FriendshipEntity {
    private String friendshipGuid;
    private String friend;
    private FriendshipStatus status;
    private Timestamp createdDate;
    private String addedBy;

    public FriendshipEntity() {

    }

    public String getFriendshipGuid() {
        return friendshipGuid;
    }

    public void setFriendshipGuid(String friendshipGuid) {
        this.friendshipGuid = friendshipGuid;
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
