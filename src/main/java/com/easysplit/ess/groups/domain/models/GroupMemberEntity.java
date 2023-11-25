package com.easysplit.ess.groups.domain.models;

/**
 * Class that represents the table <i>group_members</i>
 */
public class GroupMemberEntity {
    private String groupGuid;
    private String memberGuid;

    public GroupMemberEntity() {

    }

    public GroupMemberEntity(String groupGuid, String memberGuid) {
        this.groupGuid = groupGuid;
        this.memberGuid = memberGuid;
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
