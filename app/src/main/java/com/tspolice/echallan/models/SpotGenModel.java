package com.tspolice.echallan.models;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Srinivas on 11/20/2018.
 */

public class SpotGenModel implements Serializable{

    private String gps_long;

    private ArrayList<DetaindModel> dt_items;

    private String dept_cd;

    private String gps_lat;

    private String penalty_points;

    private String occupation_cd;

    private String offence_dt;

    private String enforcementType;

    private String responsible_ps_cd;

    private ArrayList<VltnListModel> violations;

    private String imageStatus;

    private String remark_cd;

    private String bacSerialNo;

    public String getBacSerialNo() {
        return bacSerialNo;
    }

    public void setBacSerialNo(String bacSerialNo) {
        this.bacSerialNo = bacSerialNo;
    }

    private String ps_juris_cd;

    private String unit_cd;

    private String name;

    private String point_cd;

    private String age;

    private String gender;

    private String unitShortForm;

    private String challan_type;

    private String pid_cd;

    private String regn_no;

    private String challan_base_cd;

    private String chargeSheetStatus;

    private String contact_no;

    private String education_cd;

    private String stateCode;

    private String paymentStatus;

    private String address;

    private String unitName;

    private String dob;

    private ArrayList<Id_proof> id_proof;

    private String parent_name;

    private String baclevel;

    private String gatewayCode;

    private String d_of_pay;

    private String paid_amt;

    private String wheeler_cd;

    public String getGps_long ()
    {
        return gps_long;
    }

    public void setGps_long (String gps_long)
    {
        this.gps_long = gps_long;
    }

    public ArrayList<DetaindModel> getDt_items ()
    {
        return dt_items;
    }

    public void setDt_items (ArrayList<DetaindModel> dt_items)
    {
        this.dt_items = dt_items;
    }

    public String getDept_cd ()
    {
        return dept_cd;
    }

    public void setDept_cd (String dept_cd)
    {
        this.dept_cd = dept_cd;
    }

    public String getGps_lat ()
    {
        return gps_lat;
    }

    public void setGps_lat (String gps_lat)
    {
        this.gps_lat = gps_lat;
    }

    public String getPenalty_points ()
    {
        return penalty_points;
    }

    public void setPenalty_points (String penalty_points)
    {
        this.penalty_points = penalty_points;
    }

    public String getOccupation_cd ()
    {
        return occupation_cd;
    }

    public void setOccupation_cd (String occupation_cd)
    {
        this.occupation_cd = occupation_cd;
    }

    public String getOffence_dt ()
    {
        return offence_dt;
    }

    public void setOffence_dt (String offence_dt)
    {
        this.offence_dt = offence_dt;
    }

    public String getEnforcementType ()
    {
        return enforcementType;
    }

    public void setEnforcementType (String enforcementType)
    {
        this.enforcementType = enforcementType;
    }

    public String getResponsible_ps_cd ()
    {
        return responsible_ps_cd;
    }

    public void setResponsible_ps_cd (String responsible_ps_cd)
    {
        this.responsible_ps_cd = responsible_ps_cd;
    }

    public ArrayList<VltnListModel> getViolations() {
        return violations;
    }

    public void setViolations(ArrayList<VltnListModel> violations) {
        this.violations = violations;
    }

    public String getImageStatus ()
    {
        return imageStatus;
    }

    public void setImageStatus (String imageStatus)
    {
        this.imageStatus = imageStatus;
    }

    public String getRemark_cd ()
    {
        return remark_cd;
    }

    public void setRemark_cd (String remark_cd)
    {
        this.remark_cd = remark_cd;
    }

    public String getPs_juris_cd ()
    {
        return ps_juris_cd;
    }

    public void setPs_juris_cd (String ps_juris_cd)
    {
        this.ps_juris_cd = ps_juris_cd;
    }

    public String getUnit_cd ()
    {
        return unit_cd;
    }

