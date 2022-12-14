package com.gnrc.telehealth.Model;

import java.io.Serializable;

public class MemberDetailsForDialogModel implements Serializable {
    private String member_id,  memberName, editTextValueSys,editTextValueDia,typeSpinner, editTextValueValue,
            covidStatus, doseStatus,noVaccineReason, smokingStatus,alcoholStatus, atalAmritStatus, ayushmanStatus,
    memberSurveyId, groupSurveyID, timeStamp,familyID, videoPath, HeadName, HeadAddress, memberList;
    private Boolean checkedStatus;
    private int checkedId;

    //))))))))))))))))))))))))))))))))

    private boolean isSmoker,isAtal,isTeleMed,isOpd,isAmbulance,isChecked;
    private boolean isAlcoholic,isAyush;

    //))))))))))))))))))))))))))))))))


    public String getMemberList() {
        return memberList;
    }

    public void setMemberList(String memberList) {
        this.memberList = memberList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getHeadName() {
        return HeadName;
    }

    public void setHeadName(String headName) {
        HeadName = headName;
    }

    public String getHeadAddress() {
        return HeadAddress;
    }

    public void setHeadAddress(String headAddress) {
        HeadAddress = headAddress;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getFamilyID() {
        return familyID;
    }

    public void setFamilyID(String familyID) {
        this.familyID = familyID;
    }

    public String getGroupSurveyID() {
        return groupSurveyID;
    }

    public void setGroupSurveyID(String groupSurveyID) {
        this.groupSurveyID = groupSurveyID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isTeleMed() {
        return isTeleMed;
    }

    public void setTeleMed(boolean teleMed) {
        isTeleMed = teleMed;
    }

    public boolean isOpd() {
        return isOpd;
    }

    public void setOpd(boolean opd) {
        isOpd = opd;
    }

    public boolean isAmbulance() {
        return isAmbulance;
    }

    public void setAmbulance(boolean ambulance) {
        isAmbulance = ambulance;
    }

    public String getMemberSurveyId() {
        return memberSurveyId;
    }

    public void setMemberSurveyId(String memberSurveyId) {
        this.memberSurveyId = memberSurveyId;
    }

    public boolean isAtal() {
        return isAtal;
    }

    public void setAtal(boolean atal) {
        isAtal = atal;
    }

    public boolean isAyush() {
        return isAyush;
    }

    public void setAyush(boolean ayush) {
        isAyush = ayush;
    }


    public String getSmokingStatus() {
        return smokingStatus;
    }

    public String getAtalAmritStatus() {
        return atalAmritStatus;
    }

    public void setAtalAmritStatus(String atalAmritStatus) {
        this.atalAmritStatus = atalAmritStatus;
    }

    public String getAyushmanStatus() {
        return ayushmanStatus;
    }

    public void setAyushmanStatus(String ayushmanStatus) {
        this.ayushmanStatus = ayushmanStatus;
    }

    public void setSmokingStatus(String smokingStatus) {
        this.smokingStatus = smokingStatus;
    }

    public String getAlcoholStatus() {
        return alcoholStatus;
    }

    public void setAlcoholStatus(String alcoholStatus) {
        this.alcoholStatus = alcoholStatus;
    }

    public int getCheckedId() {
        return checkedId;
    }

    public void setCheckedId(int checkedId) {
        this.checkedId = checkedId;
    }

    public Boolean getCheckedStatus() {
        return checkedStatus;
    }

    public void setCheckedStatus(Boolean checkedStatus) {
        this.checkedStatus = checkedStatus;
    }

    public String getCovidStatus() {
        return covidStatus;
    }

    public void setCovidStatus(String covidStatus) {
        this.covidStatus = covidStatus;
    }

    public String getDoseStatus() {
        return doseStatus;
    }

    public void setDoseStatus(String doseStatus) {
        this.doseStatus = doseStatus;
    }

    public String getNoVaccineReason() {
        return noVaccineReason;
    }

    public void setNoVaccineReason(String noVaccineReason) {
        this.noVaccineReason = noVaccineReason;
    }

    public String getTypeSpinner() {
        return typeSpinner;
    }

    public void setTypeSpinner(String typeSpinner) {
        this.typeSpinner = typeSpinner;
    }

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


    //((((((((((((((((((((((((((((((((((((((((((((((

    public boolean isSmoker() {
        return isSmoker;
    }

    public void setSmoker(boolean smoker) {
        isSmoker = smoker;
    }

    public boolean isAlcoholic() {
        return isAlcoholic;
    }

    public void setAlcoholic(boolean alcoholic) {
        isAlcoholic = alcoholic;
    }

    //((((((((((((((((((((((((((((((((((((((((((((((
}
