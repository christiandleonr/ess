package com.easysplit.shared.infrastructure.helpers;

import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Helper class that contains utility methods to be used for the infrastructure layer
 */
@Component
public class InfrastructureHelper {
    private final MessageSource messageSource;

    public InfrastructureHelper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Get current date
     *
     * @return current date as Timestamp
     */
    public Timestamp getCurrentDate() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Throws an InternalServerErrorException with the provided error title and error message
     *
     * @param errorTitleKey error title key
     * @param errorMessageKey error message key
     * @param args arguments
     * @param t exception
     */
    public void throwInternalServerErrorException(String errorTitleKey,
                                                  String errorMessageKey,
                                                  Object[] args,
                                                  Throwable t) {
        String errorTitle = messageSource.getMessage(errorTitleKey, null, null);
        String errorMessage = messageSource.getMessage(errorMessageKey, args, null);

        throw new InternalServerErrorException(errorTitle, errorMessage, t);
    }
}
