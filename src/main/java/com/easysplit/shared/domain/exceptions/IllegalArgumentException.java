package com.easysplit.shared.domain.exceptions;

public class IllegalArgumentException extends RuntimeException {
    private String errorTitle;
    private String errorMessage;

    public IllegalArgumentException() {

    }

    public IllegalArgumentException(String errorTitle, String errorMessage) {
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
