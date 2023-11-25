package com.easysplit.ess.user.domain.models;

import com.easysplit.ess.groups.domain.models.GroupMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(source="userGuid", target="id"),
            @Mapping(source="name", target="name"),
            @Mapping(source="lastname", target="lastname"),
            @Mapping(source="username", target="username"),
            @Mapping(source="createdDate", target="createdDate"),
    })
    User toUser(UserEntity userEntity);

    List<User> toListOfUsers(List<UserEntity> userEntities);

    @Mappings({
            @Mapping(source="id", target="userGuid"),
            @Mapping(source="name", target="name"),
            @Mapping(source="lastname", target="lastname"),
            @Mapping(source="username", target="username"),
            @Mapping(source="createdDate", target="createdDate"),
    })
    UserEntity toUserEntity(User user);

    List<UserEntity> toListOfUserEntities(List<User> users);
}