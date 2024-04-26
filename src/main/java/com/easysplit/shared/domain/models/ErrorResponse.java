package com.easysplit.shared.domain.models;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Object to be serialized and used as response when any exception is thrown
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse  {
    private String errorTitle;
    private String errorMessage;
    private Throwable cause;

    public ErrorResponse() {}

    public ErrorResponse(String errorTitle, String errorMessage) {
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(String errorTitle, String errorMessage, Throwable cause) {
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
        this.cause = cause;
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

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
