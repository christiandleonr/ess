package com.easysplit.ess.user.domain.validators;

import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.helpers.DomainHelper;
import com.easysplit.shared.utils.EssUtils;
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
    public static final int USER_NAME_LENGTH_LIMIT = 100;
    public static final int USER_LASTNAME_LENGTH_LIMIT = 100;
    public static final int USER_USERNAME_LENGTH_LIMIT = 50;
    public static final int USER_EMAIL_LENGTH_LIMIT = 100;
    public static final int USER_PHONE_LENGTH_LIMIT = 10;
    public static final int USER_PASSWORD_LENGTH_LIMIT = 18;

    private final DomainHelper domainHelper;

    @Autowired
    public UserValidator(DomainHelper domainHelper) {
        this.domainHelper = domainHelper;
    }

    /**
     * Validates user attributes
     */
    public void validate(User user) {
        if (user == null) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_EMPTYUSER_MESSAGE,
                    null
            );
        }

        validateUserName(user.getName());
        validateLastname(user.getLastname());
        validateUsername(user.getUsername());
        validatePassword(user.getPassword());
        validateEmail(user.getEmail());
        validatePhone(user.getPhone());
    }

    /**
     * Validates the user name, name cannot be empty and
     * the number of characters cannot exceed 100.
     */
    private void validateUserName(String name) {
        if (EssUtils.isNullOrEmpty(name)) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_EMPTYNAME_MESSAGE,
                    new Object[] {name}
            );
        }

        if (name.length() > USER_NAME_LENGTH_LIMIT) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_NAMETOOLONG_MESSAGE,
                    new Object[] {USER_NAME_LENGTH_LIMIT}
            );
        }
    }

    /**
     * Validates the user lastname, lastname cannot be empty and
     * the number of characters cannot exceed 100.
     */
    private void validateLastname(String lastname) {
        if (EssUtils.isNullOrEmpty(lastname)) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_EMPTYLASTNAME_MESSAGE,
                    new Object[] {lastname}
            );
        }

        if (lastname.length() > USER_LASTNAME_LENGTH_LIMIT) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_LASTNAMETOOLONG_MESSAGE,
                    new Object[] {USER_LASTNAME_LENGTH_LIMIT}
            );
        }
    }

    /**
     * Validates the user username, username cannot be empty and
     * the number of characters cannot exceed 50.
     */
    private void validateUsername(String username) {
        if (EssUtils.isNullOrEmpty(username)) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_EMPTYUSERNAME_MESSAGE,
                    new Object[] {username}
            );
        }

        if (username.length() > USER_USERNAME_LENGTH_LIMIT) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_USERNAMETOOLONG_MESSAGE,
                    new Object[] {USER_USERNAME_LENGTH_LIMIT}
            );
        }
    }

    /**
     * Validates the user plain text password, the password cannot be null nor empty and must
     * not exceed 18 characters.
     *
     * @param password plain text password provided by user
     */
    private void validatePassword(String password) {
        // TODO Change error messages
        if (EssUtils.isNullOrEmpty(password)) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_EMPTYUSERNAME_MESSAGE,
                    new Object[] {password}
            );
        }

        if (password.length() > USER_PASSWORD_LENGTH_LIMIT) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_USERNAMETOOLONG_MESSAGE,
                    new Object[] {USER_USERNAME_LENGTH_LIMIT}
            );
        }
    }

    /**
     * Validates the user email, email cannot be empty
     * the number of characters cannot exceed 100,
     * and the Format has to be valid.
     */
    private void validateEmail(String email){
        if (EssUtils.isNullOrEmpty(email)){
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_EMPTYEMAIL_MESSAGE,
                    new Object[] {email}
            );
        }

        if (email.length() > USER_EMAIL_LENGTH_LIMIT) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_EMAILTOOLONG_MESSAGE,
                    new Object[] {USER_EMAIL_LENGTH_LIMIT}
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

    /**
     *Validates the user phone number, number cannot be empty
     * the number of characters cannot exceed 10,
     * and has to only include digits
     */
    private void validatePhone(String phone){
        if (EssUtils.isNullOrEmpty(phone)) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_EMPTYPHONE_MESSAGE,
                    new Object[] {phone}
            );
        }

        if(!validatePhoneFormat(phone)){
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_INVALIDPHONECHAR_MESSAGE,
                    new Object[]{phone}
            );

        }

        if(phone.length()!=USER_PHONE_LENGTH_LIMIT){
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_USER_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_USER_WRONGPHONESIZE_MESSAGE,
                    new Object[] {USER_PHONE_LENGTH_LIMIT}
            );

        }
    }

    private boolean validatePhoneFormat(String phone){
        String regex = "^\\+?\\d+$" ;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }
}
