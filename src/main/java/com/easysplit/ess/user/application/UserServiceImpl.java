package com.easysplit.ess.user.application;

import com.easysplit.ess.groups.domain.contracts.GroupsRepository;
import com.easysplit.ess.iam.domain.contracts.RefreshTokenRepository;
import com.easysplit.ess.user.domain.contracts.*;
import com.easysplit.ess.user.domain.models.Friendship;
import com.easysplit.ess.user.domain.models.FriendshipEntity;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.ess.user.domain.models.UserMapper;
import com.easysplit.ess.user.domain.validators.FriendshipValidator;
import com.easysplit.ess.user.domain.validators.UserValidator;
import com.easysplit.ess.user.infrastructure.persistence.validators.FriendshipsDatabaseValidator;
import com.easysplit.ess.user.infrastructure.persistence.validators.UserDatabaseValidator;
import com.easysplit.shared.domain.models.ResourceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, FriendsService {
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final GroupsRepository groupsRepository;
    private final RolesRepository rolesRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserValidator userValidator;
    private final FriendshipValidator friendshipValidator;
    private final UserDatabaseValidator userDatabaseValidator;
    private final FriendshipsDatabaseValidator friendshipsDatabaseValidator;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           FriendsRepository friendsRepository,
                           GroupsRepository groupsRepository,
                           RolesRepository rolesRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           UserValidator userValidator,
                           FriendshipValidator friendshipValidator,
                           UserDatabaseValidator userDatabaseValidator,
                           FriendshipsDatabaseValidator friendshipsDatabaseValidator,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.userValidator = userValidator;
        this.friendshipValidator = friendshipValidator;
        this.userDatabaseValidator = userDatabaseValidator;
        this.friendsRepository = friendsRepository;
        this.groupsRepository = groupsRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.friendshipsDatabaseValidator = friendshipsDatabaseValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUser(String userGuid) {
        UserEntity user = userRepository.getUser(userGuid);

        return user.toUser();
    }

    @Override
    public User createUser(User user) {
        userValidator.validate(user);

        UserEntity createUser = user.toUserEntity();
        userDatabaseValidator.validate(createUser);

        createUser.setPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity createdUser = userRepository.createUser(createUser);
        return createdUser.toUser();
    }

    @Override
    @Transactional
    public void deleteUser(String userGuid) {
        friendsRepository.deleteUserFriendships(userGuid);
        groupsRepository.deleteGroupMember(userGuid);
        rolesRepository.deleteUserRoles(userGuid);
        refreshTokenRepository.deleteRefreshTokenByUser(userGuid);
        userRepository.deleteUserById(userGuid);
    }

    @Override
    public Friendship addFriend(Friendship friendship, String addedById) {
        friendshipValidator.validate(friendship, addedById);

        friendshipsDatabaseValidator.validateFriendshipNotExist(
                friendship.getFriend().getId(),
                addedById
        );

        FriendshipEntity createdFriendship = friendsRepository.addFriend(
                friendship.toFriendshipEntity(),
                addedById
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
