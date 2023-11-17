package com.easysplit.ess.friendships.domain.models;

public enum FriendshipStatus {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected");

    private final String value;

    FriendshipStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
