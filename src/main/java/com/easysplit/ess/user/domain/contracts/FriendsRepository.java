package com.easysplit.ess.user.domain.contracts;

import com.easysplit.ess.user.domain.models.FriendshipEntity;
import com.easysplit.ess.user.domain.models.UserEntity;

import java.util.List;

/**
 * Class that handle the database operations for the friendship resource
 */
public interface FriendsRepository {
    /**
     * Creates a new friendship between 2 users
     *
     * @param friendship friendship to be created
     * @param addedByGuid id of the user who is starting the relation
     * @return friendship
     */
    FriendshipEntity addFriend(FriendshipEntity friendship, String addedByGuid);

    /**
     * Loads all friends of a specific user from database
     *
     * @param userGuid user whose friends we want to load
     * @param limit friends to retrieve
     * @param offset from which number start retrieving
     * @return friends total count
     */
    List<UserEntity> loadFriends(String userGuid, int limit, int offset);

    /**
     * Counts how many friends a user has
     *
     * @param userGuid user whose friends we want to count
     * @return friends total count
     */
    int countFriends(String userGuid);

    /**
     * Deletes all friendships for a user that is being deleted.
     *
     * @param userGuid user whose friendships we want delete
     */
    void deleteUserFriendships(String userGuid);

    /**
     * Loads the friendship details between two users if exist
     *
     * @param friend id of user added as friend
     * @param addedBy id of user who is started the friendship
     * @return friendship
     */
    FriendshipEntity loadFriendship(String friend, String addedBy);
}
