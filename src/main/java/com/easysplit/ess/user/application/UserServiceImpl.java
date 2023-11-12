package com.easysplit.ess.user.application;

import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.contracts.UserService;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.ess.user.domain.models.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User getUser(String userGuid) {
        UserEntity user = userRepository.getUser(userGuid);

        return userMapper.toUser(user);
    }

    @Override
    public User createUser(User user) {
        UserEntity createdUser = userRepository.createUser(userMapper.toUserEntity(user));

        return userMapper.toUser(createdUser);
    }
}
