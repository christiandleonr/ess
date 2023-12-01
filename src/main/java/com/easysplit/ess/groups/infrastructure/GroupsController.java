package com.easysplit.ess.groups.infrastructure;

import com.easysplit.ess.groups.domain.contracts.GroupsService;
import com.easysplit.ess.groups.domain.models.Group;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.IllegalArgumentException;
import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GroupsController {
    private static final String CLASS_NAME = GroupsController.class.getName();
    private final GroupsService groupsService;
    private final InfrastructureHelper infrastructureHelper;
    private final String GROUPS_RESOURCE = "/groups";
    private static final Logger logger = LoggerFactory.getLogger(GroupsController.class);

    public GroupsController(GroupsService groupsService, InfrastructureHelper infrastructureHelper) {
        this.groupsService = groupsService;
        this.infrastructureHelper = infrastructureHelper;
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        Group createdGroup = null;
        try {
            createdGroup = groupsService.createGroup(group, group.getCreatedBy().getId());

            createdGroup.setLinks(infrastructureHelper.buildLinks(GROUPS_RESOURCE, createdGroup.getId()));
        } catch (IllegalArgumentException e) {
            logger.debug(CLASS_NAME + ".createGroup() - Invalid data for group: " + group);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error(CLASS_NAME + ".createGroup() - Something went wrong while creating the group: " + group, e);
            throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".createGroup() - Something went wrong while creating the group: " + group, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_GROUP_ERROR_TITLE,
                    ErrorKeys.CREATE_GROUP_ERROR_MESSAGE,
                    new Object[] {createdGroup},
                    e
            );
        }

        return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
    }

}
