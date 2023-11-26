package com.easysplit.ess.user.domain.contracts;

import com.easysplit.ess.user.domain.models.Friendship;

/**
 * Class that handle the business logic for the user resource
 */
public interface FriendsService {
    /**
     * Creates friendship between two users, perform the validation for
     * the data provided in the payload
     *
     * @param friendship user to be created
     * @return created user
     */
    Friendship addFriend(Friendship friendship);
}
