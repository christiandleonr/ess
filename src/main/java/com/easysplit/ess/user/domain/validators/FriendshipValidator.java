package com.easysplit.ess.user.domain.validators;

import com.easysplit.ess.user.domain.models.Friendship;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.helpers.DomainHelper;
import com.easysplit.shared.utils.EssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class that contains utility methods to be used for pure data validation
 */
@Component
public class FriendshipValidator {
    private final DomainHelper domainHelper;

    @Autowired
    public FriendshipValidator(DomainHelper domainHelper) {
        this.domainHelper = domainHelper;
    }

    /**
     * Validates the friendship to be created between to users represented
     * by the fields <i>friend</i> and <i>addedBy</i>
     *
     * @param friendship friendship to be validated
     */
    public void validate(Friendship friendship) {
        validateFriend(friendship.getFriend(),
                ErrorKeys.CREATE_FRIENDSHIP_NULL_FRIEND_MESSAGE,
                ErrorKeys.CREATE_FRIENDSHIP_EMPTY_FRIEND_ID_MESSAGE);

        validateFriend(friendship.getAddedBy(),
                ErrorKeys.CREATE_FRIENDSHIP_NULL_ADDED_BY_MESSAGE,
                ErrorKeys.CREATE_FRIENDSHIP_EMPTY_ADDED_BY_ID_MESSAGE);
    }

    /**
     * Validates the user objects in a friendship, <i>friend</i> and <i>addedBy</i>
     * cannot be null nor its ids be empty
     *
     * @param user user to be validated
     */
    public void validateFriend(User user, String nullMessage, String emptyMessage) {
        if (user == null) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_FRIENDSHIP_ILLEGALARGUMENT_TITLE,
                    nullMessage,
                    new Object[] {}
            );
        }

        if (EssUtils.isNullOrEmpty(user.getId())) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_FRIENDSHIP_ILLEGALARGUMENT_TITLE,
                    emptyMessage,
                    new Object[] {}
            );
        }
    }
}
