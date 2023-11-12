package com.easysplit.ess.user.domain.contracts;

import com.easysplit.ess.user.domain.models.UserEntity;

public interface UserRepository {
    public UserEntity createUser(UserEntity user);

    public UserEntity getUser(String userGuid);
}
