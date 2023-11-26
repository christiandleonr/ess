package com.easysplit.ess.user.application;

import com.easysplit.ess.user.domain.models.Friendship;
import com.easysplit.ess.user.domain.models.FriendshipEntity;
import com.easysplit.ess.user.domain.contracts.FriendsRepository;
import com.easysplit.ess.user.domain.contracts.FriendsService;
import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.contracts.UserService;
import com.easysplit.ess.user.domain.models.FriendshipsMapper;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.ess.user.domain.models.UserMapper;
import com.easysplit.ess.user.domain.validators.UserValidator;
import com.easysplit.ess.user.infrastructure.persistence.validators.PersistenceUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, FriendsService {
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final UserValidator userValidator;
    private final PersistenceUserValidator persistenceUserValidator;
    private final UserMapper userMapper;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           FriendsRepository friendsRepository,
                           UserMapper userMapper,
                           UserValidator userValidator,
                           PersistenceUserValidator persistenceUserValidator) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
        this.persistenceUserValidator = persistenceUserValidator;
        this.friendsRepository = friendsRepository;
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

    @Override
    public Friendship addFriend(Friendship friendship) {
        FriendshipEntity createdFriendship = friendsRepository.addFriend(
                FriendshipsMapper.INSTANCE.toFriendshipEntity(friendship)
        );

        return FriendshipsMapper.INSTANCE.toFriendship(createdFriendship);
    }
}
