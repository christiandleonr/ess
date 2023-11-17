package com.easysplit.ess.friendships.domain.contracts;

import com.easysplit.ess.friendships.domain.models.Friendship;

/**
 * Class that handle the business logic for the user resource
 */
public interface FriendshipsService {
    /**
     * Creates friendship between two users, perform the validation for
     * the data provided in the payload
     *
     * @param friendship user to be created
     * @return created user
     */
    Friendship createFriendship(Friendship friendship);
}
