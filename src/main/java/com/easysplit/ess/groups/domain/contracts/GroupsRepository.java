package com.easysplit.ess.groups.domain.contracts;

import com.easysplit.ess.groups.domain.models.GroupEntity;
import com.easysplit.ess.user.domain.models.UserEntity;

import java.util.List;

public interface GroupsRepository {
    /**
     * Creates group with the list of members provided
     *
     * @param group group to be created
     * @return created group
     */
    GroupEntity createGroup(String createdBy, GroupEntity group);

    /**
     * Adds a new group member
     *
     * @param groupMember class with the user and group information to be added
     * @return group member entity
     */
    UserEntity addGroupMember(String groupGuid, UserEntity groupMember);

    /**
     * Adds users to a group
     *
     * @param groupMembers
     */
    List<UserEntity> addGroupMembers(String groupGuid, List<UserEntity> groupMembers);
}
