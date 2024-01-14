package com.easysplit.ess.shared.utils;

import com.easysplit.shared.domain.models.ErrorResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TestRESTHelper {
    private final String BASE_URL;
    private final TestRestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public TestRESTHelper(Environment environment, TestRestTemplate testRestTemplate) {
        this.restTemplate = testRestTemplate;
        this.BASE_URL = environment.getProperty("base.url");
    }

    /**
     * Performs an HTTP (POST) request to the path specified.
     *
     * @param path endpoint url
     * @param body request body
     * @param statusCode expected status code
     * @return response object
     */
    public Object post(String path, Object body, HttpStatus statusCode) {
        return post(path, body, body.getClass(), statusCode);
    }

    /**
     * Performs an HTTP (POST) request to the path specified.
     *
     * @param path endpoint url
     * @param body request body
     * @param responseClass expected response class
     * @param statusCode expected status code
     * @return response object
     */
    public Object post(String path, Object body, Class<?> responseClass, HttpStatus statusCode) {
        Object object = null;
        
        try {
            ResponseEntity<?> responseEntity =  restTemplate.postForEntity(BASE_URL + path, body, String.class);

            if(!responseEntity.getStatusCode().equals(statusCode)) {
                throw new AssertionError("Actual status code defers from the expected status code, actual: "
                        + responseEntity.getStatusCode() + " expected: " + statusCode + ". Body: " + responseEntity.getBody());
            }

            if(responseEntity.getStatusCode().isError()) {
                throw new AssertionError(responseEntity.getBody());
            }

            if(responseEntity.getStatusCode().is2xxSuccessful()) {
                object = objectMapper.readValue((String) responseEntity.getBody(), responseClass);
            }
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        
        return object;
    }

    /**
     * Performs an HTTP (POST) request expecting an error response.
     *
     * @param path endpoint url
     * @param body request body
     * @param statusCode expected status code
     * @return error response
     */
    public ErrorResponse failPost(String path, Object body, HttpStatus statusCode) {
        ErrorResponse errorResponse = null;

        try {
            ResponseEntity<?> responseEntity =  restTemplate.postForEntity(BASE_URL + path, body, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new AssertionError(responseEntity.getBody());
            }

            if(responseEntity.getStatusCode().isError()) {
                if(!responseEntity.getStatusCode().equals(statusCode)) {
                    throw new AssertionError("Actual status code defers from the expected status code, actual: "
                            + responseEntity.getStatusCode() + " expected: " + statusCode + ". Body: " + responseEntity.getBody());
                }

                JsonNode jsonNode = objectMapper.readTree((String) responseEntity.getBody());
                errorResponse = new ErrorResponse(jsonNode.get("errorTitle").asText(), jsonNode.get("errorMessage").asText());
            }
        } catch (Exception e) {
            throw new AssertionError(e);
        }

        return errorResponse;
    }

    /**
     * Performs an HTTP (GET) request to the path specified
     *
     * @param path endpoint url
     * @param responseClass expected response class
     * @param statusCode status code
     * @return response object
     */
    public Object get(String path, Class<?> responseClass, HttpStatus statusCode) {
        Object object = null;

        try {
            ResponseEntity<?> responseEntity = get(path);

            if(!responseEntity.getStatusCode().equals(statusCode)) {
                throw new AssertionError("Actual status code defers from the expected status code, actual: "
                        + responseEntity.getStatusCode() + " expected: " + statusCode + ". Body: " + responseEntity.getBody());
            }

            if(responseEntity.getStatusCode().isError()) {
                throw new AssertionError(responseEntity.getBody());
            }

            if(responseEntity.getStatusCode().is2xxSuccessful()) {
                object = objectMapper.readValue((String) responseEntity.getBody(), responseClass);
            }
        } catch (Exception e) {
            throw new AssertionError(e);
        }

        return object;
    }

    /**
     * Performs an HTTP (GET) request without validations
     *
     * @param path endpoint url
     * @return json response object as string
     */
    public ResponseEntity<?> get(String path) {
        try {
            return restTemplate.getForEntity(BASE_URL + path, String.class);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    /**
     * Performs an HTTP (GET) expecting an error response
     *
     * @param path endpoint url
     * @param statusCode expected status code
     * @return error response
     */
    public ErrorResponse failGet(String path, HttpStatus statusCode) {
        ErrorResponse errorResponse = null;

        try {
            ResponseEntity<?> responseEntity =  restTemplate.getForEntity(BASE_URL + path, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new AssertionError(responseEntity.getBody());
            }

            if(responseEntity.getStatusCode().isError()) {
                if(!responseEntity.getStatusCode().equals(statusCode)) {
                    throw new AssertionError("Actual status code defers from the expected status code, actual: "
                            + responseEntity.getStatusCode() + " expected: " + statusCode + ". Body: " + responseEntity.getBody());
                }

                JsonNode jsonNode = objectMapper.readTree((String) responseEntity.getBody());
                errorResponse = new ErrorResponse(jsonNode.get("errorTitle").asText(), jsonNode.get("errorMessage").asText());
            }
        } catch (Exception e) {
            throw new AssertionError(e);
        }

        return errorResponse;
    }

    /**
     * Performs an HTTP (DELETE) request to the path specified,
     * performs a HTTP (GET) request to validate that the resource was deleted
     *
     * @param path endpoint url
     */
    public void delete(String path) {
        restTemplate.delete(BASE_URL + path);

        ResponseEntity<?> responseEntity = get(path);
        if (!responseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            throw new AssertionError("Something went wrong while deleting the resource "
                    + responseEntity.getBody());
        }
    }

    /**
     * Performs and HTTP (DELETE) request expecting an error response
     *
     * @param path endpoint url
     * @param statusCode expected status code
     * @return error response
     */
    public ErrorResponse failDelete(String path, HttpStatus statusCode) {
        ErrorResponse errorResponse = null;

        try {
            ResponseEntity<?> responseEntity =  restTemplate.exchange(BASE_URL + path, HttpMethod.DELETE, null, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new AssertionError(responseEntity.getBody());
            }

            if(responseEntity.getStatusCode().isError()) {
                if(!responseEntity.getStatusCode().equals(statusCode)) {
                    throw new AssertionError("Actual status code defers from the expected status code, actual: "
                            + responseEntity.getStatusCode() + " expected: " + statusCode + ". Body: " + responseEntity.getBody());
                }

                JsonNode jsonNode = objectMapper.readTree((String) responseEntity.getBody());
                errorResponse = new ErrorResponse(jsonNode.get("errorTitle").asText(), jsonNode.get("errorMessage").asText());
            }
        } catch (Exception e) {
            throw new AssertionError(e);
        }

        return errorResponse;
    }
}
