package com.easysplit.ess.user.domain.contracts;

import com.easysplit.ess.user.domain.models.User;

public interface UserService {
    public User createUser(User user);

    public User getUser(String userGuid);
}
