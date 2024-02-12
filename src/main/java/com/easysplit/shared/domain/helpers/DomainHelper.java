package com.easysplit.shared.domain.helpers;

import com.easysplit.shared.domain.exceptions.IllegalArgumentException;
import com.easysplit.shared.domain.exceptions.UnauthorizedException;
import com.easysplit.shared.domain.models.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Helper class that contains utility methods to be used for the domain layer
 */
@Component
public class DomainHelper {
    private final MessageSource messageSource;

    @Autowired
    public DomainHelper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Throws an IllegalArgumentException with the provided error title and error messages
     *
     * @param errorTitleKey error title key
     * @param errorMessageKey error message key
     * @param args arguments
     * @param locale locale
     */
    public void throwIllegalArgumentException(String errorTitleKey,
                                              String errorMessageKey,
                                              Object[] args,
                                              Locale locale) throws IllegalArgumentException {
        String errorTitle = messageSource.getMessage(errorTitleKey, null, locale);
        String errorMessage = messageSource.getMessage(errorMessageKey, args, locale);

        throw new IllegalArgumentException(errorTitle, errorMessage);
    }

    /**
     * Throws an IllegalArgumentException with the provided error title and error messages
     *
     * @param errorTitleKey error title key
     * @param errorMessageKey error message key
     * @param args arguments
     */
    public void throwIllegalArgumentException(String errorTitleKey,
                                              String errorMessageKey,
                                              Object[] args) throws IllegalArgumentException {
        throwIllegalArgumentException(errorTitleKey, errorMessageKey, args, null);
    }

    /**
     * Throws an UnauthorizedException with the provided error title and error messages
     *
     * @param errorTitleKey error title key
     * @param errorMessageKey error message key
     * @param args arguments
     */
    public void throwUnauthorizedException(String errorTitleKey,
                                           String errorMessageKey,
                                           Object[] args) throws UnauthorizedException {
        String errorTitle = messageSource.getMessage(errorTitleKey, null, null);
        String errorMessage = messageSource.getMessage(errorMessageKey, args, null);

        throw new UnauthorizedException(errorTitle, errorMessage);
    }

    /**
     * Builds an error response object
     *
     * @param errorTitleKey error title key
     * @param errorMessageKey error message key
     * @param args arguments
     * @return error response
     */
    public ErrorResponse buildErrorResponse(String errorTitleKey,
                                   String errorMessageKey,
                                   Object[] args) {
        return new ErrorResponse(
                messageSource.getMessage(errorTitleKey, null, null),
                messageSource.getMessage(errorMessageKey, args, null)
        );
    }
}
