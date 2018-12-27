package com.tspolice.echallan.models;

import java.io.Serializable;

/**
 * Created by Srinivas on 11/19/2018.
 */

public class DLModel implements Serializable {
    private String ldcDlNO;
    private String ldc_L_NM;
    private String ldc_F_NM;
    private String ldc_PG_NM;
    private String ldc_PADDR2;
    private Integer traffic_POINTS;
    private String ldc_SEX;

    public String getLdcDlNO() {
        return ldcDlNO;
    }

    public void setLdcDlNO(String ldcDlNO) {
        this.ldcDlNO = ldcDlNO;
    }

    public String getLdc_L_NM() {
        return ldc_L_NM;
    }

    public void setLdc_L_NM(String ldc_L_NM) {
        this.ldc_L_NM = ldc_L_NM;
    }

    public String getLdc_F_NM() {
        return ldc_F_NM;
    }

    public void setLdc_F_NM(String ldc_F_NM) {
        this.ldc_F_NM = ldc_F_NM;
    }

    public String getLdc_PG_NM() {
        return ldc_PG_NM;
    }

    public void setLdc_PG_NM(String ldc_PG_NM) {
        this.ldc_PG_NM = ldc_PG_NM;
    }

    public String getLdc_PADDR2() {
        return ldc_PADDR2;
    }

    public void setLdc_PADDR2(String ldc_PADDR2) {
        this.ldc_PADDR2 = ldc_PADDR2;
    }

    public Integer getTraffic_POINTS() {
        return traffic_POINTS;
    }

    public void setTraffic_POINTS(Integer traffic_POINTS) {
        this.traffic_POINTS = traffic_POINTS;
    }

    public String getLdc_SEX() {
        return ldc_SEX;
    }

    public void setLdc_SEX(String ldc_SEX) {
        this.ldc_SEX = ldc_SEX;
    }
}
