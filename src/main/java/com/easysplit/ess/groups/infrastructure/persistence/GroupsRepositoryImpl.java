package com.easysplit.ess.groups.infrastructure.persistence;

import com.easysplit.ess.groups.domain.contracts.GroupsRepository;
import com.easysplit.ess.groups.domain.models.GroupEntity;
import com.easysplit.ess.groups.domain.sql.GroupsQueries;
import com.easysplit.ess.user.application.UserServiceImpl;
import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.ess.user.domain.sql.UserQueries;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import com.easysplit.shared.utils.EssUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
            logger.error(CLASS_NAME + ".createGroup() - Something went wrong while creating the group: " + group, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_GROUP_ERROR_MESSAGE,
                    ErrorKeys.CREATE_GROUP_ERROR_MESSAGE,
                    new Object[]{ group },
                    e
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
    @Transactional
    public List<UserEntity> addGroupMembers(String groupGuid, List<UserEntity> groupMembers) {
        List<UserEntity> members = new ArrayList<>();

        if (EssUtils.isNullOrEmpty(groupMembers)) {
            logger.debug(CLASS_NAME + ".addGroupMembers() - Empty list of members: " + groupMembers);
            return members;
        }

        for (UserEntity groupMember: groupMembers) {
            UserEntity member = addGroupMember(groupGuid, groupMember);
            members.add(member);
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
                    e
            );
        }

        logger.info(CLASS_NAME + ".deleteGroupMember() - delete from " + rowsDeleted + " groups");
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
            logger.error(CLASS_NAME + ".addGroupMember() - Something went wrong while adding the member: " + groupMember, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_GROUP_ERROR_TITLE,
                    ErrorKeys.CREATE_GROUP_ERROR_MESSAGE,
                    new Object[]{ groupMember },
                    e
            );
        }

        return member;
    }
}
