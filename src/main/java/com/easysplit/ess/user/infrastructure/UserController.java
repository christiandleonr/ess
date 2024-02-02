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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final String CLASS_NAME = UserController.class.getName();
    private final String USERS_RESOURCE = "/users";
    private final UserService userService;
    private final FriendsService friendsService;
    private final InfrastructureHelper infrastructureHelper;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService,
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
            logger.debug(CLASS_NAME + ".getUser() - User with id " + id + " not found");
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error(CLASS_NAME + ".getUser() - Something went wrong while reading the user with id " + id, e);
            throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".getUser() - Something went wrong while reading the user with id " + id, e);

            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.GET_USER_ERROR_TITLE,
                    ErrorKeys.GET_USER_ERROR_MESSAGE,
                    new Object[] {id},
                    e
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
            logger.debug(CLASS_NAME + ".createUser() - Invalid data for user: " + user);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error(CLASS_NAME + ".createUser() - Something went wrong while creating the user: " + user, e);
            throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".createUser() - Something went wrong while creating the user: " + user, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_USER_ERROR_TITLE,
                    ErrorKeys.CREATE_USER_ERROR_MESSAGE,
                    new Object[] {user},
                    e
            );
        }

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") String id) {
        try {
            userService.deleteUser(id);
        } catch (NotFoundException e) {
            logger.debug(CLASS_NAME + ".deleteUser() - User with id " + id + " not found");
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error(CLASS_NAME + ".deleteUser() - Something went wrong while deleting the user with id " + id, e);
            throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".deleteUser() - Something went wrong while deleting the user with id " + id, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.DELETE_USER_ERROR_TITLE,
                    ErrorKeys.DELETE_USER_ERROR_MESSAGE,
                    new Object[] {id},
                    e
            );
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/friends")
    public ResponseEntity<Friendship> addFriend(@RequestBody Friendship friendship) {
        Friendship createdFriendship = null;
        try {
            createdFriendship = friendsService.addFriend(friendship);

            createdFriendship.setLinks(
                    infrastructureHelper.buildLinks(USERS_RESOURCE, createdFriendship.getId())
            );
        } catch (NotFoundException e) {
            logger.debug(CLASS_NAME + ".addFriend() - A user from the friendship: " + friendship + " was not found");
            throw e;
        } catch (IllegalArgumentException e) {
            logger.debug(CLASS_NAME + ".addFriend() - Illegal argument exception, most likely the friendship already exist");
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error(CLASS_NAME + ".addFriend() - Something went wrong while creating the friendship: " + friendship, e);
            throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".addFriend() - Something went wrong while creating the friendship: " + friendship, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_FRIENDSHIP_ERROR_TITLE,
                    ErrorKeys.CREATE_FRIENDSHIP_ERROR_MESSAGE,
                    new Object[]{ friendship },
                    e
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
            logger.error(CLASS_NAME + ".listFriends() - Something went wrong while reading the user's friends for user : " + userId, e);
            throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".listFriends() - Something went wrong while reading the user's friends for user : " + userId, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.LIST_FRIENDS_ERROR_TITLE,
                    ErrorKeys.LIST_FRIENDS_ERROR_MESSAGE,
                    new Object[]{ userId },
                    e
            );
        }

        return new ResponseEntity<>(friends, HttpStatus.OK);
    }
}
