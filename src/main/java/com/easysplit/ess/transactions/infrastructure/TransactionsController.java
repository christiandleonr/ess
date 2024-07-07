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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {
    private final String CLASS_NAME = TransactionsController.class.getName();
    private final TransactionsService transactionsService;
    private final InfrastructureHelper infrastructureHelper;
    private final String TRANSACTIONS_RESOURCE = "/transactions";
    private static final Logger logger = LoggerFactory.getLogger(TransactionsController.class);

    public TransactionsController(TransactionsService transactionsService, InfrastructureHelper infrastructureHelper) {
        this.transactionsService = transactionsService;
        this.infrastructureHelper = infrastructureHelper;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = null;
        try {
            String createdById = infrastructureHelper.getAuthenticatedUserId();
            createdTransaction = transactionsService.createNewTransaction(transaction, createdById);

            createdTransaction.setLinks(infrastructureHelper.buildLinks(TRANSACTIONS_RESOURCE, createdTransaction.getId()));
        } catch (NotFoundException e) {
            logger.debug("{}.createTransaction() - Resource not found for transaction: {}", CLASS_NAME, transaction);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.debug("{}.createTransaction() - Invalid data for transaction: {}", CLASS_NAME, transaction);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error("{}.createTransaction() - Something went wrong while creating the transaction: {}", CLASS_NAME, transaction, e);
            throw e;
        } catch (Exception e) {
            logger.error("{}.createTransaction() - Something went wrong while creating the transaction: {}", CLASS_NAME, transaction, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_TRANSACTION_ERROR_TITLE,
                    ErrorKeys.CREATE_TRANSACTION_ERROR_MESSAGE,
                    new Object[] {transaction},
                    e.getCause()
            );
        }

        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable(name = "id") String id){
        Transaction transaction = null;
        try{
            transaction = transactionsService.getTransaction(id);

            transaction.setLinks(infrastructureHelper.buildLinks(TRANSACTIONS_RESOURCE, id));
        } catch (NotFoundException e) {
            logger.debug("{} .getTransaction() - Transaction with id {} not found", CLASS_NAME, id);
            throw e;
        } catch (InternalServerErrorException e){
            logger.error("{} .getTransaction() - Something went wrong while reading the user with id {}", CLASS_NAME, id, e);
            throw e;
        } catch (Exception e){
            logger.error("{} .getTransaction() - Something went wrong while reading the user with id {}", CLASS_NAME, id, e);

            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.GET_TRANSACTION_ERROR_TITLE,
                    ErrorKeys.GET_TRANSACTION_ERROR_MESSAGE,
                    new Object[] {id},
                    e.getCause()
            );
        }

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable(name = "id") String id) {
        try {
            String authenticatedUserId = infrastructureHelper.getAuthenticatedUserId();
            transactionsService.deleteTransaction(id, authenticatedUserId);
        } catch (NotFoundException e) {
            logger.debug("{}.deleteTransaction() - Transaction with id {} not found", CLASS_NAME, id);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error("{}.deleteTransaction() - Something went wrong while deleting the transaction with id {}", CLASS_NAME, id, e);
            throw e;
        } catch (Exception e) {
            logger.error("{}.deleteTransaction() - Something went wrong while deleting the transaction with id {}", CLASS_NAME, id, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.DELETE_TRANSACTION_ERROR_TITLE,
                    ErrorKeys.DELETE_TRANSACTION_ERROR_MESSAGE,
                    new Object[] {id},
                    e.getCause()
            );
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
