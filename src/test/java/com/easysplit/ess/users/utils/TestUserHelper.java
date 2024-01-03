package com.easysplit.ess.users.utils;

import com.easysplit.ess.shared.utils.TestRESTHelper;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.models.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class TestUserHelper {
    private final String USER_PATH = "/api/users";
    @Autowired
    private TestRESTHelper testRestHelper;
    @Autowired
    private MessageSource messageSource;

    public User createUser(User user, HttpStatus statusCode) {
        return (User) this.testRestHelper.post(USER_PATH, user, statusCode);
    }

    public ErrorResponse failCreateUser(User user, HttpStatus statusCode) {
        return this.testRestHelper.failPost(USER_PATH, user, statusCode);
    }

    public User getUser(String id, Class<?> responseClass, HttpStatus status) {
        return (User) this.testRestHelper.get(USER_PATH + "/" + id, responseClass, status);
    }

    public ErrorResponse failGet(String id, HttpStatus statusCode) {
        return this.testRestHelper.failGet(USER_PATH + "/" + id, statusCode);
    }

    public void deleteUser(String id) {
        this.testRestHelper.delete(USER_PATH + "/" + id);
    }

    public ErrorResponse failDelete(String id, HttpStatus statusCode) {
        return this.testRestHelper.failDelete(USER_PATH + "/" + id, statusCode);
    }

    public String getMessage(String errorKey, Object[] args) {
        return messageSource.getMessage(errorKey, args, null);
    }
}
