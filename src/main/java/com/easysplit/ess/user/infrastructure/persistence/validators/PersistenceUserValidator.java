package com.easysplit.ess.user.infrastructure.persistence.validators;

import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//Class that validates that the attempted username is unique

@Component
public class PersistenceUserValidator {
    private final UserRepository userRepository;
    private final InfrastructureHelper infrastructureHelper;

    @Autowired
    public PersistenceUserValidator(UserRepository userRepository,
                                    InfrastructureHelper infrastructureHelper) {
        this.userRepository = userRepository;
        this.infrastructureHelper = infrastructureHelper;
    }

    public void validateUsernameUniqueness(String username) {
        UserEntity userEntity = userRepository.getUserByUsername(username);
        if (userEntity != null) {
            infrastructureHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_NOT_UNIQUE_USERNAME_MESSAGE,
                    new Object[]{ username }
            );
        }
    }
}
