package com.easysplit.shared.domain.exceptions;

/**
 * Class that holds all the error messages being use along with the custom exceptions
 */
public final class ErrorKeys {
    /**
     * Private constructor to prevent instantiation of the class.
     * This class should only hold string constants and should not be instantiated
     */
    private ErrorKeys() {

    }

    /**
     * Create user
     */
    public static final String CREATE_USER_ILLEGALARGUMENT_TITLE = "create.user.illegalArgument.title";

    public static final String CREATE_USER_NAMETOOLONG_MESSAGE = "create.user.illegalArgument.nameTooLong.message";
    public static final String CREATE_USER_EMPTYNAME_MESSAGE = "create.user.illegalArgument.emptyName.message";

    public static final String CREATE_USER_LASTNAMETOOLONG_MESSAGE = "create.user.illegalArgument.lastnameTooLong.message";
    public static final String CREATE_USER_EMPTYLASTNAME_MESSAGE = "create.user.illegalArgument.emptyLastname.message";

    public static final String CREATE_USER_ERROR_TITLE = "create.user.error.title";
    public static final String CREATE_USER_ERROR_MESSAGE = "create.user.error.message";

    public static final String CREATE_USER_USERNAMETOOLONG_MESSAGE = "create.user.illegalArgument.usernameTooLong.message";
    public static final String CREATE_USER_EMPTYUSERNAME_MESSAGE = "create.user.illegalArgument.emptyUsername.message";

    public static final String CREATE_USER_NOT_UNIQUE_USERNAME_MESSAGE = "create.user.illegalArgument.invalidUsername.message";

    /**
     * Get user
     */

    public static final String GET_USER_NOT_FOUND_TITLE = "get.user.notFound.title";
    public static final String GET_USER_NOT_FOUND_MESSAGE = "get.user.notFound.message";

    public static final String GET_USER_ERROR_TITLE = "get.user.error.title";
    public static final String GET_USER_ERROR_MESSAGE = "get.user.error.message";

    /**
     * Delete user
     */
    public static final String DELETE_USER_ERROR_TITLE = "delete.user.error.title";
    public static final String DELETE_USER_ERROR_MESSAGE= "delete.user.error.message";

    /**
     * Create friendship
     */

    public static final String CREATE_FRIENDSHIP_ERROR_TITLE = "create.friendship.error.title";
    public static final String CREATE_FRIENDSHIP_ERROR_MESSAGE = "create.friendship.error.message";
}
