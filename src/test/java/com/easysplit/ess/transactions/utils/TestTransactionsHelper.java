package com.easysplit.ess.transactions.utils;

import com.easysplit.shared.domain.models.ResourceList;
import com.easysplit.shared.utils.TestRESTHelper;
import com.easysplit.ess.transactions.domain.models.Transaction;
import com.easysplit.shared.domain.models.ErrorResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestTransactionsHelper {
    private final String TRANSACTIONS_PATH = "/api/transactions";
    private final String GROUPS_TRANSACTIONS_PATH = "/api/groups/%s/transactions";
    @Autowired
    private TestRESTHelper testRESTHelper;

    public Transaction createTransaction(Transaction transaction, HttpStatus statusCode) {
        return (Transaction) this.testRESTHelper.post(TRANSACTIONS_PATH, transaction, Transaction.class, statusCode);
    }

    public ErrorResponse failCreateTransaction(Transaction transaction, HttpStatus statusCode) {
        return this.testRESTHelper.failPost(TRANSACTIONS_PATH, transaction, statusCode);
    }

    public void createGroupTransactions(String groupId, List<Transaction> transactions, HttpStatus statusCode) {
        this.testRESTHelper.postNoResponse(
                String.format(GROUPS_TRANSACTIONS_PATH, groupId),
                transactions,
                statusCode
        );
    }

    public ErrorResponse failCreateGroupTransactions(String groupId, List<Transaction> transactions, HttpStatus statusCode) {
        return this.testRESTHelper.failPost(
            String.format(GROUPS_TRANSACTIONS_PATH, groupId),
            transactions,
            statusCode
        );
    }

    public Transaction getTransaction(String id, Class<?> responseClass, HttpStatus statusCode) {
        return (Transaction) this.testRESTHelper.get(TRANSACTIONS_PATH + "/" + id, responseClass, statusCode);
    }

    public ErrorResponse failGet(String id, HttpStatus statusCode) {
        return this.testRESTHelper.failGet(TRANSACTIONS_PATH + "/" + id, statusCode);
    }

    public ErrorResponse failGet(String id, HttpStatus statusCode, HttpHeaders headers) {
        return this.testRESTHelper.failGet(TRANSACTIONS_PATH + "/" + id, statusCode, headers);
    }

    public ResourceList<Transaction> listGroupTransactions(String groupId, HttpStatus statusCode) {
        return this.testRESTHelper.list(
            String.format(GROUPS_TRANSACTIONS_PATH, groupId),
            Transaction.class,
            statusCode
        );
    }

    public ErrorResponse failListGroupTransactions(String groupId, HttpStatus statusCode) {
        return this.failGet(String.format(GROUPS_TRANSACTIONS_PATH, groupId), statusCode);
    }

    public void deleteTransaction(String id) {
        this.testRESTHelper.delete(TRANSACTIONS_PATH + "/" + id);
    }

    public ErrorResponse failDelete(String id, HttpStatus statusCode) {
        return this.testRESTHelper.failDelete(TRANSACTIONS_PATH + "/" + id, statusCode);
    }
}

