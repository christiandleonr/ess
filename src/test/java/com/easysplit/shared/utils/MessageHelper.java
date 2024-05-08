package com.easysplit.shared.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageHelper {
    @Autowired
    private MessageSource messageSource;

    public String getMessage(String errorKey, Object[] args) {
        return messageSource.getMessage(errorKey, args, null);
    }
}
