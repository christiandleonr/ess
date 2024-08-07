package com.easysplit.ess.users.utils;

import com.easysplit.ess.user.domain.models.Friendship;
import com.easysplit.shared.domain.models.ResourceList;
import com.easysplit.shared.utils.TestRESTHelper;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.models.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class TestUsersHelper {
    private final String USER_PATH = "/api/users";
    private final String ADD_FRIENDS_PATH = USER_PATH + "/friends";
    private final String LIST_FRIENDS_PATH = USER_PATH + "/%s/friends";
    private final String CREATE_USER_PATH = USER_PATH + "/create";
    @Autowired
    private TestRESTHelper testRestHelper;

    public User createUser(User user, HttpStatus statusCode) {
        return (User) this.testRestHelper.postNoAuth(CREATE_USER_PATH, user, User.class, statusCode);
    }

    public Friendship addFriend(Friendship friendship, HttpStatus statusCode, HttpHeaders authHeaders) {
        return (Friendship) this.testRestHelper.postCustomAuth(
                ADD_FRIENDS_PATH,
                friendship,
                Friendship.class,
                statusCode,
                authHeaders
        );
    }

    public ErrorResponse failCreateUser(User user, HttpStatus statusCode) {
        return this.testRestHelper.failPost(CREATE_USER_PATH, user, statusCode);
    }

    public ErrorResponse failAddFriend(Friendship friendship, HttpStatus statusCode, HttpHeaders authHeaders) {
        return this.testRestHelper.failPost(ADD_FRIENDS_PATH, friendship, statusCode, authHeaders);
    }

    public User getUser(String id, Class<?> responseClass, HttpStatus status) {
        return (User) this.testRestHelper.get(USER_PATH + "/" + id, responseClass, status);
    }

    public ErrorResponse failGet(String id, HttpStatus statusCode) {
        return this.testRestHelper.failGet(USER_PATH + "/" + id, statusCode);
    }

    public ErrorResponse failGet(String id, HttpStatus statusCode, HttpHeaders headers) {
        return this.testRestHelper.failGet(USER_PATH + "/" + id, statusCode, headers);
    }

    public ResourceList<User> listFriends(String addedById, HttpStatus statusCode, HttpHeaders authHeaders) {
        String path = String.format(LIST_FRIENDS_PATH, addedById);
        return this.testRestHelper.listWithCustomAuth(
                path,
                User.class,
                statusCode,
                authHeaders
        );
    }

    public ErrorResponse failListFriends(String addedById, HttpStatus statusCode, HttpHeaders httpHeaders) {
        return this.testRestHelper.failGet(
                String.format(LIST_FRIENDS_PATH, addedById),
                statusCode,
                httpHeaders
        );
    }

    public void deleteUser(String id) {
        this.testRestHelper.delete(USER_PATH + "/" + id);
    }

    public ErrorResponse failDelete(String id, HttpStatus statusCode) {
        return this.testRestHelper.failDelete(USER_PATH + "/" + id, statusCode);
    }
}
