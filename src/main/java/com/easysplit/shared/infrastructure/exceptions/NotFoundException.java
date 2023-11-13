package com.easysplit.shared.infrastructure.exceptions;

/**
 * Custom exception to handle not founds
 */
public class NotFoundException extends RuntimeException {
    private String errorTitle;
    private String errorMessage;

    public NotFoundException() {

    }

    public NotFoundException(String errorTitle, String errorMessage) {
        super(errorMessage);
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
