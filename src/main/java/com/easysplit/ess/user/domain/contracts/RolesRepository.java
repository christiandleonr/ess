package com.easysplit.ess.user.domain.contracts;

import com.easysplit.ess.user.domain.models.RoleEntity;
import com.easysplit.ess.user.domain.models.UserRoleEntity;

import java.util.List;

/**
 * Class that handle the database operations for the user_roles and roles tables
 */
public interface RolesRepository {
    /**
     * Inserts the relation between a user and its role
     *
     * @param userRoleEntity role-user relation
     * @return created role-user relation
     */
    void insertUserRole(UserRoleEntity userRoleEntity);

    /**
     * Loads all the roles for a specific user
     * @param userGuid user to get the roles for
     *
     * @return list of roles
     */
    List<RoleEntity> getRoles(String userGuid);
}
