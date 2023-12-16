package com.easysplit.ess.user.application;

import com.easysplit.ess.groups.domain.contracts.GroupsRepository;
import com.easysplit.ess.user.domain.models.Friendship;
import com.easysplit.ess.user.domain.models.FriendshipEntity;
import com.easysplit.ess.user.domain.contracts.FriendsRepository;
import com.easysplit.ess.user.domain.contracts.FriendsService;
import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.contracts.UserService;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.ess.user.domain.models.UserMapper;
import com.easysplit.ess.user.domain.validators.FriendshipValidator;
import com.easysplit.ess.user.domain.validators.UserValidator;
import com.easysplit.ess.user.infrastructure.persistence.validators.FriendshipsDatabaseValidator;
import com.easysplit.ess.user.infrastructure.persistence.validators.UserDatabaseValidator;
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
    private final FriendshipValidator friendshipValidator;
    private final UserDatabaseValidator userDatabaseValidator;
    private final FriendshipsDatabaseValidator friendshipsDatabaseValidator;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           FriendsRepository friendsRepository,
                           GroupsRepository groupsRepository,
                           UserValidator userValidator,
                           FriendshipValidator friendshipValidator,
                           UserDatabaseValidator userDatabaseValidator,
                           FriendshipsDatabaseValidator friendshipsDatabaseValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.friendshipValidator = friendshipValidator;
        this.userDatabaseValidator = userDatabaseValidator;
        this.friendsRepository = friendsRepository;
        this.groupsRepository = groupsRepository;
        this.friendshipsDatabaseValidator = friendshipsDatabaseValidator;
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
        userDatabaseValidator.validate(createUser);

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
        friendshipValidator.validate(friendship);

        friendshipsDatabaseValidator.validateFriendshipNotExist(
                friendship.getFriend().getId(),
                friendship.getAddedBy().getId()
        );

        FriendshipEntity createdFriendship = friendsRepository.addFriend(
                friendship.toFriendshipEntity()
        );

        return createdFriendship.toFriendship();
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
