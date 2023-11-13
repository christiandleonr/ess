package com.easysplit.ess.user.domain.contracts;

import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.shared.infrastructure.exceptions.InternalServerErrorException;
import com.easysplit.shared.infrastructure.exceptions.NotFoundException;

/**
 * Class that handle the database operations for the user resource
 */

public interface UserRepository {
    /**
     * Creates a new user, perform validations against db data
     *
     * @param user user data
     * @return created user
     */
    public UserEntity createUser(UserEntity user);

    /**
     * Gets a user by its guid
     *
     * @param userGuid user id
     * @return user
     */
    public UserEntity getUser(String userGuid);
}
