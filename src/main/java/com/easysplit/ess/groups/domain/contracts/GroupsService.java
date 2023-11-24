package com.easysplit.ess.groups.domain.contracts;

import com.easysplit.ess.groups.domain.models.Group;

public interface GroupsService {
    /**
     * Creates a new group
     * @param group
     * @return
     */
    Group createGroup(Group group);
}
