package com.easysplit.ess.friendships.infrastructure;

import com.easysplit.ess.user.domain.contracts.FriendsService;
import com.easysplit.ess.user.domain.models.Friendship;
import com.easysplit.ess.user.infrastructure.UserController;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import com.easysplit.shared.domain.exceptions.NotFoundException;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipsController {
    private static final String CLASS_NAME = FriendshipsController.class.getName();

    private final String FRIENDSHIPS_RESOURCE = "/friendships";

    private final InfrastructureHelper infrastructureHelper;
    private final FriendsService friendsService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public FriendshipsController(FriendsService friendsService, InfrastructureHelper infrastructureHelper) {
        this.friendsService = friendsService;
        this.infrastructureHelper = infrastructureHelper;
    }

    @PostMapping
    public ResponseEntity<Friendship> createFriendship(@RequestBody Friendship friendship) {
        Friendship createdFriendship = null;
        try {
            createdFriendship = friendsService.createFriendship(friendship);

            createdFriendship.setLinks(
                    infrastructureHelper.buildLinks(FRIENDSHIPS_RESOURCE, createdFriendship.getId())
            );
        } catch (NotFoundException e) {
            logger.debug(CLASS_NAME + ".createFriendship() - A user from the friendship: " + friendship + " was not found");
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error(CLASS_NAME + ".createFriendship() - Something went wrong while creating the friendship: " + friendship, e);
            throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".createFriendship() - Something went wrong while creating the friendship: " + friendship, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_FRIENDSHIP_ERROR_TITLE,
                    ErrorKeys.CREATE_FRIENDSHIP_ERROR_MESSAGE,
                    new Object[]{ friendship },
                    e
            );
        }

        return new ResponseEntity<>(createdFriendship, HttpStatus.CREATED);
    }
}
