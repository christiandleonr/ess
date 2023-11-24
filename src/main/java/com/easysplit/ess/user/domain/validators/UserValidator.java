package com.easysplit.ess.user.domain.validators;

import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.helpers.DomainHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.validator.routines.EmailValidator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Class that contains utility methods to be used for pure data validation
 */
@Component
public class UserValidator {
    private static final int USER_NAME_LENGTH = 100;
    private static final int USER_LASTNAME_LENGTH = 100;
    private static final int USER_USERNAME_LENGTH = 50;
    private static final int USER_EMAIL_LENGTH = 100;
    private static final int USER_PHONE_LENGTH = 10;

    private final DomainHelper domainHelper;

    @Autowired
    public UserValidator(DomainHelper domainHelper) {
        this.domainHelper = domainHelper;
    }

    /**
     * Validates user attributes
     */
    public void validate(User user) {
        validateUserName(user.getName());
        validateLastname(user.getLastname());
        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        validatePhone(user.getPhone());
    }

    /**
     * Validates the username, name cannot be empty and
     * the number of characters cannot exceed 100.
     */
    private void validateUserName(String name) {
        if (name.isEmpty()) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_EMPTYNAME_MESSAGE,
                    new Object[] {name}
            );
        }

        if (name.length() > USER_NAME_LENGTH) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_NAMETOOLONG_MESSAGE,
                    new Object[] {USER_NAME_LENGTH}
            );
        }
    }

    /**
     * Validates the user lastname, lastname cannot be empty and
     * the number of characters cannot exceed 100.
     */
    private void validateLastname(String lastname) {
        if (lastname.isEmpty()) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_EMPTYLASTNAME_MESSAGE,
                    new Object[] {lastname}
            );
        }

        if (lastname.length() > USER_LASTNAME_LENGTH) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_LASTNAMETOOLONG_MESSAGE,
                    new Object[] {USER_LASTNAME_LENGTH}
            );
        }
    }

    /**
     * Validates the user username, username cannot be empty and
     * the number of characters cannot exceed 50.
     */
    private void validateUsername(String username) {
        if (username.isEmpty()) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_EMPTYUSERNAME_MESSAGE,
                    new Object[] {username}
            );
        }

        if (username.length() > USER_USERNAME_LENGTH) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_USERNAMETOOLONG_MESSAGE,
                    new Object[] {USER_USERNAME_LENGTH}
            );
        }
    }

    /**
     * Validates the user email, email cannot be empty
     * the number of characters cannot exceed 100,
     * and the Format has to be valid.
     */
    private void validateEmail(String email){
        if (email.isEmpty()){
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_EMPTYEMAIL_MESSAGE,
                    new Object[] {email}
            );
        }

        if (email.length() > USER_EMAIL_LENGTH) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_EMAILTOOLONG_MESSAGE,
                    new Object[] {USER_EMAIL_LENGTH}
            );
        }

        if (!EmailValidator.getInstance().isValid(email)) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_INVALIDEMAILFORMAT_MESSAGE,
                    new Object[]{email}
            );
        }
    }

    private void validatePhone(String phone){
        if (phone.isEmpty()) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_EMPTYPHONE_MESSAGE,
                    new Object[] {phone}
            );
        }

        if(phone.length()!=USER_PHONE_LENGTH){
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_WRONGPHONESIZE_MESSAGE,
                    new Object[] {USER_PHONE_LENGTH}
            );

        }

        if(!validatePhoneFormat(phone)){
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_INVALIDPHONECHAR_MESSAGE,
                    new Object[]{phone}
            );

        }

    }

    private boolean validatePhoneFormat(String phone){
        String regex = "^\\+?\\d+$" ;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }

    public void throwIllegalArgumentException() {

    }
}
