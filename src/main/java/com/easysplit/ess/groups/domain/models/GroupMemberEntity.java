package com.easysplit.ess.groups.domain.models;

/**
 * TODO Add comments
 */
public class GroupMemberEntity {
    private String groupGuid;
    private String memberGuid;

    public GroupMemberEntity() {

    }

    public String getGroupGuid() {
        return groupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        this.groupGuid = groupGuid;
    }

    public String getMemberGuid() {
        return memberGuid;
    }

    public void setMemberGuid(String memberGuid) {
        this.memberGuid = memberGuid;
    }
}
