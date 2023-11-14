package com.easysplit.ess.user.domain.contracts;

import com.easysplit.ess.user.domain.models.UserEntity;

/**
 * Class that handle the database operations for the user resource
 */

public interface UserRepository {
    /**
     * Creates a new user
     *
     * @param user user data
     * @return created user
     */
    UserEntity createUser(UserEntity user);

    /**
     * Gets a user by its guid
     *
     * @param userGuid user id
     * @return user
     */
    UserEntity getUser(String userGuid);

    /**
     * Validates that the username do not exist in DB.
     * Throws an exception if the username exist
     *
     * @param username username to be validated
     */
    UserEntity getUserByUsername(String username);

    /**
     * Deletes user by its guid
     *
     * @param userGuid user id
     */
    void deleteUserById(String userGuid);
}
