package com.easysplit.shared.domain.exceptions;

/**
 * Custom exception to handle internal server errors
 */
public class InternalServerErrorException extends RuntimeException {
    private String errorTitle;
    private String errorMessage;

    public InternalServerErrorException() {

    }

    public InternalServerErrorException(String errorTitle, String errorMessage) {
        super(errorMessage);
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
    }

    public InternalServerErrorException(String errorTitle, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
    }


    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
