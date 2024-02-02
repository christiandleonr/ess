package com.easysplit.ess.user.domain.models;

/**
 * Role object to be serialized
 */
public class Role {
    private String id;
    private String name;

    public Role() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role ( id : " + this.id + " | "
                + "name : " + this.name  + " )";
    }

    /**
     * Generates a roll entity from this class
     *
     * @return role entity object
     */
    private RoleEntity toRoleEntity() {
        return RoleMapper.INSTANCE.toRoleEntity(this);
    }
}
