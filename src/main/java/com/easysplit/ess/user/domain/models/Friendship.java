package com.easysplit.ess.user.domain.models;

import com.easysplit.shared.domain.models.Link;
import java.sql.Timestamp;
import java.util.List;

/**
 * Friendship object to be serialized
 */
public class Friendship {
    private String id;
    private User friend;
    private FriendshipStatus status;
    private Timestamp createdDate;
    private User addedBy;
    private List<Link> links;

    public Friendship() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
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

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * Generates a friendship entity from this class
     *
     * @return friendship entity
     */
    public FriendshipEntity toFriendshipEntity() {
        FriendshipEntity friendshipEntity = FriendshipsMapper.INSTANCE.toFriendshipEntity(this);
        friendshipEntity.setFriend(this.friend.toUserEntity());
        friendshipEntity.setAddedBy(this.addedBy.toUserEntity());

        return friendshipEntity;
    }

    @Override
    public String toString() {
        return "Friendship ( id : " + this.id + " | "
                + "friend : " + this.friend + " | "
                + "status : " + this.status + " | "
                + "createdDate : " + this.createdDate + " | "
                + "addedBy : " + this.addedBy + " )";
    }
}
