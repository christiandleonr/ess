package com.easysplit.ess.groups.domain.models;

import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.shared.utils.EssUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Add comments
 */
public class GroupEntity {
    private String groupGuid;
    private String name;
    private String description;
    private User createdBy;
    private Timestamp createdDate;
    private User updatedBy;
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

    /**
     * TODO Add comments
     * @param members
     * @return
     */
    public List<GroupMemberEntity> buildGroupMemberEntities(List<User> members) {
        List<GroupMemberEntity> groupMembers = new ArrayList<>();

        if (EssUtils.isNullOrEmpty(members)) {
            return groupMembers;
        }

        for (User user: members) {
            GroupMemberEntity groupMember = new GroupMemberEntity();
            groupMember.setGroupGuid(groupGuid);
            groupMember.setMemberGuid(user.getId());

            groupMembers.add(groupMember);
        }

        return groupMembers;
    }

    /**
     *
     * @param groupMapper
     * @return
     */
    public Group toGroup(GroupMapper groupMapper, List<User> members) {
        if (groupMapper == null) {
            return null;
        }

        Group group = groupMapper.toGroup(this);
        group.setMembers(members);

        return group;
    }
}
