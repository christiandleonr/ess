package com.easysplit.ess.user.domain.models;

/**
 * Role object to be used for database operations
 */
public class RoleEntity {
    private String roleGuid;
    private String name;

    public RoleEntity() {}

    public String getRoleGuid() {
        return roleGuid;
    }

    public void setRoleGuid(String roleGuid) {
        this.roleGuid = roleGuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role ( id : " + this.roleGuid + " | "
                + "name : " + this.name  + " )";
    }

    /**
     * Generates a role object from this entity class
     *
     * @return role object
     */
    private Role toRole() {
        return RoleMapper.INSTANCE.toRole(this);
    }
}
