package com.tspolice.echallan.models;

import java.io.Serializable;

/**
 * Created by Srinivas on 11/19/2018.
 */

public class RCModel implements Serializable{
    private String regnNo;
    private String o_name;
    private String address;
    private String veh_type;
    private String chas_no;
    private String eng_no;
    private String insurance_validity;
    private String vclass_id;


    public String getRegnNo() {
        return regnNo;
    }

    public void setRegnNo(String regnNo) {
        this.regnNo = regnNo;
    }

    public String getO_name() {
        return o_name;
    }

    public void setO_name(String o_name) {
        this.o_name = o_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVeh_type() {
        return veh_type;
    }

    public void setVeh_type(String veh_type) {
        this.veh_type = veh_type;
    }

    public String getChas_no() {
        return chas_no;
    }

    public void setChas_no(String chas_no) {
        this.chas_no = chas_no;
    }

    public String getEng_no() {
        return eng_no;
    }

    public void setEng_no(String eng_no) {
        this.eng_no = eng_no;
    }

    public String getInsurance_validity() {
        return insurance_validity;
    }

    public void setInsurance_validity(String insurance_validity) {
        this.insurance_validity = insurance_validity;
    }

    public String getVclass_id() {
        return vclass_id;
    }

    public void setVclass_id(String vclass_id) {
        this.vclass_id = vclass_id;
    }


}
