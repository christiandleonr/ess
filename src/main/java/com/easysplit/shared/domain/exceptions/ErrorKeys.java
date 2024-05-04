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
    public static final String CREATE_USER_EMPTYUSER_MESSAGE = "create.user.illegalArgument.emptyUser.message";

    public static final String CREATE_USER_NAMETOOLONG_MESSAGE = "create.user.illegalArgument.nameTooLong.message";
    public static final String CREATE_USER_EMPTYNAME_MESSAGE = "create.user.illegalArgument.emptyName.message";

    public static final String CREATE_USER_LASTNAMETOOLONG_MESSAGE = "create.user.illegalArgument.lastnameTooLong.message";
    public static final String CREATE_USER_EMPTYLASTNAME_MESSAGE = "create.user.illegalArgument.emptyLastname.message";

    public static final String CREATE_USER_ERROR_TITLE = "create.user.error.title";
    public static final String CREATE_USER_ERROR_MESSAGE = "create.user.error.message";

    public static final String CREATE_USER_USERNAMETOOLONG_MESSAGE = "create.user.illegalArgument.usernameTooLong.message";
    public static final String CREATE_USER_EMPTYUSERNAME_MESSAGE = "create.user.illegalArgument.emptyUsername.message";
    public static final String CREATE_USER_PASSWORDTOOLONG_MESSAGE = "create.user.illegalArgument.passwordTooLong.message";
    public static final String CREATE_USER_EMPTYPASSWORD_MESSAGE = "create.user.illegalArgument.emptyPassword.message";
    public static final String CREATE_USER_EMAILTOOLONG_MESSAGE = "create.user.illegalArgument.emailTooLong.message";
    public static final String CREATE_USER_EMPTYEMAIL_MESSAGE = "create.user.illegalArgument.emptyEmail.message";
    public static final String CREATE_USER_INVALIDEMAILFORMAT_MESSAGE="create.user.illegalArgument.invalidEmailFormat.message";
    public static final String CREATE_USER_EMPTYPHONE_MESSAGE = "create.user.illegalArgument.emptyPhone.message";
    public static final String CREATE_USER_WRONGPHONESIZE_MESSAGE = "create.user.illegalArgument.phoneWrongSize.message";
    public static final String CREATE_USER_INVALIDPHONECHAR_MESSAGE = "create.user.illegalArgument.phoneInvalidChar.message";
    public static final String CREATE_USER_NOT_UNIQUE_USERNAME_MESSAGE = "create.user.illegalArgument.invalidUsername.message";
    public static final String CREATE_USER_NOT_UNIQUE_EMAIL_MESSAGE = "create.user.illegalArgument.invalidEmail.message";
    public static final String CREATE_USER_NOT_UNIQUE_PHONE_MESSAGE = "create.user.illegalArgument.invalidPhone.message";
    public static final String CREATE_USER_BY_USERNAME_MESSAGE = "create.user.by.username.error.message";
    public static final String CREATE_USER_BY_EMAIL_MESSAGE = "create.user.by.email.error.message";
    public static final String CREATE_USER_BY_PHONE_MESSAGE = "create.user.by.phone.error.message";

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
    public static final String CREATE_FRIENDSHIP_ILLEGALARGUMENT_TITLE = "create.friendship.illegalArgument.title";
    public static final String CREATE_FRIENDSHIP_NULL_FRIEND_MESSAGE = "create.friendship.illegalArgument.nullFriend.message";
    public static final String CREATE_FRIENDSHIP_NULL_ADDED_BY_MESSAGE = "create.friendship.illegalArgument.nullAddedBy.message";
    public static final String CREATE_FRIENDSHIP_EMPTY_FRIEND_ID_MESSAGE = "create.friendship.illegalArgument.friendEmptyId.message";
    public static final String CREATE_FRIENDSHIP_EMPTY_ADDED_BY_ID_MESSAGE = "create.friendship.illegalArgument.addedByEmptyId.message";

    /**
     * List friends
     */

    public static final String LIST_FRIENDS_ERROR_TITLE = "list.friends.error.title";
    public static final String LIST_FRIENDS_ERROR_MESSAGE = "list.friends.error.message";

    /**
     * Load friendship details
     */
    public static final String LOAD_FRIENDSHIP_ERROR_TITLE = "load.friendship.error.title";
    public static final String LOAD_FRIENDSHIP_ERROR_MESSAGE = "load.friendship.error.message";
    public static final String LOAD_FRIENDSHIP_ILLEGALARGUMENT_TITLE = "load.friendship.illegalArgument.title";
    public static final String LOAD_FRIENDSHIP_EXIST_MESSAGE = "load.friendship.illegalArgument.friendship.exist";

    /**
     * Create group
     */
    public static final String CREATE_GROUP_ERROR_TITLE = "create.group.error.title";
    public static final String CREATE_GROUP_ERROR_MESSAGE = "create.group.error.message";

    public static final String CREATE_GROUP_ILLEGALARGUMENT_TITLE = "create.group.illegalArgument.title";
    public static final String CREATE_GROUP_EMPTYGROUP_MESSAGE = "create.group.illegalArgument.emptyGroup.message";
    public static final String CREATE_GROUP_NAMETOOLONG_MESSAGE = "create.group.illegalArgument.nameTooLong.message";
    public static final String CREATE_GROUP_EMPTYNAME_MESSAGE = "create.group.illegalArgument.emptyName.message";
    public static final String CREATE_GROUP_DESCRIPTIONTOOLONG_MESSAGE = "create.group.illegalArgument.descriptionTooLong.message";

    /**
     * Delete group
     */
    public static final String DELETE_GROUP_ERROR_TITLE = "delete.group.error.title";
    public static final String DELETE_GROUP_ERROR_MESSAGE = "delete.group.error.message";

    /**
     * Delete group member
     */
    public static final String DELETE_GROUP_MEMBER_ERROR_TITLE = "delete.group.member.error.title";
    public static final String DELETE_GROUP_MEMBER_ERROR_MESSAGE = "delete.group.member.error.message";
    public static final String DELETE_ALL_GROUP_MEMBER_ERROR_TITLE = "delete.all.group.member.error.title";
    public static final String DELETE_ALL_GROUP_MEMBER_ERROR_MESSAGE = "delete.all.group.member.error.message";

    /**
     * Get group
     */
    public static final String GET_GROUP_NOT_FOUND_TITLE = "get.group.notFound.title";
    public static final String GET_GROUP_NOT_FOUND_MESSAGE = "get.group.notFound.message";

    public static final String GET_GROUP_ERROR_TITLE = "get.group.error.title";
    public static final String GET_GROUP_ERROR_MESSAGE = "get.group.error.message";

    /**
     * Create transaction
     */
    public static final String CREATE_TRANSACTION_ERROR_TITLE = "create.transaction.error.title";
    public static final String CREATE_TRANSACTION_ERROR_MESSAGE = "create.transaction.error.message";
    public static final String CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE = "create.transaction.illegalArgument.title";
    public static final String CREATE_TRANSACTION_NULLOREMPTY_TRANSACTION_MESSAGE = "create.transaction.illegalArgument.nullOrEmpty.transaction.message";
    public static final String CREATE_TRANSACTION_NULLOREMPTY_NAME_MESSAGE = "create.transaction.illegalArgument.nullOrEmpty.name.message" ;
    public static final String CREATE_TRANSACTION_NAME_TOOLONG_MESSAGE = "create.transaction.illegalArgument.name.tooLong.message";
    public static final String CREATE_TRANSACTION_NULLOREMPTY_CURRENCY_MESSAGE = "create.transaction.illegalArgument.nullOrEmpty.currency.message";
    public static final String CREATE_TRANSACTION_CURRENCY_TOOLONG_MESSAGE = "create.transaction.illegalArgument.currency.tooLong.message";
    public static final String CREATE_TRANSACTION_NULLOREMPTY_CREDITOR_MESSAGE = "create.transaction.illegalArgument.nullOrEmpty.creditor.id.message";
    public static final String CREATE_TRANSACTION_NULLOREMPTY_DEBTOR_MESSAGE = "create.transaction.illegalArgument.nullOrEmpty.debtor.id.message";
    public static final String CREATE_TRANSACTION_NULLOREMPTY_CREATED_BY_MESSAGE = "create.transaction.illegalArgument.nullOrEmpty.createdBy.id.message";
    public static final String INSERT_NEW_DEBT_ERROR_TITLE = "insert.new.debt.error.title";
    public static final String INSERT_NEW_DEBT_ERROR_MESSAGE = "insert.new.debt.error.message";
    public static final String INSERT_NEW_DEBT_NULLOREMPTY_DEBT_MESSAGE = "insert.new.debt.illegalArgument.nullOrEmpty.debt";
    public static final String INSERT_NEW_DEBT_NULLOREMPTY_TOTAL_AMOUNT_MESSAGE = "insert.new.debt.illegalArgument.nullOrEmpty.totalAmount";
    public static final String INSERT_NEW_DEBT_NULLOREMPTY_DEBT_AMOUNT_MESSAGE = "insert.new.debt.illegalArgument.nullOrEmpty.debt.amount";
    public static final String INSERT_NEW_DEBT_ZERO_TOTAL_AMOUNT_MESSAGE = "insert.new.debt.illegalArgument.zero.totalAmount";
    public static final String INSERT_NEW_DEBT_ZERO_DEBT_MESSAGE = "insert.new.debt.illegalArgument.zero.debt";
    public static final String INSERT_NEW_DEBT_DEBT_GREATERTHAN_TOTALAMOUNT = "insert.new.debt.illegalArgument.debt.greaterThan.totalAmount";
    public static final String INSERT_NEW_DEBT_NULLOREMPTY_CREATED_BY_MESSAGE = "insert.new.debt.illegalArgument.debt.created.by.empty";
    public static final String READ_LAST_REVISION_ERROR_MESSAGE = "read.last.revision.error.message";

    /**
     * Bulk create transactions
     */
    public static final String BULK_CREATE_TRANSACTION_ERROR_TITLE = "bulk.create.transaction.error.title";
    public static final String BULK_CREATE_TRANSACTION_ERROR_MESSAGE = "bulk.create.transaction.error.message";
    public static final String BULK_CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE = "bulk.create.transaction.illegalArgument.title";
    public static final String BULK_CREATE_TRANSACTION_ILLEGALARGUMENT_EMPTY_TRANSACTION_LIST = "bulk.create.transaction.illegalArgument.nullOrEmpty.transactionList.message";
    public static final String BULK_INSERT_NEW_DEBT_ERROR_TITLE = "bulk.insert.new.debt.error.title";
    public static final String BULK_INSERT_NEW_DEBT_ERROR_MESSAGE = "bulk.insert.new.debt.error.message";

    /**
     * Get transaction
     */
    public static final String GET_TRANSACTION_NOT_FOUND_TITLE = "get.transaction.notFound.title";
    public static final String GET_TRANSACTION_NOT_FOUND_MESSAGE = "get.transaction.notFound.message";
    public static final String GET_TRANSACTION_ERROR_TITLE = "get.transaction.error.title";
    public static final String GET_TRANSACTION_ERROR_MESSAGE = "get.transaction.error.message";

    /**
     * List transactions
     */
    public static final String LIST_TRANSACTIONS_BY_GROUP_ERROR_TITLE = "list.transactions.byGroup.error.title";
    public static final String LIST_TRANSACTIONS_BY_GROUP_ERROR_MESSAGE = "list.transactions.byGroup.error.message";
    public static final String LIST_TRANSACTIONS_BY_GROUP_COUNT_ERROR_MESSAGE = "list.transactions.byGroup.count.error.message";

    /**
     * Get debt
     */
    public static final String GET_DEBT_NOT_FOUND_TITLE = "get.debt.notFound.title";
    public static final String GET_DEBT_NOT_FOUND_MESSAGE = "get.debt.notFound.message";
    public static final String GET_DEBT_ERROR_TITLE = "get.debt.error.title";
    public static final String GET_DEBT_ERROR_MESSAGE = "get.debt.error.message";

    /**
     * Roles
     */
    public static final String ASSIGN_ROLES_ERROR_TILE = "assign.roles.error.title";
    public static final String ASSIGN_ROLES_ERROR_MESSAGE = "assign.roles.error.message";
    public static final String LIST_ROLES_ERROR_TILE = "list.roles.error.title";
    public static final String LIST_ROLES_ERROR_MESSAGE = "list.roles.error.message";
    public static final String DELETE_ROLES_ERROR_TILE = "delete.roles.error.title";
    public static final String DELETE_ROLES_ERROR_MESSAGE = "delete.roles.error.message";

    /**
     * IAM
     */
    public static final String AUTHENTICATION_ERROR_TITLE = "authentication.error.title";
    public static final String AUTHENTICATION_ERROR_MESSAGE = "authentication.error.message";
    public static final String AUTHENTICATION_ILLEGALARGUMENT_TITLE = "authentication.illegalArgument.title";
    public static final String AUTHENTICATION_INVALID_USERID_MESSAGE = "authentication.invalid.userId.message";
    public static final String UNAUTHORIZED_EXCEPTION_TITLE = "unauthorizedException.title";
    public static final String REFRESH_TOKEN_NOT_FOUND_TITLE = "refreshToken.notFound.title";
    public static final String REFRESH_TOKEN_NOT_FOUND_MESSAGE = "refreshToken.notFound.message";
    public static final String ACCESS_TOKEN_EXPIRED_MESSAGE = "accessToken.expired.message";
    public static final String REFRESH_TOKEN_EXPIRED_MESSAGE = "refreshToken.expired.message";

}
