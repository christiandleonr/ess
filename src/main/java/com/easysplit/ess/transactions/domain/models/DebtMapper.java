package com.easysplit.ess.transactions.domain.models;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DebtMapper {
    DebtMapper INSTANCE = Mappers.getMapper(DebtMapper.class);

    @Mappings({
            @Mapping(source="id", target="debtGuid"),
            @Mapping(target="totalAmount", ignore = true),
            @Mapping(target="debt", ignore = true),
            @Mapping(source="debtSettled", target="debtSettled"),
            @Mapping(source="revision", target="revision"),
            @Mapping(target="createdBy", ignore = true),
            @Mapping(source="createdDate", target="createdDate"),
    })
    DebtEntity toDebtEntity(Debt debt);

    @Mappings({
            @Mapping(source="debtGuid", target="id"),
            @Mapping(target="totalAmount", ignore = true),
            @Mapping(target="debt", ignore = true),
            @Mapping(source="debtSettled", target="debtSettled"),
            @Mapping(source="revision", target="revision"),
            @Mapping(target="createdBy", ignore = true),
            @Mapping(source="createdDate", target="createdDate"),
    })
    Debt toDebt(DebtEntity debtEntity);
}
