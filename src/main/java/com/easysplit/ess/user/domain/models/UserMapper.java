package com.easysplit.ess.user.domain.models;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mappings({
            @Mapping(source="userGuid", target="userGuid"),
            @Mapping(source="name", target="name"),
            @Mapping(source="lastname", target="lastname"),
            @Mapping(source="username", target="username"),
            @Mapping(source="createdDate", target="createdDate"),
    })
    User toUser(UserEntity userEntity);

    @Mappings({
            @Mapping(source="userGuid", target="userGuid"),
            @Mapping(source="name", target="name"),
            @Mapping(source="lastname", target="lastname"),
            @Mapping(source="username", target="username"),
            @Mapping(source="createdDate", target="createdDate"),
    })
    UserEntity toUserEntity(User user);
}