package com.easysplit.ess.iam.domain.models;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
    RefreshTokenMapper INSTANCE = Mappers.getMapper(RefreshTokenMapper.class);

    @Mappings({
            @Mapping(source="id", target="id"),
            @Mapping(source="token", target="token"),
    })
    RefreshTokenEntity toRefreshTokenEntity(RefreshToken refreshToken);

    @Mappings({
            @Mapping(source="id", target="id"),
            @Mapping(source="token", target="token"),
    })
    RefreshToken toRefreshToken(RefreshTokenEntity refreshToken);
}
