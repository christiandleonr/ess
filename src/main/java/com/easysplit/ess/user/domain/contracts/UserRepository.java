package com.easysplit.ess.user.domain.contracts;

import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.shared.infrastructure.exceptions.InternalServerErrorException;
import com.easysplit.shared.infrastructure.exceptions.NotFoundException;

public interface UserRepository {
    public UserEntity createUser(UserEntity user) throws InternalServerErrorException;

    public UserEntity getUser(String userGuid) throws NotFoundException, InternalServerErrorException;
}
