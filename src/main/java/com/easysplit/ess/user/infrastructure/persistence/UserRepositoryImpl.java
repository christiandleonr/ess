package com.easysplit.ess.user.infrastructure.persistence;

import com.easysplit.ess.user.domain.contracts.RolesRepository;
import com.easysplit.ess.user.domain.models.*;
import com.easysplit.ess.user.domain.sql.FriendshipsQueries;
import com.easysplit.ess.user.domain.contracts.FriendsRepository;
import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.sql.RolesQueries;
import com.easysplit.ess.user.domain.sql.UserQueries;
import com.easysplit.ess.user.domain.sql.UserRolesQueries;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import com.easysplit.shared.utils.EssUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository, RolesRepository, FriendsRepository {
    private static final String CLASS_NAME = UserRepositoryImpl.class.getName();
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private final JdbcTemplate jdbc;
    private final InfrastructureHelper infrastructureHelper;
    private static final String ES_USER_ROLEGUID = "93605fa8-0fd6-4d1a-a0a5-80f4d892b091";

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbc, InfrastructureHelper infrastructureHelper){
        this.jdbc = jdbc;
        this.infrastructureHelper = infrastructureHelper;
    }

    @Override
    @Transactional
    public UserEntity createUser(UserEntity user) throws InternalServerErrorException {
        String userGuid = UUID.randomUUID().toString();
        Timestamp createdDate = infrastructureHelper.getCurrentDate();

        try {
            jdbc.update(UserQueries.INSERT_USER,
                    userGuid,
                    user.getName(),
                    user.getLastname(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getPhone(),
                    createdDate);
        } catch (Exception e) {
            logger.error("{}.createUser() - Something went wrong while creating the user: {}", CLASS_NAME, user, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_USER_ERROR_TITLE,
                    ErrorKeys.CREATE_USER_ERROR_MESSAGE,
                    new Object[]{user},
                    e.getCause()
            );
        }

        user.setUserGuid(userGuid);
        user.setCreatedDate(createdDate);

        /**
         * Handle user roles - TODO Think a better way of doing this
         */
        insertUserRole(new UserRoleEntity(userGuid, ES_USER_ROLEGUID));
        user.setRoles(getRoles(userGuid));

        return user;
    }

    @Override
    public UserEntity getUser(String userGuid) {
        UserEntity userEntity = null;

        if (EssUtils.isNullOrEmpty(userGuid)) {
            return null;
        }

        try {
            userEntity = jdbc.query(UserQueries.GET_USER,
                    this::toUserEntity,
                    userGuid);
        } catch (Exception e) {
            logger.error("{}.getUser() - Something went wrong while reading the user with id: {}", CLASS_NAME, userGuid, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.GET_USER_ERROR_TITLE,
                    ErrorKeys.GET_USER_ERROR_MESSAGE,
                    new Object[] {userGuid},
                    e.getCause()
            );
        }

        if (userEntity == null) {
            logger.debug("{}.getUser() - User with id {} not found", CLASS_NAME, userGuid);
            infrastructureHelper.throwNotFoundException(
                    ErrorKeys.GET_USER_NOT_FOUND_TITLE,
                    ErrorKeys.GET_USER_NOT_FOUND_MESSAGE,
                    new Object[]{userGuid}
            );
        }

        return userEntity;
    }

    @Override
    public UserEntity getUserByUsername(String username, boolean throwException) {
        UserEntity userEntity = null;

        try {
            userEntity = jdbc.query(UserQueries.GET_USER_BY_USERNAME,
                    this::toUserEntity,
                    username);
        } catch (Exception e) {
            logger.error("{}.getUserByUsername() - Something went wrong while reading the user with username: {}", CLASS_NAME, username, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_USER_ERROR_TITLE,
                    ErrorKeys.CREATE_USER_BY_USERNAME_MESSAGE,
                    new Object[] {username},
                    e.getCause()
            );
        }

        if (throwException && userEntity == null) {
            logger.debug("{}.getUserByUsername() - User with username {} not found", CLASS_NAME, username);
            infrastructureHelper.throwNotFoundException(
                    ErrorKeys.GET_USER_NOT_FOUND_TITLE,
                    ErrorKeys.GET_USER_NOT_FOUND_MESSAGE,
                    new Object[]{username} // TODO Work on error message to specify it was looked by username
            );
        }

        return userEntity;
    }

    @Override
    public UserEntity getUserByEmail(String email, boolean throwException) {
        UserEntity userEntity = null;

        try {
            userEntity = jdbc.query(UserQueries.GET_USER_BY_EMAIL,
                    this::toUserEntity,
                    email);
        } catch (Exception e) {
            logger.error("{}.getUser() - Something went wrong while reading the user with email: {}", CLASS_NAME, email, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_USER_ERROR_TITLE,
                    ErrorKeys.CREATE_USER_BY_EMAIL_MESSAGE,
                    new Object[] {email},
                    e.getCause()
            );
        }

        if (throwException && userEntity == null) {
            logger.debug("{}.getUser() - User with email {} not found", CLASS_NAME, email);
            infrastructureHelper.throwNotFoundException(
                    ErrorKeys.GET_USER_NOT_FOUND_TITLE,
                    ErrorKeys.GET_USER_NOT_FOUND_MESSAGE,
                    new Object[]{email} // TODO Work on error message to specify it was looked by email
            );
        }

        return userEntity;
    }

    @Override
    public UserEntity getUserByPhone(String phone, boolean throwException) {
        UserEntity userEntity = null;

        try {
            userEntity = jdbc.query(UserQueries.GET_USER_BY_PHONE,
                    this::toUserEntity,
                    phone);
        } catch (Exception e) {
            logger.error("{}.getUser() - Something went wrong while reading the user with phone: {}", CLASS_NAME, phone, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_USER_ERROR_TITLE,
                    ErrorKeys.CREATE_USER_BY_PHONE_MESSAGE,
                    new Object[] {phone},
                    e.getCause()
            );
        }

        if (throwException && userEntity == null) {
            logger.debug("{}.getUser() - User with username {} not found", CLASS_NAME, phone);
            infrastructureHelper.throwNotFoundException(
                    ErrorKeys.GET_USER_NOT_FOUND_TITLE,
                    ErrorKeys.GET_USER_NOT_FOUND_MESSAGE,
                    new Object[]{phone} // TODO Work on error message to specify it was looked by phone
            );
        }

        return userEntity;
    }

    @Override
    @Transactional
    public void deleteUserById(String userGuid) {
        // Throws a NotFoundException if user does not exist
        getUser(userGuid);

        int rowsDeleted = 0;
        try {
            rowsDeleted = jdbc.update(UserQueries.DELETE_USER_BY_ID, userGuid);
        } catch (Exception e) {
            logger.error("{}.deleteRefreshToken() - Something went wrong while deleting the user with id: {}", CLASS_NAME, userGuid, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.DELETE_USER_ERROR_TITLE,
                    ErrorKeys.DELETE_USER_ERROR_MESSAGE,
                    new Object[] {userGuid},
                    e.getCause()
            );
        }

        logger.info("{}.deleteUserById() - Users deleted: {}", CLASS_NAME, rowsDeleted);
    }

    @Override
    @Transactional
    public void insertUserRole(UserRoleEntity userRoleEntity) {
        try {
            jdbc.update(UserRolesQueries.INSERT_USER_ROLE,
                    userRoleEntity.getUserGuid(),
                    userRoleEntity.getRoleGuid()
            );
        } catch (Exception e) {
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.ASSIGN_ROLES_ERROR_TILE,
                    ErrorKeys.ASSIGN_ROLES_ERROR_MESSAGE,
                    null
            );
        }
    }

    @Override
    public List<RoleEntity> getRoles(String userGuid) {
        List<RoleEntity> roles = new ArrayList<>();
        try {
            roles = jdbc.query(RolesQueries.GET_USER_ROLES,
                    this::toRoles,
                    userGuid);
        } catch (Exception e) {
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.DELETE_ROLES_ERROR_TILE,
                    ErrorKeys.DELETE_ROLES_ERROR_MESSAGE,
                    null
            );
        }

        return roles;
    }

    @Override
    public void deleteUserRoles(String userGuid) {
        int rowsDeleted = 0;
        try {
            rowsDeleted = jdbc.update(UserRolesQueries.DELETE_USER_ROLES, userGuid);
        } catch (Exception e) {
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.DELETE_USER_ERROR_TITLE,
                    ErrorKeys.DELETE_USER_ERROR_MESSAGE,
                    new Object[] {userGuid},
                    e.getCause()
            );
        }

        logger.info("{}.deleteUserRoles() - User friendships deleted: {}", CLASS_NAME, rowsDeleted);
    }

    @Override
    @Transactional
    public FriendshipEntity addFriend(FriendshipEntity friendship, String addedByGuid) throws InternalServerErrorException {
        String friendshipGuid = UUID.randomUUID().toString();
        Timestamp createdDate = infrastructureHelper.getCurrentDate();

        // Throws NotFoundException if any of both users is not found
        UserEntity friend = getUser(friendship.getFriend().getUserGuid());
        UserEntity addedBy = getUser(addedByGuid);

        try {
            jdbc.update(FriendshipsQueries.CREATE_FRIENDSHIP,
                    friendshipGuid,
                    friend.getUserGuid(),
                    FriendshipStatus.PENDING.getValue(),
                    createdDate,
                    addedByGuid
            );
        } catch (Exception e) {
            logger.error("{}.createFriendship() - Something went wrong while creating the friendship: {}", CLASS_NAME, friendship, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_FRIENDSHIP_ERROR_TITLE,
                    ErrorKeys.CREATE_FRIENDSHIP_ERROR_MESSAGE,
                    new Object[]{ friendship },
                    e.getCause()
            );
        }

        friendship.setFriendshipGuid(friendshipGuid);
        friendship.setFriend(friend);
        friendship.setStatus(FriendshipStatus.PENDING);
        friendship.setCreatedDate(createdDate);
        friendship.setAddedBy(addedBy);

        return friendship;
    }

    @Override
    public List<UserEntity> loadFriends(String userGuid, int limit, int offset) {
        List<UserEntity> friends = new ArrayList<>();

        if (EssUtils.isNullOrEmpty(userGuid)) {
            return friends;
        }

        try {
            List<String> friendsIds = jdbc.query(FriendshipsQueries.GET_FRIENDS,
                    (rs, rowNum) -> rs.getString(UserQueries.USERGUID_COLUMN),
                    userGuid, userGuid, userGuid, limit, offset);

            for (String friendId: friendsIds) {
                friends.add(getUser(friendId));
            }
        } catch (Exception e) {
            logger.error("{}.loadFriends() - Something went wrong while reading the user's friends for user with id: {}", CLASS_NAME, userGuid, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.LIST_FRIENDS_ERROR_TITLE,
                    ErrorKeys.LIST_FRIENDS_ERROR_MESSAGE,
                    new Object[] {userGuid},
                    e.getCause()
            );
        }

        return friends;
    }

    @Override
    public int countFriends(String userGuid) {
        int totalCount = 0;
        if (EssUtils.isNullOrEmpty(userGuid)) {
            return totalCount;
        }

        try {
            totalCount = jdbc.query(FriendshipsQueries.COUNT_FRIENDS,
                    (preparedStatement) -> {
                        preparedStatement.setString(1, userGuid);
                    }, (rs) -> {
                        if (!rs.next()) {
                            return 0;
                        }
                        return rs.getInt(1);
                    });
        } catch (Exception e) {
            logger.error("{}.countFriends() - Something went wrong while the user's friends for user with id: {}", CLASS_NAME, userGuid, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.LIST_FRIENDS_ERROR_TITLE,
                    ErrorKeys.LIST_FRIENDS_ERROR_MESSAGE,
                    new Object[] {userGuid},
                    e.getCause()
            );
        }

        return totalCount;
    }

    @Override
    @Transactional
    public void deleteUserFriendships(String userGuid) {
        // Throws a NotFoundException if user does not exist
        getUser(userGuid);

        int rowsDeleted = 0;
        try {
            rowsDeleted = jdbc.update(FriendshipsQueries.DELETE_USER_FRIENDSHIPS, userGuid);
        } catch (Exception e) {
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.DELETE_USER_ERROR_TITLE,
                    ErrorKeys.DELETE_USER_ERROR_MESSAGE,
                    new Object[] {userGuid},
                    e.getCause()
            );
        }

        logger.info("{}.deleteUserFriendships() - User friendships deleted: {}", CLASS_NAME, rowsDeleted);
    }

    @Override
    public FriendshipEntity loadFriendship(String friend, String addedBy) {
        FriendshipEntity friendshipEntity = null;

        if (EssUtils.isNullOrEmpty(friend) || EssUtils.isNullOrEmpty(addedBy)) {
            return null;
        }

        try {
            friendshipEntity = jdbc.query(FriendshipsQueries.LOAD_FRIENDSHIP_DETAILS,
                    this::toFriendshipEntity,
                    friend, addedBy);
        } catch (Exception e) {
            logger.error("{}.loadFriendship() - Something went wrong while loading the friendship details with friend: {} and addedBy: {}", CLASS_NAME, friend, addedBy, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.LOAD_FRIENDSHIP_ERROR_TITLE,
                    ErrorKeys.LOAD_FRIENDSHIP_ERROR_MESSAGE,
                    new Object[] {friend, addedBy},
                    e.getCause()
            );
        }

        return friendshipEntity;
    }

    /**
     * Generates a list of user from a result set. Executes the method next() in loop to
     * go through all the rows
     *
     * @param rs result set
     * @return list of users
     */
    private List<UserEntity> toUserEntities(ResultSet rs) throws SQLException {
        List<UserEntity> users = new ArrayList<>();

        while(rs.next()) {
            users.add(buildEntity(rs));
        }

        return users;
    }

    /**
     * Generates a user from a result set, executes the method next()
     * to jump to the first row
     *
     * @param rs result set
     * @return user
     */
    private UserEntity toUserEntity(ResultSet rs) throws SQLException {
        UserEntity userEntity = null;

        if (rs.next()) {
            userEntity = buildEntity(rs);
        }

        return userEntity;
    }

    /**
     * Builds a user entity from a result set
     *
     * @param rs result set
     * @return user entity created from a result set
     */
    private UserEntity buildEntity(ResultSet rs) throws SQLException {

        UserEntity userEntity = new UserEntity();

        String userGuid = rs.getString(UserQueries.USERGUID_COLUMN.toLowerCase());

        userEntity.setUserGuid(userGuid);
        userEntity.setName(rs.getString(UserQueries.NAME_COLUMN.toLowerCase()));
        userEntity.setLastname(rs.getString(UserQueries.LASTNAME_COLUMN.toLowerCase()));
        userEntity.setUsername(rs.getString(UserQueries.USERNAME_COLUMN.toLowerCase()));
        userEntity.setPassword(rs.getString(UserQueries.PASSWORD_COLUMN.toLowerCase()));
        userEntity.setEmail(rs.getString(UserQueries.EMAIL_COLUMN.toLowerCase()));
        userEntity.setPhone(rs.getString(UserQueries.PHONE_COLUMN.toLowerCase()));
        userEntity.setCreatedDate(rs.getTimestamp(UserQueries.CREATE_DATE_COLUMN.toLowerCase()));

        userEntity.setRoles(
            getRoles(userGuid)
        );

        return userEntity;
    }

    private List<RoleEntity> toRoles(ResultSet rs) throws SQLException {
        List<RoleEntity> roles = new ArrayList<>();

        while (rs.next()) {
            roles.add(buildRoleEntity(rs));
        }

        return roles;
    }

    /**
     * Build a role entity from a result set
     *
     * @param rs result set
     * @return role entity
     */
    private RoleEntity buildRoleEntity(ResultSet rs) throws SQLException {
        RoleEntity roleEntity = new RoleEntity();

        roleEntity.setRoleGuid(rs.getString(RolesQueries.ROLEGUID_COLUMN).toLowerCase());
        roleEntity.setName(rs.getString(RolesQueries.NAME_COLUMN).toLowerCase());

        return roleEntity;
    }

    /**
     * Build a friendship entity from a result set
     *
     * @param rs result set from which the friendship entity is build
     * @return friendship entity
     */
    private FriendshipEntity toFriendshipEntity(ResultSet rs) throws SQLException {
        FriendshipEntity friendshipEntity = null;

        if (rs.next()) {
            friendshipEntity = new FriendshipEntity();

            friendshipEntity.setFriendshipGuid(rs.getString(FriendshipsQueries.FRIENDSHIPGUID_COLUMN.toLowerCase()));

            String friendGuid = rs.getString(FriendshipsQueries.FRIEND_COLUMN.toLowerCase());
            UserEntity friend = getUser(friendGuid);
            friendshipEntity.setFriend(friend);

            String status = rs.getString(FriendshipsQueries.STATUS_COLUMN.toLowerCase());
            FriendshipStatus friendshipStatus = FriendshipStatus.valueOfString(status);
            friendshipEntity.setStatus(friendshipStatus);

            friendshipEntity.setCreatedDate(rs.getTimestamp(FriendshipsQueries.CREATED_DATE_COLUMN.toLowerCase()));

            String addedByGuid = rs.getString(FriendshipsQueries.ADDED_BY_COLUMN.toLowerCase());
            UserEntity addedBy = getUser(addedByGuid);
            friendshipEntity.setAddedBy(addedBy);
        }

        return friendshipEntity;
    }
}
