package com.easysplit.ess.user.domain.models;

/**
 * This entity class represents a user-role relation
 */
public class UserRoleEntity {
    private String userGuid;
    private String roleGuid;

    public UserRoleEntity() {}

    public UserRoleEntity(String userGuid, String roleGuid) {
        this.userGuid = userGuid;
        this.roleGuid = roleGuid;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public String getRoleGuid() {
        return roleGuid;
    }

    public void setRoleGuid(String roleGuid) {
        this.roleGuid = roleGuid;
    }
}
