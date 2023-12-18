package com.easysplit.ess.user.domain.models;

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

    /**
     * Get the Friendship status from a string
     *
     * @param status status
     * @return FriendshipStatus value
     */
    public static FriendshipStatus valueOfString(String status) {
        for (FriendshipStatus enumValue : FriendshipStatus.values()) {
            if (enumValue.value.equalsIgnoreCase(status)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("No constant with status " + status + " found");
    }
}
