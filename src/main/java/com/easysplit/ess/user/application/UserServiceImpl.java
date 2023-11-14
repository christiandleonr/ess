package com.easysplit.ess.user.application;

import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.contracts.UserService;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.ess.user.domain.models.UserMapper;
import com.easysplit.ess.user.domain.validators.UserValidator;
import com.easysplit.ess.user.infrastructure.persistence.validators.PersistenceUserValidator;
import com.easysplit.shared.domain.helpers.DomainHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PersistenceUserValidator persistenceUserValidator;
    private final UserMapper userMapper;
    private final DomainHelper domainHelper;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           UserValidator userValidator,
                           PersistenceUserValidator persistenceUserValidator,
                           DomainHelper domainHelper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
        this.persistenceUserValidator = persistenceUserValidator;
        this.domainHelper = domainHelper;
    }

    @Override
    public User getUser(String userGuid) {
        UserEntity user = userRepository.getUser(userGuid);

        return userMapper.toUser(user);
    }

    @Override
    public User createUser(User user) {
        userValidator.validate(user);

        UserEntity createUser = userMapper.toUserEntity(user);
        persistenceUserValidator.validateUsernameUniqueness(createUser.getUsername());

        UserEntity createdUser = userRepository.createUser(createUser);
        return userMapper.toUser(createdUser);
    }

    @Override
    public void deleteUser(String userGuid) {
        userRepository.deleteUserById(userGuid);
    }
}
