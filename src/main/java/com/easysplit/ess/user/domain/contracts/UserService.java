package com.easysplit.ess.user.domain.contracts;

import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.infrastructure.exceptions.InternalServerErrorException;
import com.easysplit.shared.infrastructure.exceptions.NotFoundException;

public interface UserService {
    public User createUser(User user) throws InternalServerErrorException;

    public User getUser(String userGuid) throws NotFoundException, InternalServerErrorException;
}
