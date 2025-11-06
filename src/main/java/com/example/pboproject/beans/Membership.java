package com.example.pboproject.beans;

public class Membership {
    private int memberId;
    private String  memberName;
    private int phoneNoMember;
    private int totalPoints;

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Membership(int memberId, String memberName, int phoneNoMember) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.phoneNoMember = phoneNoMember;
    }

    public Membership() {

    }

    public int getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public int getPhoneNoMember() {
        return phoneNoMember;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setPhoneNoMember(int phoneNoMember) {
        this.phoneNoMember = phoneNoMember;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
}
