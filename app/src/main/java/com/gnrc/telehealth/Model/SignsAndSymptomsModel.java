package com.gnrc.telehealth.Model;

import java.io.Serializable;

public class SignsAndSymptomsModel implements Serializable {
    String ATR_CODE, PRT_CODE, PRT_DESC, PRT_DESC_ALT, PRT_DESC_BENG, PRT_SLNO, ATR_DESC, ATR_DESC_ALT, ATR_DESC_BENG, ATR_SLNO,
    member_name;

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getATR_CODE() {
        return ATR_CODE;
    }

    public void setATR_CODE(String ATR_CODE) {
        this.ATR_CODE = ATR_CODE;
    }

    public String getPRT_CODE() {
        return PRT_CODE;
    }

    public void setPRT_CODE(String PRT_CODE) {
        this.PRT_CODE = PRT_CODE;
    }

    public String getPRT_DESC() {
        return PRT_DESC;
    }

    public void setPRT_DESC(String PRT_DESC) {
        this.PRT_DESC = PRT_DESC;
    }

    public String getPRT_DESC_ALT() {
        return PRT_DESC_ALT;
    }

    public void setPRT_DESC_ALT(String PRT_DESC_ALT) {
        this.PRT_DESC_ALT = PRT_DESC_ALT;
    }

    public String getPRT_DESC_BENG() {
        return PRT_DESC_BENG;
    }

    public void setPRT_DESC_BENG(String PRT_DESC_BENG) {
        this.PRT_DESC_BENG = PRT_DESC_BENG;
    }

    public String getPRT_SLNO() {
        return PRT_SLNO;
    }

    public void setPRT_SLNO(String PRT_SLNO) {
        this.PRT_SLNO = PRT_SLNO;
    }

    public String getATR_DESC() {
        return ATR_DESC;
    }

    public void setATR_DESC(String ATR_DESC) {
        this.ATR_DESC = ATR_DESC;
    }

    public String getATR_DESC_ALT() {
        return ATR_DESC_ALT;
    }

    public void setATR_DESC_ALT(String ATR_DESC_ALT) {
        this.ATR_DESC_ALT = ATR_DESC_ALT;
    }

    public String getATR_DESC_BENG() {
        return ATR_DESC_BENG;
    }

    public void setATR_DESC_BENG(String ATR_DESC_BENG) {
        this.ATR_DESC_BENG = ATR_DESC_BENG;
    }

    public String getATR_SLNO() {
        return ATR_SLNO;
    }

    public void setATR_SLNO(String ATR_SLNO) {
        this.ATR_SLNO = ATR_SLNO;
    }
}
