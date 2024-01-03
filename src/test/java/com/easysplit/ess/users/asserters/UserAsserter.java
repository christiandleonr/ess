package com.easysplit.ess.users.asserters;

import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.utils.EssUtils;
import org.assertj.core.api.AbstractAssert;

public class UserAsserter extends AbstractAssert<UserAsserter, User> {

    public UserAsserter(User actual) {
        super(actual, UserAsserter.class);
    }

    public UserAsserter assertUser(User expected) {
        if (this.actual == null) {
            failWithMessage("User cannot be empty");
        }

        if (expected.getId() != null) {
            String expectedId = expected.getId();
            String actualId = this.actual.getId();
            if (EssUtils.isNullOrEmpty(actualId)) {
                failWithMessage("User id cannot be null nor empty.");
            }

            if (!expectedId.equals(actualId)) {
                failWithMessage("Expected " + actualId + " and actual "
                        + expectedId + " ids do not match.");
            }
        }

        if (expected.getName() != null) {
            String expectedName = expected.getName();
            String actualName = this.actual.getName();
            if (EssUtils.isNullOrEmpty(actualName)) {
                failWithMessage("User name cannot be null nor empty.");
            }

            if (!expectedName.equals(actualName)) {
                failWithMessage("Expected - " + expectedName + " and actual - "
                        + actualName + " names do not match.");
            }
        }

        if (expected.getLastname() != null) {
            String expectedLastname = expected.getLastname();
            String actualLastname = this.actual.getLastname();
            if (EssUtils.isNullOrEmpty(actualLastname)) {
                failWithMessage("User lastname cannot be null nor empty.");
            }

            if (!expectedLastname.equals(actualLastname)) {
                failWithMessage("Expected - " + expectedLastname + " and actual - "
                        + actualLastname + " lastnames do not match.");
            }
        }

        if (expected.getUsername() != null) {
            String expectedUsername = expected.getUsername();
            String actualUsername = this.actual.getUsername();
            if (EssUtils.isNullOrEmpty(actualUsername)) {
                failWithMessage("User username cannot be null nor empty.");
            }

            if (!expectedUsername.equals(actualUsername)) {
                failWithMessage("Expected - " + expectedUsername + " and actual - "
                        + actualUsername + " usernames do not match.");
            }
        }

        if (expected.getEmail() != null) {
            String expectedEmail = expected.getEmail();
            String actualEmail = this.actual.getEmail();
            if (EssUtils.isNullOrEmpty(actualEmail)) {
                failWithMessage("User email cannot be null nor empty.");
            }

            if (!expectedEmail.equals(actualEmail)) {
                failWithMessage("Expected - " + expectedEmail + " and actual - "
                        + actualEmail + " emails do not match.");
            }
        }

        if (expected.getPhone() != null) {
            String expectedPhone = expected.getPhone();
            String actualPhone = this.actual.getPhone();
            if (EssUtils.isNullOrEmpty(actualPhone)) {
                failWithMessage("User email cannot be null nor empty.");
            }

            if (!expectedPhone.equals(actualPhone)) {
                failWithMessage("Expected - " + expectedPhone + " and actual - "
                        + actualPhone + " phones do not match.");
            }
        }

        return this;
    }
}
