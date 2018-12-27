package com.tspolice.echallan.models;

import java.io.Serializable;

/**
 * Created by Srinivas on 11/14/2018.
 */

public class MultiSelectModel implements Serializable{
    private int id;
    private Integer offence_cd;
    private String vltnSecName;
    private String vltnDis;
    private String vltnSec;
    private int fine_min;
    private int fine_max;
    private int penalty_points;
    private Boolean isSelected;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getOffence_cd() {
        return offence_cd;
    }

    public String getVltnSecName() {
        return vltnSecName;
    }

    public void setVltnSecName(String vltnSecName) {
        this.vltnSecName = vltnSecName;
    }

    public String getVltnDis() {
        return vltnDis;
    }

    public void setVltnDis(String vltnDis) {
        this.vltnDis = vltnDis;
    }

    public String getVltnSec() {
        return vltnSec;
    }

    public void setVltnSec(String vltnSec) {
        this.vltnSec = vltnSec;
    }

    public void setOffence_cd(Integer offence_cd) {
        this.offence_cd = offence_cd;
    }

    public int getFine_min() {
        return fine_min;
    }

    public void setFine_min(int fine_min) {
        this.fine_min = fine_min;
    }

    public int getFine_max() {
        return fine_max;
    }

    public void setFine_max(int fine_max) {
        this.fine_max = fine_max;
    }

    public int getPenalty_points() {
        return penalty_points;
    }

    public void setPenalty_points(int penalty_points) {
        this.penalty_points = penalty_points;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
