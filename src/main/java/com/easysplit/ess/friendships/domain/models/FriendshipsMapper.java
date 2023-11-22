package com.easysplit.ess.friendships.domain.models;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface FriendshipsMapper {
    @Mappings({
            @Mapping(source="friendshipGuid", target="id"),
            @Mapping(source="friend", target="friend"),
            @Mapping(source="status", target="status"),
            @Mapping(source="createdDate", target="createdDate"),
            @Mapping(source="createdBy", target="createdBy"),
    })
    Friendship toFriendship(FriendshipEntity friendshipEntity);

    @Mappings({
            @Mapping(source="id", target="friendshipGuid"),
            @Mapping(source="friend", target="friend"),
            @Mapping(source="status", target="status"),
            @Mapping(source="createdDate", target="createdDate"),
            @Mapping(source="createdBy", target="createdBy"),
    })
    FriendshipEntity toFriendshipEntity(Friendship friendship);
}
