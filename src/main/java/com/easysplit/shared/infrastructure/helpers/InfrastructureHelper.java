package com.easysplit.shared.infrastructure.helpers;

import com.easysplit.ess.iam.domain.models.IamUserDetails;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.IllegalArgumentException;
import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import com.easysplit.shared.domain.exceptions.NotFoundException;
import com.easysplit.shared.domain.models.Link;
import com.easysplit.shared.utils.EssUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Helper class that contains utility methods to be used for the infrastructure layer
 */
@Component
public class InfrastructureHelper {
    private final String HOST_KEY = "Host";
    private final String SELF = "self";
    private final String HTTP_GET = "GET";
    private final MessageSource messageSource;

    public InfrastructureHelper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Get current date
     *
     * @return current date as Timestamp
     */
    public Timestamp getCurrentDate() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Generates an expiry date from the current date adding a duration time.
     *
     * @param duration duration in minutes
     * @return
     */
    public Timestamp getExpiryDate(int duration) {
        return new Timestamp(System.currentTimeMillis() + ((long) duration * 60 * 1000));
    }

    /**
     * Throws an InternalServerErrorException with the provided error title and error message
     *
     * @param errorTitleKey error title key
     * @param errorMessageKey error message key
     * @param t exception
     */
    public void throwInternalServerErrorException(String errorTitleKey,
                                                  String errorMessageKey,
                                                  Throwable t) {
        String errorTitle = messageSource.getMessage(errorTitleKey, null, null);
        String errorMessage = messageSource.getMessage(errorMessageKey, null, null);

        throw new InternalServerErrorException(errorTitle, errorMessage, t);
    }

    /**
     * Throws an InternalServerErrorException with the provided error title and error message
     *
     * @param errorTitleKey error title key
     * @param errorMessageKey error message key
     * @param args arguments
     * @param t exception
     */
    public void throwInternalServerErrorException(String errorTitleKey,
                                                  String errorMessageKey,
                                                  Object[] args,
                                                  Throwable t) {
        String errorTitle = messageSource.getMessage(errorTitleKey, null, null);
        String errorMessage = messageSource.getMessage(errorMessageKey, args, null);

        throw new InternalServerErrorException(errorTitle, errorMessage, t);
    }

    /**
     * Throws an IllegalArgumentException with the provided error title and error messages
     *
     * @param errorTitleKey error title key
     * @param errorMessageKey error message key
     * @param args arguments
     * @param locale locale
     */
    public void throwIllegalArgumentException(String errorTitleKey,
                                              String errorMessageKey,
                                              Object[] args,
                                              Locale locale) {
        String errorTitle = messageSource.getMessage(errorTitleKey, null, locale);
        String errorMessage = messageSource.getMessage(errorMessageKey, args, locale);

        throw new IllegalArgumentException(errorTitle, errorMessage);
    }

    /**
     * Throws an IllegalArgumentException with the provided error title and error messages
     *
     * @param errorTitleKey error title key
     * @param errorMessageKey error message key
     * @param args arguments
     */
    public void throwIllegalArgumentException(String errorTitleKey,
                                              String errorMessageKey,
                                              Object[] args) {
        throwIllegalArgumentException(errorTitleKey, errorMessageKey, args, null);
    }

    /**
     * Throws a NotFoundException with the provided error title and error message
     *
     * @param errorTitleKey error title key
     * @param errorMessageKey error message key
     * @param args arguments
     */
    public void throwNotFoundException(String errorTitleKey,
                                       String errorMessageKey,
                                       Object[] args) {
        String errorTitle = messageSource.getMessage(errorTitleKey, null, null);
        String errorMessage = messageSource.getMessage(errorMessageKey, args, null);

        throw new NotFoundException(errorTitle, errorMessage);
    }

    /**
     * Builds a list of reference links
     *
     * @param resource resource path
     * @param resourceId resource id
     * @return list of links
     */
    public List<Link> buildLinks(String resource, String resourceId) {
        return buildLinks(resource, resourceId, HTTP_GET);
    }

    /**
     * Builds a list of reference links
     *
     * @param resource resource path
     * @param resourceId resource id
     * @param method http method
     * @return list of links
     */
    public List<Link> buildLinks(String resource, String resourceId, String method) {
        return Arrays.asList(
                buildSelfLink(resource, resourceId, method)
        );
    }

    /**
     * Builds a self reference link
     *
     * @param resource resource path
     * @param resourceId resource id
     * @param method http method
     * @return self reference link
     */
    public Link buildSelfLink(String resource, String resourceId, String method) {
        Link link = new Link();

        link.setHref(buildHref(resource, resourceId));
        link.setMethod(method);
        link.setType(SELF);

        return link;
    }

    /**
     * Builds a href link for the resource provided
     *
     * @param resource resource path
     * @param resourceId resource id
     * @return href
     */
    public String buildHref(String resource, String resourceId) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String host = request.getHeader(HOST_KEY);

        return "http://" + host + "/api" + resource + "/" + resourceId;
    }

    /**
     * Use the current security context to get the id of the authenticated user
     */
    public String getAuthenticatedUserId() {
        String userId = null;

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof IamUserDetails) {
            userId = ((IamUserDetails) authentication.getPrincipal()).getId();
        }

        if (EssUtils.isNullOrEmpty(userId)) {
            throwIllegalArgumentException(
                    ErrorKeys.AUTHENTICATION_ILLEGALARGUMENT_TITLE,
                    ErrorKeys.AUTHENTICATION_INVALID_USERID_MESSAGE,
                    null
            );
        }

        return userId;
    }
}
