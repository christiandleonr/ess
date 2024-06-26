package com.easysplit.ess.groups.infrastructure;

import com.easysplit.ess.groups.domain.contracts.GroupsService;
import com.easysplit.ess.groups.domain.models.Group;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.IllegalArgumentException;
import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import com.easysplit.shared.domain.exceptions.NotFoundException;
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
            String createdById = infrastructureHelper.getAuthenticatedUserId();
            createdGroup = groupsService.createGroup(group, createdById);

            createdGroup.setLinks(infrastructureHelper.buildLinks(GROUPS_RESOURCE, createdGroup.getId()));
        } catch (NotFoundException e) {
            logger.debug("{}.createGroup() - Resource not found for group: {}", CLASS_NAME, group);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.debug("{}.createGroup() - Invalid data for group: {}", CLASS_NAME, group);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error("{}.createGroup() - Something went wrong while creating the group: {}", CLASS_NAME, group, e);
            throw e;
        } catch (Exception e) {
            logger.error("{}.createGroup() - Something went wrong while creating the group: {}", CLASS_NAME, group, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_GROUP_ERROR_TITLE,
                    ErrorKeys.CREATE_GROUP_ERROR_MESSAGE,
                    new Object[] {group},
                    e.getCause()
            );
        }

        return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable(name = "id") String id){
        try{
            groupsService.deleteGroup(id);
        }catch (NotFoundException e) {
            logger.debug("{}.deleteGroup() - group with id {} not found", CLASS_NAME, id);
            throw e;
        }catch (InternalServerErrorException e) {
            logger.error("{}.deleteGroup() - Something went wrong while deleting the group with id {}", CLASS_NAME, id, e);
            throw e;
        }catch (Exception e) {
            logger.error("{}.deleteGroup() - Something went wrong while deleting the group with id {}", CLASS_NAME, id, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.DELETE_GROUP_ERROR_TITLE,
                    ErrorKeys.DELETE_GROUP_ERROR_MESSAGE,
                    new Object[] {id},
                    e.getCause()
            );
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroup(@PathVariable(name = "id") String id) {
        Group group = null;
        try {
            group = groupsService.getGroup(id);

            group.setLinks(infrastructureHelper.buildLinks(GROUPS_RESOURCE, id));
        } catch (NotFoundException e) {
            logger.debug("{}.getGroup() - Group with id {} not found", CLASS_NAME, id);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error("{}.getGroup() - Something went wrong while reading the group with id {}", CLASS_NAME, id, e);
            throw e;
        } catch (Exception e) {
            logger.error("{}.getGroup() - Something went wrong while reading the group with id {}", CLASS_NAME, id, e);

            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.GET_USER_ERROR_TITLE,
                    ErrorKeys.GET_USER_ERROR_MESSAGE,
                    new Object[] {id},
                    e.getCause()
            );
        }

        return new ResponseEntity<>(group, HttpStatus.OK);
    }

}