    public void setUnit_cd (String unit_cd)
    {
        this.unit_cd = unit_cd;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getPoint_cd ()
    {
        return point_cd;
    }

    public void setPoint_cd (String point_cd)
    {
        this.point_cd = point_cd;
    }

    public String getAge ()
    {
        return age;
    }

    public void setAge (String age)
    {
        this.age = age;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getUnitShortForm ()
    {
        return unitShortForm;
    }

    public void setUnitShortForm (String unitShortForm)
    {
        this.unitShortForm = unitShortForm;
    }

    public String getChallan_type ()
    {
        return challan_type;
    }

    public void setChallan_type (String challan_type)
    {
        this.challan_type = challan_type;
    }

    public String getPid_cd ()
    {
        return pid_cd;
    }

    public void setPid_cd (String pid_cd)
    {
        this.pid_cd = pid_cd;
    }

    public String getRegn_no ()
    {
        return regn_no;
    }

    public void setRegn_no (String regn_no)
    {
        this.regn_no = regn_no;
    }

    public String getChallan_base_cd ()
    {
        return challan_base_cd;
    }

    public void setChallan_base_cd (String challan_base_cd)
    {
        this.challan_base_cd = challan_base_cd;
    }

    public String getChargeSheetStatus ()
    {
        return chargeSheetStatus;
    }

    public void setChargeSheetStatus (String chargeSheetStatus)
    {
        this.chargeSheetStatus = chargeSheetStatus;
    }

    public String getContact_no ()
    {
        return contact_no;
    }

    public void setContact_no (String contact_no)
    {
        this.contact_no = contact_no;
    }

    public String getEducation_cd ()
    {
        return education_cd;
    }

    public void setEducation_cd (String education_cd)
    {
        this.education_cd = education_cd;
    }

    public String getStateCode ()
    {
        return stateCode;
    }

    public void setStateCode (String stateCode)
    {
        this.stateCode = stateCode;
    }

    public String getPaymentStatus ()
    {
        return paymentStatus;
    }

    public void setPaymentStatus (String paymentStatus)
    {
        this.paymentStatus = paymentStatus;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getUnitName ()
    {
        return unitName;
    }

    public void setUnitName (String unitName)
    {
        this.unitName = unitName;
    }

    public String getDob ()
    {
        return dob;
    }

    public void setDob (String dob)
    {
        this.dob = dob;
    }

    public ArrayList<Id_proof> getId_proof ()
    {
        return id_proof;
    }

    public void setId_proof (ArrayList<Id_proof> id_proof)
    {
        this.id_proof = id_proof;
    }

    public String getParent_name ()
    {
        return parent_name;
    }

    public void setParent_name (String parent_name)
    {
        this.parent_name = parent_name;
    }

    public String getBaclevel ()
    {
        return baclevel;
    }

    public void setBaclevel (String baclevel)
    {
        this.baclevel = baclevel;
    }

    public String getGatewayCode ()
    {
        return gatewayCode;
    }

    public void setGatewayCode (String gatewayCode)
    {
        this.gatewayCode = gatewayCode;
    }

    public String getD_of_pay ()
    {
        return d_of_pay;
    }

    public void setD_of_pay (String d_of_pay)
    {
        this.d_of_pay = d_of_pay;
    }

    public String getPaid_amt ()
    {
        return paid_amt;
    }

    public void setPaid_amt (String paid_amt)
    {
        this.paid_amt = paid_amt;
    }

    public String getWheeler_cd() {
        return wheeler_cd;
    }

    public void setWheeler_cd(String wheeler_cd) {
        this.wheeler_cd = wheeler_cd;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [gps_long = "+gps_long+", dt_items = "+dt_items+", dept_cd = "+dept_cd+", gps_lat = "+gps_lat+", penalty_points = "+penalty_points+", occupation_cd = "+occupation_cd+", offence_dt = "+offence_dt+", enforcementType = "+enforcementType+", responsible_ps_cd = "+responsible_ps_cd+", violations = "+violations+", imageStatus = "+imageStatus+", remark_cd = "+remark_cd+", ps_juris_cd = "+ps_juris_cd+", unit_cd = "+unit_cd+", name = "+name+", point_cd = "+point_cd+", age = "+age+", gender = "+gender+", unitShortForm = "+unitShortForm+", challan_type = "+challan_type+", pid_cd = "+pid_cd+", regn_no = "+regn_no+", challan_base_cd = "+challan_base_cd+", chargeSheetStatus = "+chargeSheetStatus+", contact_no = "+contact_no+", education_cd = "+education_cd+", stateCode = "+stateCode+", paymentStatus = "+paymentStatus+", address = "+address+", unitName = "+unitName+", dob = "+dob+", id_proof = "+id_proof+", parent_name = "+parent_name+", baclevel = "+baclevel+", gatewayCode = "+gatewayCode+", d_of_pay = "+d_of_pay+", paid_amt = "+paid_amt+"]";
    }
}
