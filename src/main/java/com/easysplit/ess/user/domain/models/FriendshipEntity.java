package com.easysplit.ess.user.domain.models;

import java.sql.Timestamp;

/**
 * Friendship object to be used for database operations
 */
public class FriendshipEntity {
    private String friendshipGuid;
    private UserEntity friend;
    private FriendshipStatus status;
    private Timestamp createdDate;
    private UserEntity addedBy;

    public FriendshipEntity() {}

    public String getFriendshipGuid() {
        return friendshipGuid;
    }

    public void setFriendshipGuid(String friendshipGuid) {
        this.friendshipGuid = friendshipGuid;
    }

    public UserEntity getFriend() {
        return friend;
    }

    public void setFriend(UserEntity friend) {
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

    public UserEntity getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(UserEntity addedBy) {
        this.addedBy = addedBy;
    }

    /**
     * Generates a friendship object from this entity class
     *
     * @return friendship
     */
    public Friendship toFriendship() {
        Friendship friendship = FriendshipsMapper.INSTANCE.toFriendship(this);
        friendship.setFriend(this.friend.toUser());
        friendship.setAddedBy(this.addedBy.toUser());

        return friendship;
    }

    @Override
    public String toString() {
        return "id : " + this.friendshipGuid + " | "
                + "friend : " + this.friend + " | "
                + "status : " + this.status + " | "
                + "createdDate : " + this.createdDate + " | "
                + "addedBy : " + this.addedBy;
    }
}
