package com.easysplit.ess.user.domain.models;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FriendshipsMapper {
    FriendshipsMapper INSTANCE = Mappers.getMapper(FriendshipsMapper.class);

    @Mappings({
            @Mapping(source="friendshipGuid", target="id"),
            @Mapping(source="friend", target="friend"),
            @Mapping(source="status", target="status"),
            @Mapping(source="createdDate", target="createdDate"),
            @Mapping(source="addedBy", target="addedBy"),
    })
    Friendship toFriendship(FriendshipEntity friendshipEntity);

    @Mappings({
            @Mapping(source="id", target="friendshipGuid"),
            @Mapping(source="friend", target="friend"),
            @Mapping(source="status", target="status"),
            @Mapping(source="createdDate", target="createdDate"),
            @Mapping(source="addedBy", target="addedBy"),
    })
    FriendshipEntity toFriendshipEntity(Friendship friendship);
}
