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
            @Mapping(source="currency", target="currency"),
            @Mapping(source="createdDate", target="createdDate"),
            @Mapping(source="updatedDate", target="updatedDate"),
    })
    TransactionEntity toTransactionEntity(Transaction transaction);

    @Mappings({
            @Mapping(source="transactionGuid", target="id"),
            @Mapping(source="name", target="name"),
            @Mapping(source="currency", target="currency"),
            @Mapping(source="createdDate", target="createdDate"),
            @Mapping(source="updatedDate", target="updatedDate"),
    })
    Transaction toTransaction(TransactionEntity transactionEntity);
}
