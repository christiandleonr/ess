package com.easysplit.ess.user.domain.models;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mappings({
            @Mapping(source="roleGuid", target="id"),
            @Mapping(source="name", target="name"),
    })
    Role toRole(RoleEntity roleEntity);

    List<Role> toListOfRoles(List<RoleEntity> roleEntities);

    @Mappings({
            @Mapping(source="id", target="roleGuid"),
            @Mapping(source="name", target="name"),
    })
    RoleEntity toRoleEntity(Role role);

    List<RoleEntity> toListOfRoleEntities(List<Role> roles);
}
