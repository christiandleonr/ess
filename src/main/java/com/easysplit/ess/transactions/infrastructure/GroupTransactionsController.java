package com.easysplit.ess.transactions.infrastructure;

import com.easysplit.ess.transactions.domain.contracts.TransactionsService;
import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.IllegalArgumentException;
import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import com.easysplit.shared.domain.exceptions.NotFoundException;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groups/{id}/transactions")
public class GroupTransactionsController {
    private final String CLASS_NAME = GroupTransactionsController.class.getName();
    private static final Logger logger = LoggerFactory.getLogger(GroupTransactionsController.class);

    private final TransactionsService transactionsService;
    private final InfrastructureHelper infrastructureHelper;
    private final String TRANSACTIONS_RESOURCE = "/transactions";

    public GroupTransactionsController(TransactionsService transactionsService, InfrastructureHelper infrastructureHelper) {
        this.transactionsService = transactionsService;
        this.infrastructureHelper = infrastructureHelper;
    }

    @PostMapping
    public ResponseEntity<Void> createGroupTransactions(@PathVariable(name = "id") String groupId, List<Transaction> transactions) {
        try {
            String createdById = infrastructureHelper.getAuthenticatedUserId();
            transactionsService.bulkCreateTransaction(transactions, groupId, createdById);
        } catch (NotFoundException e) {
            logger.debug(CLASS_NAME + ".createGroupTransactions() - Resource not found for transaction list: " + transactions);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.debug(CLASS_NAME + ".createGroupTransactions() - Invalid data for transaction list: " + transactions);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error(CLASS_NAME + ".createGroupTransactions() - Something went wrong while creating the transactions: " + transactions, e);
            throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".createGroupTransactions() - Something went wrong while creating the transactions: " + transactions, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.BULK_CREATE_TRANSACTION_ERROR_TITLE,
                    ErrorKeys.BULK_CREATE_TRANSACTION_ERROR_MESSAGE,
                    null,
                    e.getCause()
            );
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
