package com.easysplit.ess.groups.domain.models;

import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.ess.user.domain.models.UserMapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents the table <i>groups</i>
 */
public class GroupEntity {
    private String groupGuid;
    private String name;
    private String description;
    private List<UserEntity> members;
    private UserEntity createdBy;
    private Timestamp createdDate;
    private UserEntity updatedBy;
    private Timestamp updatedDate;

    public GroupEntity() {

    }

    public String getGroupGuid() {
        return groupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        this.groupGuid = groupGuid;
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

    public UserEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserEntity createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public UserEntity getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UserEntity updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<UserEntity> getMembers() {
        return members;
    }

    public void setMembers(List<UserEntity> members) {
        this.members = members;
    }

    /**
     * Generates a list of members including the createdBy user
     *
     * @return list of members
     */
    public List<UserEntity> getAllMembers() {
        List<UserEntity> members = new ArrayList<>(this.members);

        if (createdBy != null) {
            members.add(createdBy);
        }

        return members;
    }

    /**
     * Generates a group object from this entity class
     *
     * @return group entity
     */
    public Group toGroup() {
        Group group = GroupMapper.INSTANCE.toGroup(this);
        List<User> members = UserMapper.INSTANCE.toListOfUsers(this.getMembers());

        group.setMembers(members);
        group.setCreatedBy(UserMapper.INSTANCE.toUser(this.createdBy));
        group.setUpdatedBy(UserMapper.INSTANCE.toUser(this.updatedBy));

        return group;
    }

    @Override
    public String toString() {
        return "id : " + this.groupGuid + " | "
                + "name : " + this.name + " | "
                + "description : " + this.description + " | "
                + "members : " + this.members + " | "
                + "createdBy : " + this.createdBy + " | "
                + "createdDate : " + this.createdDate + " | "
                + "updatedBy : " + this.updatedBy + " | "
                + "updatedDate : " + this.updatedDate;
    }
}
