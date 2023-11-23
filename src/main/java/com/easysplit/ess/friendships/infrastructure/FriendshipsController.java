package com.easysplit.ess.friendships.infrastructure;

import com.easysplit.ess.friendships.domain.contracts.FriendshipsService;
import com.easysplit.ess.friendships.domain.models.Friendship;
import com.easysplit.ess.friendships.domain.models.FriendshipEntity;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.exceptions.IllegalArgumentException;
import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import com.easysplit.shared.domain.exceptions.NotFoundException;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipsController {
    private final String FRIENDSHIPS_RESOURCE = "/friendships";

    private final InfrastructureHelper infrastructureHelper;
    private final FriendshipsService friendshipsService;

    public FriendshipsController(FriendshipsService friendshipsService, InfrastructureHelper infrastructureHelper) {
        this.friendshipsService = friendshipsService;
        this.infrastructureHelper = infrastructureHelper;
    }

    @PostMapping
    public ResponseEntity<Friendship> createFriendship(@RequestBody Friendship friendship) {
        try {
            Friendship createdFriendship = friendshipsService.createFriendship(friendship);

            createdFriendship.setLinks(
                    infrastructureHelper.buildLinks(FRIENDSHIPS_RESOURCE, createdFriendship.getId())
            );
            return new ResponseEntity<>(createdFriendship, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            //TODO Add logs
            throw e;
        } catch (InternalServerErrorException e) {
            //TODO Add logs
            throw e;
        } catch (Exception e) {
            //TODO Add logs
            throw new InternalServerErrorException(); // TODO Add error title, error message and cause
        }
    }
}
