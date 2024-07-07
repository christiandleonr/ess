package com.easysplit.ess.groups.domain.models;

import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.ess.user.domain.models.UserMapper;
import com.easysplit.shared.domain.models.Link;

import java.sql.Timestamp;
import java.util.List;

/**
 * Group object to be serialized
 */
public class Group {
    private String id;
    private String name;
    private String description;
    private List<User> members;
    private User createdBy;
    private Timestamp createdDate;
    private User updatedBy;
    private Timestamp updatedDate;
    private List<Link> links;

    public Group() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * Generates a group entity class
     *
     * @return group entity
     */
    public GroupEntity toGroupEntity() {
        GroupEntity groupEntity = GroupMapper.INSTANCE.toGroupEntity(this);
        List<UserEntity> members = UserMapper.INSTANCE.toListOfUserEntities(this.getMembers());

        groupEntity.setMembers(members);

        if (this.createdBy != null) {
            groupEntity.setCreatedBy(this.createdBy.toUserEntity());
        }

        if (this.updatedBy != null) {
            groupEntity.setUpdatedBy(this.updatedBy.toUserEntity());
        }

        return groupEntity;
    }

    @Override
    public String toString() {
        return "Group ( id : " + this.id + " | "
                + "name : " + this.name + " | "
                + "description : " + this.description + " | "
                + "members : " + this.members + " | "
                + "createdBy : " + this.createdBy + " | "
                + "createdDate : " + this.createdDate + " | "
                + "updatedBy : " + this.updatedBy + " | "
                + "updatedDate : " + this.updatedDate + " )";
    }
}
