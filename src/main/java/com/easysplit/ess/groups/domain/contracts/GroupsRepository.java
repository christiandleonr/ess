package com.easysplit.ess.groups.domain.contracts;

import com.easysplit.ess.groups.domain.models.GroupEntity;
import com.easysplit.ess.user.domain.models.UserEntity;

import java.util.List;

/**
 * Class that handle the database operations for the groups resource
 */
public interface GroupsRepository {
    /**
     * Creates group with the list of members provided
     *
     * @param group group to be created
     * @return created group
     */
    GroupEntity createGroup(String createdBy, GroupEntity group);

    /**
     * Gets a group by its guid
     *
     * @param groupGuid group id
     * @return group
     */
    GroupEntity getGroup(String groupGuid);

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
     * @param groupMembers list of members to add
     */
    List<UserEntity> addGroupMembers(String groupGuid, List<UserEntity> groupMembers);

    /**
     * Get group members
     *
     * @param groupGuid group guid
     * @return list of members
     */
    List<UserEntity> getGroupMembers(String groupGuid);

    /**
     * Delete group member
     *
     * @param userGuid user to be removed
     */
    void deleteGroupMember(String userGuid);

    /**
     * Deletes all members for a group that is being deleted
     *
     * @param groupGuid group whose members we want to delete
     */
    void deleteAllGroupMembers(String groupGuid);

    /**
     *  Deletes group by its guid
     *
     * @param groupGuid group guid
     */
    void deleteGroup(String groupGuid);
}
