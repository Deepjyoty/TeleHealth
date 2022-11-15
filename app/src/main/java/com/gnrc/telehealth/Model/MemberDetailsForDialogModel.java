package com.gnrc.telehealth.Model;

import java.io.Serializable;

public class MemberDetailsForDialogModel implements Serializable {
    private String member_id,  memberName, editTextValueSys,editTextValueDia,editTextValueValue;

    public String getEditTextValueSys() {
        return editTextValueSys;
    }

    public void setEditTextValueSys(String editTextValueSys) {
        this.editTextValueSys = editTextValueSys;
    }

    public String getEditTextValueDia() {
        return editTextValueDia;
    }

    public void setEditTextValueDia(String editTextValueDia) {
        this.editTextValueDia = editTextValueDia;
    }

    public String getEditTextValueValue() {
        return editTextValueValue;
    }

    public void setEditTextValueValue(String editTextValueValue) {
        this.editTextValueValue = editTextValueValue;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
