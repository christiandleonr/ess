package com.easysplit.ess.user.infrastructure;

import com.easysplit.ess.user.domain.models.Friendship;
import com.easysplit.ess.user.domain.contracts.FriendsService;
import com.easysplit.ess.user.domain.contracts.UserService;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.IllegalArgumentException;
import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import com.easysplit.shared.domain.exceptions.NotFoundException;
import com.easysplit.shared.domain.models.ResourceList;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private static final String CLASS_NAME = UsersController.class.getName();
    private final String USERS_RESOURCE = "/users";
    private final UserService userService;
    private final FriendsService friendsService;
    private final InfrastructureHelper infrastructureHelper;
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    public UsersController(UserService userService,
                           FriendsService friendsService,
                           InfrastructureHelper infrastructureHelper) {
        this.userService = userService;
        this.infrastructureHelper = infrastructureHelper;
        this.friendsService = friendsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") String id) {
        User user = null;
        try {
            user = userService.getUser(id);

            user.setLinks(infrastructureHelper.buildLinks(USERS_RESOURCE, id));
        } catch (NotFoundException e) {
            logger.debug("{}.getUser() - User with id {} not found", CLASS_NAME, id);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error("{}.getUser() - Something went wrong while reading the user with id {}", CLASS_NAME, id, e);
            throw e;
        } catch (Exception e) {
            logger.error("{}.getUser() - Something went wrong while reading the user with id {}", CLASS_NAME, id, e);

            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.GET_USER_ERROR_TITLE,
                    ErrorKeys.GET_USER_ERROR_MESSAGE,
                    new Object[] {id},
                    e.getCause()
            );
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = null;
        try {
            createdUser = userService.createUser(user);

            createdUser.setLinks(infrastructureHelper.buildLinks(USERS_RESOURCE, createdUser.getId()));
        } catch (IllegalArgumentException e) {
            logger.debug("{}.createUser() - Invalid data for user: {}", CLASS_NAME, user);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error("{}.createUser() - Something went wrong while creating the user: {}", CLASS_NAME, user, e);
            throw e;
        } catch (Exception e) {
            logger.error("{}.createUser() - Something went wrong while creating the user: {}", CLASS_NAME, user, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_USER_ERROR_TITLE,
                    ErrorKeys.CREATE_USER_ERROR_MESSAGE,
                    new Object[] {user},
                    e.getCause()
            );
        }

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") String id) {
        try {
            userService.deleteUser(id);
        } catch (NotFoundException e) {
            logger.debug("{}.deleteUser() - User with id {} not found", CLASS_NAME, id);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error("{}.deleteUser() - Something went wrong while deleting the user with id {}", CLASS_NAME, id, e);
            throw e;
        } catch (Exception e) {
            logger.error("{}.deleteUser() - Something went wrong while deleting the user with id {}", CLASS_NAME, id, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.DELETE_USER_ERROR_TITLE,
                    ErrorKeys.DELETE_USER_ERROR_MESSAGE,
                    new Object[] {id},
                    e.getCause()
            );
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/friends")
    public ResponseEntity<Friendship> addFriend(Authentication auth, @RequestBody Friendship friendship) {
        Friendship createdFriendship = null;
        try {
            createdFriendship = friendsService.addFriend(
                    friendship,
                    infrastructureHelper.getAuthenticatedUserId()
            );

            createdFriendship.setLinks(
                    infrastructureHelper.buildLinks(USERS_RESOURCE, createdFriendship.getId())
            );
        } catch (NotFoundException e) {
            logger.debug("{}.addFriend() - A user from the friendship: {} was not found", CLASS_NAME, friendship);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.debug("{}.addFriend() - Illegal argument exception, most likely the friendship already exist", CLASS_NAME);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error("{}.addFriend() - Something went wrong while creating the friendship: {}", CLASS_NAME, friendship, e);
            throw e;
        } catch (Exception e) {
            logger.error("{}.addFriend() - Something went wrong while creating the friendship: {}", CLASS_NAME, friendship, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_FRIENDSHIP_ERROR_TITLE,
                    ErrorKeys.CREATE_FRIENDSHIP_ERROR_MESSAGE,
                    new Object[]{ friendship },
                    e.getCause()
            );
        }

        return new ResponseEntity<>(createdFriendship, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<ResourceList<User>> listFriends(@PathVariable(name = "id") String userId,
                                                          @RequestParam(name = "limit") int limit,
                                                          @RequestParam(name = "offset") int offset,
                                                          @RequestParam(name = "totalCount") boolean totalCount) {
        ResourceList<User> friends = null;
        try {
            friends = friendsService.listFriends(userId, limit, offset, totalCount);

            // TODO Work on links
        } catch (InternalServerErrorException e) {
            logger.error("{}.listFriends() - Something went wrong while reading the user's friends for user : {}", CLASS_NAME, userId, e);
            throw e;
        } catch (Exception e) {
            logger.error("{}.listFriends() - Something went wrong while reading the user's friends for user : {}", CLASS_NAME, userId, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.LIST_FRIENDS_ERROR_TITLE,
                    ErrorKeys.LIST_FRIENDS_ERROR_MESSAGE,
                    new Object[]{ userId },
                    e.getCause()
            );
        }

        return new ResponseEntity<>(friends, HttpStatus.OK);
    }
}
