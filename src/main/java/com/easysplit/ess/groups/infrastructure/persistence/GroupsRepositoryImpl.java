package com.easysplit.ess.groups.infrastructure.persistence;

import com.easysplit.ess.groups.domain.contracts.GroupsRepository;
import com.easysplit.ess.groups.domain.models.GroupEntity;
import com.easysplit.ess.groups.domain.sql.GroupsQueries;
import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.NotFoundException;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import com.easysplit.shared.utils.EssUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class GroupsRepositoryImpl implements GroupsRepository {
    private final JdbcTemplate jdbc;
    private final InfrastructureHelper infrastructureHelper;
    private final UserRepository userRepository;
    private static final String CLASS_NAME = GroupsRepositoryImpl.class.getName();
    private static final Logger logger = LoggerFactory.getLogger(GroupsRepositoryImpl.class);

    @Autowired
    public GroupsRepositoryImpl(JdbcTemplate jdbc,
                                InfrastructureHelper infrastructureHelper,
                                UserRepository userRepository) {
        this.jdbc = jdbc;
        this.infrastructureHelper = infrastructureHelper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public GroupEntity createGroup(String createdBy, GroupEntity group) {
        String groupGuid = UUID.randomUUID().toString();
        Timestamp createdDate = infrastructureHelper.getCurrentDate();

        // Throws a NotFoundException if the user does not exist
        UserEntity createdByUser = userRepository.getUser(createdBy);

        try {
            jdbc.update(GroupsQueries.INSERT_GROUP,
                    groupGuid,
                    group.getName(),
                    group.getDescription(),
                    createdByUser.getUserGuid(),
                    createdDate,
                    createdByUser.getUserGuid(),
                    createdDate
            );
        } catch (Exception e) {
            logger.error("{}.createGroup() - Something went wrong while creating the group: {}", CLASS_NAME, group, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_GROUP_ERROR_MESSAGE,
                    ErrorKeys.CREATE_GROUP_ERROR_MESSAGE,
                    new Object[]{ group },
                    e.getCause()
            );
        }

        group.setGroupGuid(groupGuid);
        group.setCreatedBy(createdByUser);
        group.setCreatedDate(createdDate);
        group.setUpdatedBy(createdByUser);
        group.setUpdatedDate(createdDate);

        // Add list of members
        List<UserEntity> members = addGroupMembers(groupGuid, group.getAllMembers());
        group.setMembers(members);

        return group;
    }

    @Override
    public void deleteGroup(String groupGuid){
        // Throws a NotFoundException if group does not exist
        getGroup(groupGuid);

        int rowsDeleted = 0;
        try{
            rowsDeleted = jdbc.update(GroupsQueries.DELETE_GROUP_BY_ID, groupGuid);
        } catch (Exception e) {
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.DELETE_GROUP_ERROR_TITLE,
                    ErrorKeys.DELETE_GROUP_ERROR_MESSAGE,
                    new Object[] {groupGuid},
                    e.getCause()
            );
        }

        logger.info("{} .deleteGroupsById() - Groups deleted: {}", CLASS_NAME, rowsDeleted);
    }

    @Override
    public GroupEntity getGroup(String groupGuid) {
        GroupEntity groupEntity = null;

        try {
            groupEntity = jdbc.query(GroupsQueries.GET_GROUP,
                    this::toGroupEntity,
                    groupGuid);
        } catch (NotFoundException e) {
            // Catching NotFoundException thrown from toGroupEntity method
            logger.debug("{}.getGroup() - NotFoundException while reading the user: {}", CLASS_NAME, groupGuid, e);
            throw e;
        } catch (Exception e) {
            logger.error("{}.getGroup() - Something went wrong while reading the group with id: {}", CLASS_NAME, groupGuid, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.GET_GROUP_ERROR_TITLE,
                    ErrorKeys.GET_GROUP_ERROR_MESSAGE,
                    new Object[] {groupGuid},
                    e.getCause()
            );
        }

        if (groupEntity == null) {
            logger.debug("{}.getGroup() - Group with id {} not found", CLASS_NAME, groupGuid);
            infrastructureHelper.throwNotFoundException(
                    ErrorKeys.GET_GROUP_NOT_FOUND_TITLE,
                    ErrorKeys.GET_GROUP_NOT_FOUND_MESSAGE,
                    new Object[]{groupGuid}
            );
        }

        return groupEntity;

    }

    @Override
    @Transactional
    public List<UserEntity> addGroupMembers(String groupGuid, List<UserEntity> groupMembers) {
        List<UserEntity> members = new ArrayList<>();

        if (EssUtils.isNullOrEmpty(groupMembers)) {
            logger.debug("{}.addGroupMembers() - Empty list of members: {}", CLASS_NAME, groupMembers);
            return members;
        }

        for (UserEntity groupMember: groupMembers) {
            UserEntity member = addGroupMember(groupGuid, groupMember);
            members.add(member);
        }

        return members;
    }

    @Override
    public List<UserEntity> getGroupMembers(String groupGuid) {
        List<UserEntity> members = new ArrayList<>();

        if (EssUtils.isNullOrEmpty(groupGuid)) {
            return members;
        }

        try {
            List<String> memberIds = jdbc.query(GroupsQueries.GET_GROUP_MEMBERS,
                    (rs, rowNum) -> rs.getString(GroupsQueries.GROUPMEMBERS_MEMBERGUID_COLUMN),
                    groupGuid);

            for (String memberId: memberIds) {
                members.add(userRepository.getUser(memberId));
            }
        } catch (NotFoundException e) {
            logger.debug("{}.getGroupMembers() - A member of the group was not found {}", CLASS_NAME, groupGuid, e);
            throw e;
        } catch (Exception e) {
            logger.error("{}.getGroupMembers() - Something went wrong while reading the group's members for group with id: {}", CLASS_NAME, groupGuid, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.GET_GROUP_ERROR_TITLE,
                    ErrorKeys.GET_GROUP_ERROR_MESSAGE,
                    new Object[] {groupGuid},
                    e.getCause()
            );
        }

        return members;
    }

    @Override
    @Transactional
    public void deleteGroupMember(String userGuid) {
        // Throws a NotFoundException if user does not exist
        userRepository.getUser(userGuid);

        int rowsDeleted = 0;
        try {
            rowsDeleted = jdbc.update(GroupsQueries.DELETE_GROUP_MEMBER, userGuid);
        } catch (Exception e) {
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.DELETE_GROUP_MEMBER_ERROR_TITLE,
                    ErrorKeys.DELETE_GROUP_MEMBER_ERROR_MESSAGE,
                    new Object[]{ userGuid },
                    e.getCause()
            );
        }

        logger.info("{}.deleteGroupMember() - delete from {} groups", CLASS_NAME, rowsDeleted);
    }

    public void deleteAllGroupMembers(String groupGuid) {
        // Throws a NotFoundException if group does not exist
        getGroup(groupGuid);

        int rowsDeleted = 0;
        try {
            rowsDeleted = jdbc.update(GroupsQueries.DELETE_ALL_GROUP_MEMBERS, groupGuid);
        } catch (Exception e) {
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.DELETE_ALL_GROUP_MEMBER_ERROR_TITLE,
                    ErrorKeys.DELETE_ALL_GROUP_MEMBER_ERROR_MESSAGE,
                    new Object[]{ groupGuid },
                    e.getCause()
            );
        }

        logger.info("{}.deleteAllGroupMembers() - delete from {} groups", CLASS_NAME, rowsDeleted);

    }

    @Override
    @Transactional
    public UserEntity addGroupMember(String groupGuid, UserEntity groupMember) {
        if (groupMember == null) {
            return null;
        }

        // Throws NotFoundException if the user does not exist
        UserEntity member = userRepository.getUser(groupMember.getUserGuid());

        try {
            jdbc.update(GroupsQueries.INSERT_GROUP_MEMBER,
                    groupGuid,
                    member.getUserGuid()
            );
        } catch (Exception e) {
            logger.error("{}.addGroupMember() - Something went wrong while adding the member: {}", CLASS_NAME, groupMember, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_GROUP_ERROR_TITLE,
                    ErrorKeys.CREATE_GROUP_ERROR_MESSAGE,
                    new Object[]{ groupMember },
                    e.getCause()
            );
        }

        return member;
    }

    /**
     * Create a <i>GroupEntity</i> object from a <i>ResultSet</i>
     *
     * @param rs result set
     * @return Object of type <i>GroupEntity</i>
     */
    private GroupEntity toGroupEntity(ResultSet rs) throws SQLException {

        GroupEntity groupEntity = null;

        if (rs.next()) {
            groupEntity = new GroupEntity();

            groupEntity.setGroupGuid(rs.getString(GroupsQueries.GROUPGUID_COLUMN.toLowerCase()));
            groupEntity.setName(rs.getString(GroupsQueries.NAME_COLUMN.toLowerCase()));
            groupEntity.setDescription(rs.getString(GroupsQueries.DESCRIPTION_COLUMN.toLowerCase()));

            String createdByGuid = rs.getString(GroupsQueries.CREATED_BY_COLUMN.toLowerCase());
            UserEntity createdBy = userRepository.getUser(createdByGuid);
            groupEntity.setCreatedBy(createdBy);

            groupEntity.setCreatedDate(rs.getTimestamp(GroupsQueries.CREATED_DATE_COLUMN.toLowerCase()));

            String updatedByGuid = rs.getString(GroupsQueries.UPDATED_BY_COLUMN.toLowerCase());
            UserEntity updatedBy = userRepository.getUser(updatedByGuid);
            groupEntity.setUpdatedBy(updatedBy);

            groupEntity.setUpdatedDate(rs.getTimestamp(GroupsQueries.UPDATED_DATE_COLUMN.toLowerCase()));
        }


        return groupEntity;
    }
}
