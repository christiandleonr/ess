package com.easysplit.ess.transactions.domain.models;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mappings({
            @Mapping(source="id", target="transactionGuid"),
            @Mapping(source="name", target="name"),
            @Mapping(target="currency", ignore = true),
            @Mapping(target="debt", ignore = true),
            @Mapping(target="group", ignore = true),
            @Mapping(target="creditor", ignore = true),
            @Mapping(target="debtor", ignore = true),
            @Mapping(target="createdBy", ignore = true),
            @Mapping(source="createdDate", target="createdDate"),
            @Mapping(target="updatedBy", ignore = true),
            @Mapping(source="updatedDate", target="updatedDate"),
    })
    TransactionEntity toTransactionEntity(Transaction transaction);

    @Mappings({
            @Mapping(source="transactionGuid", target="id"),
            @Mapping(source="name", target="name"),
            @Mapping(target="currency", ignore = true),
            @Mapping(target="debt", ignore = true),
            @Mapping(target="group", ignore = true),
            @Mapping(target="creditor", ignore = true),
            @Mapping(target="debtor", ignore = true),
            @Mapping(target="createdBy", ignore = true),
            @Mapping(source="createdDate", target="createdDate"),
            @Mapping(target="updatedBy", ignore = true),
            @Mapping(source="updatedDate", target="updatedDate"),
    })
    Transaction toTransaction(TransactionEntity transactionEntity);
}
