package com.msw.mesapp.bean.id_management;

public class MemberData {
    private String member_code;
    private String member_name;

    public MemberData(String member_code, String member_name) {
        this.member_code = member_code;
        this.member_name = member_name;
    }

    public String getMember_code() {
        return member_code;
    }

    public void setMember_code(String member_code) {
        this.member_code = member_code;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }
}
