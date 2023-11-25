package com.easysplit.ess.groups.domain.contracts;

import com.easysplit.ess.groups.domain.models.Group;

public interface GroupsService {
    /**
     * Creates a new group
     * @param group group to be created
     * @param createdBy user who creates the group
     * @return created group
     */
    Group createGroup(Group group, String createdBy);
}
