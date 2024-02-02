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
     * Gets user by its username if exist
     *
     * @param username username
     */
    UserEntity getUserByUsername(String username, boolean throwException);

    /**
     * Gets user by its email if exist
     *
     * @param email user email
     */
    UserEntity getUserByEmail(String email, boolean throwException);

    /**
     * Gets user by its phone number if exist
     *
     * @param phone user phone number
     */
    UserEntity getUserByPhone(String phone, boolean throwException);

    /**
     * Deletes user by its guid
     *
     * @param userGuid user id
     */
    void deleteUserById(String userGuid);
}
