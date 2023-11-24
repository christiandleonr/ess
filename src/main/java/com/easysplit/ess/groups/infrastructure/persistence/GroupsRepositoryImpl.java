package com.easysplit.ess.groups.infrastructure.persistence;

import com.easysplit.ess.groups.domain.contracts.GroupsRepository;
import com.easysplit.ess.groups.domain.models.GroupEntity;
import com.easysplit.ess.groups.domain.models.GroupMemberEntity;
import com.easysplit.ess.groups.domain.sql.GroupsQueries;
import com.easysplit.ess.user.application.UserServiceImpl;
import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import com.easysplit.shared.utils.EssUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
    public GroupEntity createGroup(GroupEntity group) {
        String groupGuid = UUID.randomUUID().toString();
        Timestamp createdDate = infrastructureHelper.getCurrentDate();

        try {
            jdbc.update(GroupsQueries.INSERT_GROUP,
                    groupGuid,
                    group.getName(),
                    group.getDescription(),
                    group.getCreatedBy(),
                    createdDate,
                    group.getUpdatedBy(),
                    createdDate
            );
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".createGroup() - Something went wrong while creating the group: " + group, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_USER_ERROR_TITLE,
                    ErrorKeys.CREATE_USER_ERROR_MESSAGE,
                    new Object[]{ group },
                    e
            );
        }

        group.setGroupGuid(groupGuid);
        group.setCreatedDate(createdDate);
        group.setUpdatedDate(createdDate);

        return group;
    }

    @Override
    public List<UserEntity> addGroupMembers(List<GroupMemberEntity> groupMembers) {
        List<UserEntity> members = new ArrayList<>();

        if (EssUtils.isNullOrEmpty(groupMembers)) {
            logger.debug(CLASS_NAME + ".addGroupMembers() - Empty list of members: " + groupMembers);
            return members;
        }

        for (GroupMemberEntity groupMember: groupMembers) {
            UserEntity member = addGroupMember(groupMember);
            members.add(member);
        }

        return members;
    }

    @Override
    public UserEntity addGroupMember(GroupMemberEntity groupMember) {
        if (groupMember == null) {
            return null;
        }

        // Throws NotFoundException if the user does not exist
        UserEntity member = userRepository.getUser(groupMember.getMemberGuid());

        try {
            jdbc.update(GroupsQueries.INSERT_GROUP_MEMBER,
                    groupMember.getGroupGuid(),
                    member.getUserGuid()
            );
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".addGroupMember() - Something went wrong while adding the member: " + groupMember, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_USER_ERROR_TITLE,
                    ErrorKeys.CREATE_USER_ERROR_MESSAGE,
                    new Object[]{ groupMember },
                    e
            );
        }

        return member;
    }
}
