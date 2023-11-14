package com.easysplit.ess.user.infrastructure.persistence.validators;

import com.easysplit.ess.user.domain.contracts.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PersistenceUserValidator {
    private final UserRepository userRepository;

    @Autowired
    public PersistenceUserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUsernameUniqueness(String username) {
        if (!userRepository.validateUsernameNotExist(username)) {

        }
    }
}
