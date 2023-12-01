package com.easysplit.ess.user.domain.contracts;

import com.easysplit.ess.user.domain.models.Friendship;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.shared.domain.models.ResourceList;

import java.util.List;

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

    /**
     * Lists all friends of a specific user from database
     *
     * @param userGuid user who we will look the friends for
     * @return list of friends
     */
    ResourceList<User> listFriends(String userGuid, int limit, int offset, boolean totalCount);
}
