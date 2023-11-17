package com.easysplit.ess.friendships.domain.contracts;

import com.easysplit.ess.friendships.domain.models.FriendshipEntity;

/**
 * Class that handle the database operations for the friendship resource
 */
public interface FriendshipsRepository {
    /**
     * Creates a new friendship between 2 users
     *
     * @param friendship friendship to be created
     * @return friendship
     */
    FriendshipEntity createFriendship(FriendshipEntity friendship);
}
