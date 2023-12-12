package com.easysplit.ess.user.application;

import com.easysplit.ess.groups.domain.contracts.GroupsRepository;
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
import com.easysplit.shared.domain.models.ResourceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, FriendsService {
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final GroupsRepository groupsRepository;
    private final UserValidator userValidator;
    private final PersistenceUserValidator persistenceUserValidator;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           FriendsRepository friendsRepository,
                           GroupsRepository groupsRepository,
                           UserValidator userValidator,
                           PersistenceUserValidator persistenceUserValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.persistenceUserValidator = persistenceUserValidator;
        this.friendsRepository = friendsRepository;
        this.groupsRepository = groupsRepository;
    }

    @Override
    public User getUser(String userGuid) {
        UserEntity user = userRepository.getUser(userGuid);

        return UserMapper.INSTANCE.toUser(user);
    }

    @Override
    public User createUser(User user) {
        userValidator.validate(user);

        UserEntity createUser = UserMapper.INSTANCE.toUserEntity(user);
        persistenceUserValidator.validateUsernameUniqueness(createUser.getUsername());

        UserEntity createdUser = userRepository.createUser(createUser);
        return UserMapper.INSTANCE.toUser(createdUser);
    }

    @Override
    @Transactional
    public void deleteUser(String userGuid) {
        userRepository.deleteUserById(userGuid);
        friendsRepository.deleteUserFriendships(userGuid);
        groupsRepository.deleteGroupMember(userGuid);
    }

    @Override
    public Friendship addFriend(Friendship friendship) {
        FriendshipEntity createdFriendship = friendsRepository.addFriend(
                FriendshipsMapper.INSTANCE.toFriendshipEntity(friendship)
        );

        return FriendshipsMapper.INSTANCE.toFriendship(createdFriendship);
    }

    @Override
    public ResourceList<User> listFriends(String userGuid, int limit, int offset, boolean countFriends) {
        ResourceList<User> friendsList = new ResourceList<>();

        int totalCount = 0;
        if (countFriends) {
            totalCount = friendsRepository.countFriends(userGuid);
        }

        List<UserEntity> friends = friendsRepository.loadFriends(userGuid, limit, offset);
        int count = friends.size();

        friendsList.setLimit(limit);
        friendsList.setOffset(offset);
        friendsList.setCount(count);
        friendsList.setTotalCount(totalCount);
        friendsList.setHasMore(count > limit);
        friendsList.setData(UserMapper.INSTANCE.toListOfUsers(friends));

        return friendsList;
    }
}
