package com.easysplit.shared.infrastructure.exceptions;

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
    public static final String CREATE_USER_ERROR_TITLE = "create.user.error.title";
    public static final String CREATE_USER_ERROR_MESSAGE = "create.user.error.message";

    /**
     * Get user
     */
    public static final String GET_USER_ERROR_TITLE = "get.user.error.title";
    public static final String GET_USER_ERROR_MESSAGE = "get.user.error.message";
    public static final String GET_USER_NOT_FOUND_TITLE = "get.user.notfound.title";
    public static final String GET_USER_NOT_FOUND_MESSAGE = "get.user.notfound.message";
}
