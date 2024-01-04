package com.easysplit.ess.groups.application;

import com.easysplit.ess.groups.domain.contracts.GroupsRepository;
import com.easysplit.ess.groups.domain.contracts.GroupsService;
import com.easysplit.ess.groups.domain.models.Group;
import com.easysplit.ess.groups.domain.models.GroupEntity;
import com.easysplit.ess.groups.domain.validators.GroupValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupsServiceImpl implements GroupsService {
    private final GroupsRepository groupsRepository;
    private final GroupValidator groupValidator;

    @Autowired
    public GroupsServiceImpl(GroupsRepository groupsRepository,
                             GroupValidator groupValidator) {
        this.groupsRepository = groupsRepository;
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

    @Override
    public Group getGroup(String groupGuid) {
        GroupEntity group = groupsRepository.getGroup(groupGuid);
        group.setMembers(
                groupsRepository.getGroupMembers(groupGuid)
        );

        return group.toGroup();
    }

    @Override
    @Transactional
    public void deleteGroup(String groupGuid){
        groupsRepository.deleteAllGroupMembers(groupGuid);
        groupsRepository.deleteGroup(groupGuid);

    }
}
