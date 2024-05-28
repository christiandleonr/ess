package com.easysplit.shared.utils;

import com.easysplit.ess.iam.domain.models.Auth;
import com.easysplit.ess.iam.domain.models.Token;
import com.easysplit.shared.domain.models.ErrorResponse;
import com.easysplit.shared.domain.models.ResourceList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class TestRESTHelper {
    private final String BASE_URL;
    private final String IAM_PATH = "/api/iam";
    private final TestRestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.security.test.user}")
    private String testUsername;
    @Value("${spring.security.test.password}")
    private String testPassword;

    @Autowired
    public TestRESTHelper(Environment environment, TestRestTemplate testRestTemplate) {
        this.restTemplate = testRestTemplate;
        this.BASE_URL = environment.getProperty("base.url");
    }

    /**
     * Use a system managed user for testing authentication
     *
     * @return generated token
     */
    private Token authenticate() {
        return authenticate(testUsername, testPassword);
    }

    /**
     * Use a custom user for testing authentication
     *
     * @param username user username
     * @param password user password
     * @return generated token
     */
    public Token authenticate(String username, String password) {
        Auth auth = new Auth(username, password);

        return (Token) postNoAuth(IAM_PATH, auth, Token.class, HttpStatus.OK);
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
     * Performs an HTTP (POST) request to the path specified with authentication
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
            HttpEntity<Object> requestBody = new HttpEntity<>(body, buildAuthenticationHeader());
            ResponseEntity<?> responseEntity =  restTemplate.postForEntity(BASE_URL + path, requestBody, String.class);

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
     * Performs an HTTP (POST) request to the path specified without authentication
     *
     * @param path endpoint url
     * @param body request body
     * @param responseClass expected response class
     * @param statusCode expected status code
     * @return response object
     */
    public Object postNoAuth(String path, Object body, Class<?> responseClass, HttpStatus statusCode) {
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
     * Performs an HTTP (POST) request to the path specified with authentication
     *
     * @param path endpoint url
     * @param body request body
     * @param responseClass expected response class
     * @param statusCode expected status code
     * @return response object
     */
    public Object postCustomAuth(String path, Object body, Class<?> responseClass, HttpStatus statusCode, HttpHeaders authHeaders) {
        Object object = null;

        try {
            HttpEntity<Object> requestBody = new HttpEntity<>(body, authHeaders);
            ResponseEntity<?> responseEntity =  restTemplate.postForEntity(BASE_URL + path, requestBody, String.class);

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
     * Performs an HTTP (POST) request to the path specified without expecting a response
     *
     * @param path endpoint url
     * @param body request body
     * @param statusCode expected status code
     */
    public void postNoResponse(String path, Object body, HttpStatus statusCode) {
        Object object = null;

        try {
            HttpEntity<Object> requestBody = new HttpEntity<>(body, buildAuthenticationHeader());
            ResponseEntity<?> responseEntity =  restTemplate.postForEntity(BASE_URL + path, requestBody, String.class);

            if(!responseEntity.getStatusCode().equals(statusCode)) {
                throw new AssertionError("Actual status code defers from the expected status code, actual: "
                        + responseEntity.getStatusCode() + " expected: " + statusCode + ". Body: " + responseEntity.getBody());
            }

            if(responseEntity.getStatusCode().isError()) {
                throw new AssertionError(responseEntity.getBody());
            }
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Performs an HTTP (POST) request expecting an error response with authentication
     *
     * @param path endpoint url
     * @param body request body
     * @param statusCode expected status code
     * @return error response
     */
    public ErrorResponse failPost(String path, Object body, HttpStatus statusCode) {
        return failPost(path, body, statusCode, buildAuthenticationHeader());
    }

    /**
     * Performs an HTTP (POST) request expecting an error response with authentication
     *
     * @param path endpoint url
     * @param body request body
     * @param statusCode expected status code
     * @param httpHeaders header for the request
     * @return error response
     */
    public ErrorResponse failPost(String path, Object body, HttpStatus statusCode, HttpHeaders httpHeaders) {
        ErrorResponse errorResponse = null;

        try {
            HttpEntity<Object> requestBody = new HttpEntity<>(body, httpHeaders);
            ResponseEntity<?> responseEntity =  restTemplate.postForEntity(BASE_URL + path, requestBody, String.class);

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
     * Performs an HTTP (POST) request expecting an error response without authentication
     *
     * @param path endpoint url
     * @param body request body
     * @param statusCode expected status code
     * @return error response
     */
    public ErrorResponse failPostNoAuth(String path, Object body, HttpStatus statusCode) {
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
     * Performs an HTTP (GET) request to the path specified with authentication
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
     * Performs an HTTP (GET) request without status code validations and using system authentication
     *
     * @param path endpoint url
     * @return json response object as string
     */
    public ResponseEntity<?> get(String path) {
        try {
            return restTemplate.exchange(BASE_URL + path,
                    HttpMethod.GET,
                    new HttpEntity<>(buildAuthenticationHeader()),
                    String.class);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    /**
     * Performs an HTTP (GET) request without status code validations and using custom authentication
     *
     * @param path endpoint url
     * @return json response object as string
     */
    public ResponseEntity<?> getWithCustomAuth(String path, HttpHeaders authHeaders) {
        try {
            return restTemplate.exchange(BASE_URL + path,
                    HttpMethod.GET,
                    new HttpEntity<>(authHeaders),
                    String.class);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    /**
     * Performs an HTTP (GET) expecting an error response with authentication
     *
     * @param path endpoint url
     * @param statusCode expected status code
     * @return error response
     */
    public ErrorResponse failGet(String path, HttpStatus statusCode) {
        return failGet(path, statusCode, buildAuthenticationHeader());
    }

    /**
     * Performs an HTTP (GET) expecting an error response with http headers provided
     *
     * @param path endpoint url
     * @param statusCode expected status code
     * @return error response
     */
    public ErrorResponse failGet(String path, HttpStatus statusCode, HttpHeaders httpHeaders) {
        ErrorResponse errorResponse = null;

        try {
            ResponseEntity<?> responseEntity = restTemplate.exchange(BASE_URL + path,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                String.class);

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
     * TODO Add comments
     * @param path
     * @param dataClass
     * @param statusCode
     * @return
     * @param <T>
     */
    public <T> ResourceList<T> list(String path, Class<T> dataClass, HttpStatus statusCode) {
        ResourceList<T> object = null;

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
                TypeReference<ResourceList<T>> typeRef = new TypeReference<ResourceList<T>>() {
                    @Override
                    public Type getType() {
                        return TypeFactory.defaultInstance().constructParametricType(ResourceList.class, dataClass);
                    }
                };

                object = objectMapper.readValue((String) responseEntity.getBody(), typeRef);
            }
        } catch (Exception e) {
            throw new AssertionError(e);
        }

        return object;
    }

    /**
     * TODO Add comments
     * @param path
     * @param dataClass
     * @param statusCode
     * @return
     * @param <T>
     */
    public <T> ResourceList<T> listWithCustomAuth(String path, Class<T> dataClass, HttpStatus statusCode, HttpHeaders authHeaders) {
        ResourceList<T> object = null;

        try {
            ResponseEntity<?> responseEntity = getWithCustomAuth(path, authHeaders);

            if(!responseEntity.getStatusCode().equals(statusCode)) {
                throw new AssertionError("Actual status code defers from the expected status code, actual: "
                        + responseEntity.getStatusCode() + " expected: " + statusCode + ". Body: " + responseEntity.getBody());
            }

            if(responseEntity.getStatusCode().isError()) {
                throw new AssertionError(responseEntity.getBody());
            }

            if(responseEntity.getStatusCode().is2xxSuccessful()) {
                TypeReference<ResourceList<T>> typeRef = new TypeReference<ResourceList<T>>() {
                    @Override
                    public Type getType() {
                        return TypeFactory.defaultInstance().constructParametricType(ResourceList.class, dataClass);
                    }
                };

                object = objectMapper.readValue((String) responseEntity.getBody(), typeRef);
            }
        } catch (Exception e) {
            throw new AssertionError(e);
        }

        return object;
    }

    /**
     * Performs an HTTP (DELETE) request to the path specified,
     * performs an HTTP (GET) request to validate that the resource was deleted
     *
     * @param path endpoint url
     */
    public void delete(String path) {
        restTemplate.exchange(BASE_URL + path,
                HttpMethod.DELETE,
                new HttpEntity<>(buildAuthenticationHeader()),
                String.class);

        ResponseEntity<?> responseEntity = get(path);
        if (!responseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            throw new AssertionError("Something went wrong while deleting the resource "
                    + responseEntity.getBody());
        }
    }

    /**
     * Performs and HTTP (DELETE) request expecting an error response with authentication
     *
     * @param path endpoint url
     * @param statusCode expected status code
     * @return error response
     */
    public ErrorResponse failDelete(String path, HttpStatus statusCode) {
        ErrorResponse errorResponse = null;

        try {
            ResponseEntity<?> responseEntity =  restTemplate.exchange(BASE_URL + path, HttpMethod.DELETE, new HttpEntity<>(buildAuthenticationHeader()), String.class);

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
     * Builds http headers with authentication details using a bearer token for a system managed user
     *
     * @return http headers with authentication details
     */
    private HttpHeaders buildAuthenticationHeader() {
        Token token = authenticate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token.getToken());

        return headers;
    }

    /**
     * Builds http headers with authentication details using a bearer token for a custom user
     *
     * @param username user username
     * @param password user password
     * @return http headers with authentication details
     */
    public HttpHeaders buildAuthenticationHeader(String username, String password) {
        Token token = authenticate(username, password);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token.getToken());

        return headers;
    }
}
