package com.easysplit.ess.groups.domain.validators;

import com.easysplit.ess.groups.domain.models.Group;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.helpers.DomainHelper;
import com.easysplit.shared.utils.EssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class that contains utility methods to be used for pure data validation
 */
@Component
public class GroupValidator {
    private static final int GROUP_NAME_LENGTH_LIMIT = 100;
    private static final int GROUP_DESCRIPTION_LENGTH_LIMIT = 200;

    private final DomainHelper domainHelper;

    @Autowired
    public GroupValidator(DomainHelper domainHelper) {
        this.domainHelper = domainHelper;
    }

    /**
     * Validate group information provided in the payload
     *
     * @param group group to be validated
     */
    public void validate(Group group) {
        if (group == null) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_GROUP_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_GROUP_EMPTYGROUP_MESSAGE,
                    null
            );
        }

        validateName(group.getName());
        validateDescription(group.getDescription());
    }

    /**
     * Validates the group name
     * The name cannot be empty or exceed 100 characters
     *
     * @param name group name
     */
    private void validateName(String name) {
        if (EssUtils.isNullOrEmpty(name)) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_GROUP_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_GROUP_EMPTYNAME_MESSAGE,
                    new Object[] {name}
            );
        }

        if (name.length() > GROUP_NAME_LENGTH_LIMIT) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_GROUP_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_GROUP_NAMETOOLONG_MESSAGE,
                    new Object[] {GROUP_NAME_LENGTH_LIMIT}
            );
        }
    }

    private void validateDescription(String description) {
        if (description.length() > GROUP_DESCRIPTION_LENGTH_LIMIT) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_GROUP_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_GROUP_DESCRIPTIONTOOLONG_MESSAGE,
                    new Object[] {GROUP_DESCRIPTION_LENGTH_LIMIT}
            );
        }
    }
}
