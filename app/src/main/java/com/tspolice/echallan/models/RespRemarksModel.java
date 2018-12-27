package com.tspolice.echallan.models;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Srinivas on 10/8/2018.
 */

public class RespRemarksModel implements Serializable{

    private int state_cd;

    private int zone_CD;

    private int cadre_cd;

    private String contact_no;

    private int division_CD;

    private String zone_NAME;

    private String ps_name;

    private String empId;

    private String state_name;

    private int ps_cd;

    private String userID;

    private String pwd;

    private String emp_name;

    private int actvie_status;

    private String division_NAME;

    private String duties;

    private int unitcd;

    private int role_cd;

    private String unit_name;

    private String email_id;

    private String cadre_sf;

    private String unit_sf;

    private int dept_cd;

    public RespRemarksModel() {

    }


    public static RespRemarksModel fromJson(String json) {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .create().fromJson(json, RespRemarksModel.class);
    }

    public int getState_cd() {
        return state_cd;
    }

    public void setState_cd(int state_cd) {
        this.state_cd = state_cd;
    }

    public int getZone_CD() {
        return zone_CD;
    }

    public void setZone_CD(int zone_CD) {
        this.zone_CD = zone_CD;
    }

    public int getCadre_cd() {
        return cadre_cd;
    }

    public void setCadre_cd(int cadre_cd) {
        this.cadre_cd = cadre_cd;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public int getDivision_CD() {
        return division_CD;
    }

    public void setDivision_CD(int division_CD) {
        this.division_CD = division_CD;
    }

    public String getZone_NAME() {
        return zone_NAME;
    }

    public void setZone_NAME(String zone_NAME) {
        this.zone_NAME = zone_NAME;
    }

    public String getPs_name() {
        return ps_name;
    }

    public void setPs_name(String ps_name) {
        this.ps_name = ps_name;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public int getPs_cd() {
        return ps_cd;
    }

    public void setPs_cd(int ps_cd) {
        this.ps_cd = ps_cd;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public int getActvie_status() {
        return actvie_status;
    }

    public void setActvie_status(int actvie_status) {
        this.actvie_status = actvie_status;
    }

    public String getDivision_NAME() {
        return division_NAME;
    }

    public void setDivision_NAME(String division_NAME) {
        this.division_NAME = division_NAME;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public int getUnitcd() {
        return unitcd;
    }

    public void setUnitcd(int unitcd) {
        this.unitcd = unitcd;
    }

    public int getRole_cd() {
        return role_cd;
    }

    public void setRole_cd(int role_cd) {
        this.role_cd = role_cd;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getCadre_sf() {
        return cadre_sf;
    }

    public void setCadre_sf(String cadre_sf) {
        this.cadre_sf = cadre_sf;
    }

    public String getUnit_sf() {
        return unit_sf;
    }

    public void setUnit_sf(String unit_sf) {
        this.unit_sf = unit_sf;
    }

    public int getDept_cd() {
        return dept_cd;
    }

    public void setDept_cd(int dept_cd) {
        this.dept_cd = dept_cd;
    }
}
