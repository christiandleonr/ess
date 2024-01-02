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
            createdTransaction = transactionsService.createNewTransaction(transaction);

            createdTransaction.setLinks(infrastructureHelper.buildLinks(TRANSACTIONS_RESOURCE, createdTransaction.getId()));
        } catch (NotFoundException e) {
            logger.debug(CLASS_NAME + ".createTransaction() - Resource not found for transaction: " + transaction);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.debug(CLASS_NAME + ".createTransaction() - Invalid data for transaction: " + transaction);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error(CLASS_NAME + ".createTransaction() - Something went wrong while creating the transaction: " + transaction, e);
            throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".createGroup() - Something went wrong while creating the transaction: " + transaction, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_GROUP_ERROR_TITLE,
                    ErrorKeys.CREATE_GROUP_ERROR_MESSAGE,
                    new Object[] {transaction},
                    e
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
            logger.debug(CLASS_NAME + " .getTransaction() - Transaction with id " + id + " not found");
            throw e;
        } catch (InternalServerErrorException e){
            logger.error(CLASS_NAME + " .getTransaction() - Something went wrong while reading the user with id " + id, e);
            throw e;
        } catch (Exception e){
            logger.error(CLASS_NAME + " .getTransaction() - Something went wrong while reading the user with id " + id, e);

            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.GET_TRANSACTION_ERROR_TITLE,
                    ErrorKeys.GET_TRANSACTION_ERROR_MESSAGE,
                    new Object[] {id},
                    e
            );
        }

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }
}
