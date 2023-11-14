package com.easysplit.ess.user.domain.contracts;

import com.easysplit.ess.user.domain.models.User;

/**
 * Class that handle the business logic for the user resource
 */
public interface UserService {
    /**
     * Creates a user, perform the validation for the data provided in the payload
     *
     * @param user user to be created
     * @return created user
     */
    public User createUser(User user);

    /**
     * Gets a user by its guid
     *
     * @param userGuid user id
     * @return user
     */
    public User getUser(String userGuid);
}
