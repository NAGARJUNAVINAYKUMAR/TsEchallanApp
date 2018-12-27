package com.tspolice.echallan.models;

import java.io.Serializable;

/**
 * Created by Srinivas on 11/9/2018.
 */

public class SettingsModel implements Serializable{

    private String str_PSName;
    private String str_PSCode;
    private String str_PntName;
    private String str_PntCode;
    private String str_Res_PSName;
    private String str_Res_PSCode;
    private String str_BluTHAdres;

    public String getStr_PSName() {
        return str_PSName;
    }

    public void setStr_PSName(String str_PSName) {
        this.str_PSName = str_PSName;
    }

    public String getStr_PSCode() {
        return str_PSCode;
    }

    public void setStr_PSCode(String str_PSCode) {
        this.str_PSCode = str_PSCode;
    }

    public String getStr_PntName() {
        return str_PntName;
    }

    public void setStr_PntName(String str_PntName) {
        this.str_PntName = str_PntName;
    }

    public String getStr_PntCode() {
        return str_PntCode;
    }

    public void setStr_PntCode(String str_PntCode) {
        this.str_PntCode = str_PntCode;
    }

    public String getStr_Res_PSName() {
        return str_Res_PSName;
    }

    public void setStr_Res_PSName(String str_Res_PSName) {
        this.str_Res_PSName = str_Res_PSName;
    }

    public String getStr_Res_PSCode() {
        return str_Res_PSCode;
    }

    public void setStr_Res_PSCode(String str_Res_PSCode) {
        this.str_Res_PSCode = str_Res_PSCode;
    }

    public String getStr_BluTHAdres() {
        return str_BluTHAdres;
    }

    public void setStr_BluTHAdres(String str_BluTHAdres) {
        this.str_BluTHAdres = str_BluTHAdres;
    }


}
