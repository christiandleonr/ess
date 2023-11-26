package com.easysplit.ess.friendships.infrastructure.persistence;

import com.easysplit.ess.friendships.domain.contracts.FriendshipsRepository;
import com.easysplit.ess.user.domain.models.FriendshipEntity;
import com.easysplit.ess.user.domain.models.FriendshipStatus;
import com.easysplit.ess.user.domain.sql.FriendshipsQueries;
import com.easysplit.ess.user.application.UserServiceImpl;
import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.UUID;

@Repository
public class FriendshipsRepositoryImpl implements FriendshipsRepository {
    private static final String CLASS_NAME = FriendshipsRepositoryImpl.class.getName();
    private final JdbcTemplate jdbc;
    private final InfrastructureHelper infrastructureHelper;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public FriendshipsRepositoryImpl(JdbcTemplate jdbc, InfrastructureHelper infrastructureHelper, UserRepository userRepository){
        this.jdbc = jdbc;
        this.infrastructureHelper = infrastructureHelper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public FriendshipEntity createFriendship(FriendshipEntity friendship) throws InternalServerErrorException {
        String friendshipGuid = UUID.randomUUID().toString();
        Timestamp createdDate = infrastructureHelper.getCurrentDate();

        // Throws NotFoundException if any of both users is not found
        UserEntity friend = userRepository.getUser(friendship.getFriend());
        UserEntity addedBy = userRepository.getUser(friendship.getAddedBy());

        try {
            jdbc.update(FriendshipsQueries.CREATE_FRIENDSHIP,
                    friendshipGuid,
                    friend.getUserGuid(),
                    FriendshipStatus.PENDING.getValue(),
                    createdDate,
                    addedBy.getUserGuid() // TODO change this to get the created by from authentication
            );
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".createFriendship() - Something went wrong while creating the friendship: " + friendship, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_FRIENDSHIP_ERROR_TITLE,
                    ErrorKeys.CREATE_FRIENDSHIP_ERROR_MESSAGE,
                    new Object[]{ friendship },
                    e
            );
        }

        friendship.setFriendshipGuid(friendshipGuid);
        friendship.setStatus(FriendshipStatus.PENDING);
        friendship.setCreatedDate(createdDate);

        return friendship;
    }
}
