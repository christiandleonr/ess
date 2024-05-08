package com.easysplit.ess.groups.utils;

import com.easysplit.ess.groups.domain.models.Group;
import com.easysplit.shared.utils.TestRESTHelper;
import com.easysplit.shared.domain.models.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class TestGroupsHelper {
    private final String GROUPS_PATH = "/api/groups";
    @Autowired
    private TestRESTHelper testRESTHelper;

    public Group createGroup(Group group, HttpStatus statusCode) {
        return (Group) this.testRESTHelper.post(GROUPS_PATH, group, Group.class, statusCode);
    }

    public ErrorResponse failCreateGroup(Group group, HttpStatus statusCode) {
        return this.testRESTHelper.failPost(GROUPS_PATH, group, statusCode);
    }

    public Group getGroup(String id, Class<?> responseClass, HttpStatus statusCode) {
        return (Group) this.testRESTHelper.get(GROUPS_PATH + "/" + id, responseClass, statusCode);
    }

    public ErrorResponse failGet(String id, HttpStatus statusCode) {
        return this.testRESTHelper.failGet(GROUPS_PATH + "/" + id, statusCode);
    }

    public ErrorResponse failGet(String id, HttpStatus statusCode, HttpHeaders headers) {
        return this.testRESTHelper.failGet(GROUPS_PATH + "/" + id, statusCode, headers);
    }

    public void deleteGroup(String id) {
        this.testRESTHelper.delete(GROUPS_PATH + "/" + id);
    }

    public ErrorResponse failDelete(String id, HttpStatus statusCode) {
        return this.testRESTHelper.failDelete(GROUPS_PATH + "/" + id, statusCode);
    }
}

