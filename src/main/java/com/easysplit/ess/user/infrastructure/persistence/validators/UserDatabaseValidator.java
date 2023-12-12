package com.easysplit.ess.user.infrastructure.persistence.validators;

import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDatabaseValidator {
    private final UserRepository userRepository;
    private final InfrastructureHelper infrastructureHelper;

    @Autowired
    public UserDatabaseValidator(UserRepository userRepository,
                                 InfrastructureHelper infrastructureHelper) {
        this.userRepository = userRepository;
        this.infrastructureHelper = infrastructureHelper;
    }

    /**
     * Validate user to be created again DB data.
     * Validates username, email and phone uniqueness.
     */
    public void validate(UserEntity userEntity) {
        if (userEntity == null) {
            return;
        }

        validateUsernameUniqueness(userEntity.getUsername());
        validateUserEmailUniqueness(userEntity.getEmail());
        validateUserPhoneUniqueness(userEntity.getPhone());
    }

    /**
     * Validate username uniqueness against DB data
     *
     * @param username user username
     */
    private void validateUsernameUniqueness(String username) {
        UserEntity userEntity = userRepository.getUserByUsername(username);
        if (userEntity != null) {
            infrastructureHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_NOT_UNIQUE_USERNAME_MESSAGE,
                    new Object[]{ username }
            );
        }
    }

    /**
     * Validate email uniqueness against DB data
     *
     * @param email user email
     */
    private void validateUserEmailUniqueness(String email) {
        UserEntity userEntity = userRepository.getUserByEmail(email);
        if (userEntity != null) {
            infrastructureHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_NOT_UNIQUE_USERNAME_MESSAGE,
                    new Object[]{ email }
            );
        }
    }

    /**
     * Validate phone uniqueness against DB data
     *
     * @param phone user phone
     */
    private void validateUserPhoneUniqueness(String phone) {
        UserEntity userEntity = userRepository.getUserByPhone(phone);
        if (userEntity != null) {
            infrastructureHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_NOT_UNIQUE_USERNAME_MESSAGE,
                    new Object[]{ phone }
            );
        }
    }
}
