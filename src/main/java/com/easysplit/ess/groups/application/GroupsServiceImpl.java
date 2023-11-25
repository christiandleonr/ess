package com.easysplit.ess.groups.application;

import com.easysplit.ess.groups.domain.contracts.GroupsRepository;
import com.easysplit.ess.groups.domain.contracts.GroupsService;
import com.easysplit.ess.groups.domain.models.Group;
import com.easysplit.ess.groups.domain.models.GroupEntity;
import com.easysplit.ess.groups.domain.models.GroupMapper;
import com.easysplit.ess.groups.domain.models.GroupMemberEntity;
import com.easysplit.ess.groups.domain.validators.GroupValidator;
import com.easysplit.ess.user.application.UserServiceImpl;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.ess.user.domain.models.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupsServiceImpl implements GroupsService {
    private final GroupsRepository groupsRepository;
    private final GroupMapper groupMapper;
    private final GroupValidator groupValidator;
    private static final Logger logger = LoggerFactory.getLogger(GroupsServiceImpl.class);

    @Autowired
    public GroupsServiceImpl(GroupsRepository groupsRepository,
                             GroupMapper groupMapper,
                             GroupValidator groupValidator) {
        this.groupsRepository = groupsRepository;
        this.groupMapper = groupMapper;
        this.groupValidator = groupValidator;
    }

    @Override
    public Group createGroup(Group group, String createdBy) {
        groupValidator.validate(group);

        GroupEntity createdGroupEntity = groupsRepository.createGroup(
                createdBy,
                group.toGroupEntity()
        );

        return createdGroupEntity.toGroup();
    }
}
