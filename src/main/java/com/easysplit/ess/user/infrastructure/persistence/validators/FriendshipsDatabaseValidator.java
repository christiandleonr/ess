package com.easysplit.ess.user.infrastructure.persistence.validators;

import com.easysplit.ess.user.domain.contracts.FriendsRepository;
import com.easysplit.ess.user.domain.models.FriendshipEntity;
import com.easysplit.ess.user.infrastructure.persistence.UserRepositoryImpl;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import com.easysplit.shared.utils.EssUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FriendshipsDatabaseValidator {
    private final FriendsRepository friendshipsRepository;
    private final InfrastructureHelper infrastructureHelper;
    private static final String CLASS_NAME = FriendshipsDatabaseValidator.class.getName();
    private static final Logger logger = LoggerFactory.getLogger(FriendshipsDatabaseValidator.class);

    public FriendshipsDatabaseValidator(FriendsRepository friendshipsRepository, InfrastructureHelper infrastructureHelper) {
        this.friendshipsRepository = friendshipsRepository;
        this.infrastructureHelper = infrastructureHelper;
    }

    /**
     * Validates if a friendship between two users already exist.
     *
     * @param friend id of user to be added as friend
     * @param addedBy id of user who is starting the friendship
     */
    public void validateFriendshipNotExist(String friend, String addedBy) {
        if (EssUtils.isNullOrEmpty(friend) || EssUtils.isNullOrEmpty(addedBy)) {
            logger.info("{}.validateFriendshipNotExist() - Either the friend id or added by id are null", CLASS_NAME);
            return;
        }

        FriendshipEntity friendshipEntity = friendshipsRepository.loadFriendship(friend, addedBy);

        if (friendshipEntity != null) {
            infrastructureHelper.throwIllegalArgumentException(
                    ErrorKeys.LOAD_FRIENDSHIP_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.LOAD_FRIENDSHIP_EXIST_MESSAGE,
                    new Object[]{ friend, addedBy }
            );
        }
    }
}
