package com.easysplit.ess.transactions.domain.validators;

import com.easysplit.ess.transactions.domain.models.Debt;
import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.helpers.DomainHelper;
import com.easysplit.shared.domain.models.Money;
import com.easysplit.shared.utils.EssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

/**
 * TODO Add new validations when creating transaction through the bulk process.
 *
 * 1. The total sum between all the transactions must be the same as the total amount
 * 2. Debtor and Creditor cannot be the same user?
 * 3. Total amount and debt must not be equals?
 */

/**
 * Class that contains utility methods to be used for pure data validation
 */
@Component
public class TransactionsValidator {
    public static final int TRANSACTION_NAME_LENGTH_LIMIT = 150;
    public static final int TRANSACTION_CURRENCY_CODE_LENGTH_LIMIT = 3;
    public static final BigDecimal TRANSACTION_DEBT_LOWER_LIMIT = BigDecimal.valueOf(0);
    public static final BigDecimal TRANSACTION_DEBT_TOTAL_AMOUNT_LOWER_LIMIT = BigDecimal.valueOf(0);
    private final DomainHelper domainHelper;

    @Autowired
    public TransactionsValidator(DomainHelper domainHelper) {
        this.domainHelper = domainHelper;
    }

    /**
     * All transaction must comply with the following requirements:
     * Validates if a transaction is valid to be created.
     * The name cannot be null, empty nor exceed 150 characters.
     * The currency cannot be null, empty nor exceed 3 characters and must be part of the valid currency codes.
     * The debt object must contain valid values for the fields <i>totalAmount</i>,
     * <i>debt</i> and <i>createdBy</i>.
     * The group object must have valid values for the <i>id</i> field.
     * Fields <i>creditor</i>, <i>debtor</i> and <i>createdBy</i> must have valid values for the
     * field <i>id</i>
     *
     * @param transactions list of transaction to be validated
     */
    public void validate(List<Transaction> transactions) {
        if (EssUtils.isNullOrEmpty(transactions)) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.BULK_CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.BULK_CREATE_TRANSACTION_ILLEGALARGUMENT_EMPTY_TRANSACTION_LIST,
                    null
            );
        }

        for(Transaction transaction: transactions) {
            validate(transaction);
        }
    }

    /**
     * Validates if a transaction is valid to be created.
     * The name cannot be null, empty nor exceed 150 characters.
     * The currency cannot be null, empty nor exceed 3 characters and must be part of the valid currency codes.
     * The debt object must contain valid values for the fields <i>totalAmount</i> and <i>debt</i>.
     * The group object must have valid values for the <i>id</i> field.
     * Fields <i>creditor</i> and <i>debtor</i> must have valid values for the field <i>id</i>
     *
     * @param transaction transaction to be validated
     */
    public void validate(Transaction transaction) {
        if (transaction == null) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_TRANSACTION_MESSAGE,
                    null
            );
        }

        validateName(transaction.getName());
        validateCurrency(transaction.getCurrency());
        validateDebt(transaction.getDebt());
        validateUserInTransaction(transaction.getCreditor(), ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CREDITOR_MESSAGE);
        validateUserInTransaction(transaction.getDebtor(), ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_DEBTOR_MESSAGE);
    }

    /**
     * Validates the transaction name, the name cannot be null, empty nor exceed 150 characters
     *
     * @param name transaction name
     */
    private void validateName(String name) {
        if (EssUtils.isNullOrEmpty(name)) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_NAME_MESSAGE,
                    null
            );
        }

        if (name.length() > TRANSACTION_NAME_LENGTH_LIMIT) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_TRANSACTION_NAME_TOOLONG_MESSAGE,
                    new Object[] {TRANSACTION_NAME_LENGTH_LIMIT}
            );
        }
    }

    /**
     * Validates the transaction currency type, the currency cannot be null, empty nor exceed 3 characters
     *
     * @param currency transaction currency type
     */
    private void validateCurrency(Currency currency) {
        if (currency == null) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CURRENCY_MESSAGE,
                    null
            );
        }

        String currencyCode = currency.getCurrencyCode();
        if (EssUtils.isNullOrEmpty(currencyCode)) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_TRANSACTION_NULLOREMPTY_CURRENCY_MESSAGE,
                    null
            );
        }

        if (currencyCode.length() > TRANSACTION_CURRENCY_CODE_LENGTH_LIMIT) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.CREATE_TRANSACTION_CURRENCY_TOOLONG_MESSAGE,
                    new Object[] {TRANSACTION_CURRENCY_CODE_LENGTH_LIMIT}
            );
        }
    }

    /**
     * Validates the users in the transaction, must not be null and have a valid value for its id
     *
     * @param user user to be validated
     */
    private void validateUserInTransaction(User user, String errorMessage) {
        if (user == null || EssUtils.isNullOrEmpty(user.getId())) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE,
                    errorMessage,
                    null
            );
        }
    }

    /**
     * Validates the debt for this transaction, the debt must have valid values for the fields <i>totalAmount</i>,
     * <i>debt</i> and <i>createdBy</i>.
     *
     * @param debt debt object to be validated
     */
    private void validateDebt(Debt debt) {
        if (debt == null) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_DEBT_MESSAGE,
                    null
            );
        }

        validateTotalAmount(debt.getTotalAmount());
        validateDebt(debt.getDebt(), debt.getTotalAmount());
    }

    /**
     * Validates total amount, the total amount of the debt must be greater than 0
     *
     * @param totalAmount debt amount to be validated
     */
    private void validateTotalAmount(Money totalAmount) {
        if (totalAmount == null || totalAmount.getAmount() == null) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_TOTAL_AMOUNT_MESSAGE,
                    null
            );
        }

        if (TRANSACTION_DEBT_TOTAL_AMOUNT_LOWER_LIMIT.compareTo(totalAmount.getAmount()) >= 0) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.INSERT_NEW_DEBT_ZERO_TOTAL_AMOUNT_MESSAGE,
                    new Object[] {TRANSACTION_DEBT_TOTAL_AMOUNT_LOWER_LIMIT}
            );
        }
    }

    /**
     * Validates debt, the debt must not be more than the actual total amount of the transaction,
     * the debt must be greater than 0
     *
     * @param debt debt amount to be validated
     */
    private void validateDebt(Money debt, Money totalAmount) {
        if (debt == null || debt.getAmount() == null) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.INSERT_NEW_DEBT_NULLOREMPTY_DEBT_AMOUNT_MESSAGE,
                    null
            );
        }

        if (TRANSACTION_DEBT_LOWER_LIMIT.compareTo(debt.getAmount()) >= 0) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.INSERT_NEW_DEBT_ZERO_DEBT_MESSAGE,
                    new Object[] {TRANSACTION_DEBT_LOWER_LIMIT}
            );
        }

        if (totalAmount.getAmount().compareTo(debt.getAmount()) < 0) {
            domainHelper.throwIllegalArgumentException(
                    ErrorKeys.CREATE_TRANSACTION_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.INSERT_NEW_DEBT_DEBT_GREATERTHAN_TOTALAMOUNT,
                    null
            );
        }
    }
}
