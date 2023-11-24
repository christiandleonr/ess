package com.easysplit.ess.groups.domain.models;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @Mappings({
            @Mapping(source="id", target="groupGuid"),
            @Mapping(source="name", target="name"),
            @Mapping(source="description", target="description"),
            @Mapping(source="createdBy", target="createdBy"),
            @Mapping(source="createdDate", target="createdDate"),
            @Mapping(source="updatedBy", target="updatedBy"),
            @Mapping(source="updatedDate", target="updatedDate"),
    })
    GroupEntity toGroupEntity(Group group);

    @Mappings({
            @Mapping(source="groupGuid", target="id"),
            @Mapping(source="name", target="name"),
            @Mapping(source="description", target="description"),
            @Mapping(source="createdBy", target="createdBy"),
            @Mapping(source="createdDate", target="createdDate"),
            @Mapping(source="updatedBy", target="updatedBy"),
            @Mapping(source="updatedDate", target="updatedDate"),
    })
    Group toGroup(GroupEntity groupEntity);
}
