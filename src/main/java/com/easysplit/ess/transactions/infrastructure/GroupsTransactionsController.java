package com.easysplit.ess.transactions.infrastructure;

import com.easysplit.ess.transactions.domain.contracts.GroupsTransactionsService;
import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.IllegalArgumentException;
import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import com.easysplit.shared.domain.exceptions.NotFoundException;
import com.easysplit.shared.domain.models.ResourceList;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups/{id}/transactions")
public class GroupsTransactionsController {
    private final String CLASS_NAME = GroupsTransactionsController.class.getName();
    private static final Logger logger = LoggerFactory.getLogger(GroupsTransactionsController.class);

    private final GroupsTransactionsService groupsTransactionsService;
    private final InfrastructureHelper infrastructureHelper;

    public GroupsTransactionsController(GroupsTransactionsService groupsTransactionsService, InfrastructureHelper infrastructureHelper) {
        this.groupsTransactionsService = groupsTransactionsService;
        this.infrastructureHelper = infrastructureHelper;
    }

    @PostMapping
    public ResponseEntity<Void> createGroupTransactions(@PathVariable(name = "id") String groupId, List<Transaction> transactions) {
        try {
            String createdById = infrastructureHelper.getAuthenticatedUserId();
            groupsTransactionsService.bulkCreateTransaction(transactions, groupId, createdById);
        } catch (NotFoundException e) {
            logger.debug("{}.createGroupTransactions() - Resource not found for transaction list: {}", CLASS_NAME, transactions);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.debug("{}.createGroupTransactions() - Invalid data for transaction list: {}", CLASS_NAME, transactions);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error("{}.createGroupTransactions() - Something went wrong while creating the transactions: {}", CLASS_NAME, transactions, e);
            throw e;
        } catch (Exception e) {
            logger.error("{}.createGroupTransactions() - Something went wrong while creating the transactions: {}", CLASS_NAME, transactions, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.BULK_CREATE_TRANSACTION_ERROR_TITLE,
                    ErrorKeys.BULK_CREATE_TRANSACTION_ERROR_MESSAGE,
                    null,
                    e.getCause()
            );
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<ResourceList<Transaction>> getGroupTransactions(@PathVariable(name = "id") String groupId,
                                                                          @RequestParam(name = "limit") int limit,
                                                                          @RequestParam(name = "offset") int offset,
                                                                          @RequestParam(name = "totalCount") boolean totalCount) {
        ResourceList<Transaction> transactions = null;
        try {
            transactions = groupsTransactionsService.listTransactionsByGroup(groupId, limit, offset, totalCount);
        } catch (NotFoundException e) {
            logger.debug("{}.getGroupTransactions() - Resource not found", CLASS_NAME);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.debug("{}.getGroupTransactions() - Invalid data", CLASS_NAME);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error("{}.getGroupTransactions() - Something went wrong while listing the transactions for group:  {}", CLASS_NAME, groupId, e);
            throw e;
        } catch (Exception e) {
            logger.error("{}.getGroupTransactions() - Something went wrong while listing the transactions for group: {}", CLASS_NAME, groupId, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.LIST_TRANSACTIONS_BY_GROUP_ERROR_TITLE,
                    ErrorKeys.LIST_TRANSACTIONS_BY_GROUP_ERROR_MESSAGE,
                    new Object[]{ groupId },
                    e.getCause()
            );
        }

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
