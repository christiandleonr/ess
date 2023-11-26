package com.easysplit.ess.user.domain.contracts;

import com.easysplit.ess.user.domain.models.FriendshipEntity;

/**
 * Class that handle the database operations for the friendship resource
 */
public interface FriendsRepository {
    /**
     * Creates a new friendship between 2 users
     *
     * @param friendship friendship to be created
     * @return friendship
     */
    FriendshipEntity addFriend(FriendshipEntity friendship);
}
