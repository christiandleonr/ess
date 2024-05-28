package com.easysplit.ess.groups.asserters;

import com.easysplit.ess.groups.domain.models.Group;
import com.easysplit.ess.users.asserters.UsersListAsserter;
import com.easysplit.shared.utils.EssUtils;
import org.assertj.core.api.AbstractAssert;

public class GroupsAsserter extends AbstractAssert<GroupsAsserter, Group> {

    public GroupsAsserter(Group actual) {
        super(actual, GroupsAsserter.class);
    }

    public GroupsAsserter assertGroup(Group expected) {
        if (this.actual == null) {
            failWithMessage("Group cannot be empty");
        }

        if (expected.getId() != null) {
            String expectedId = expected.getId();
            String actualId = this.actual.getId();

            if (EssUtils.isNullOrEmpty(actualId)) {
                failWithMessage("Group's id cannot be null nor empty");
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
                failWithMessage("Group's name cannot be null nor empty.");
            }

            if (!expectedName.equals(actualName)) {
                failWithMessage("Expected - " + expectedName + " and actual - "
                        + actualName + " group names do not match.");
            }
        }

        if (expected.getDescription() != null) {
            String expectedDescription = expected.getDescription();
            String actualDescriptions = this.actual.getDescription();

            if (EssUtils.isNullOrEmpty(actualDescriptions)) {
                failWithMessage("Group's name cannot be null nor empty.");
            }

            if (!expectedDescription.equals(actualDescriptions)) {
                failWithMessage("Expected - " + expectedDescription + " and actual - "
                        + actualDescriptions + " group's descriptions do not match.");
            }
        }

        if (expected.getMembers() != null) {
            UsersListAsserter.assertListEquals(this.actual.getMembers(), expected.getMembers());
        }

        return this;
    }
}
