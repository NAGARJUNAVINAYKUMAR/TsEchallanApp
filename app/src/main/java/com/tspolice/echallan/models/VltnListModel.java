package com.tspolice.echallan.models;

import java.io.Serializable;

/**
 * Created by Srinivas on 11/30/2018.
 */

public class VltnListModel implements Serializable{


    private Integer offence_cd;
    private String offence_desc;
    private String section;
    private int fine_min;
    private int fine_max;
    private int penalty_points;

    public Integer getOffence_cd() {
        return offence_cd;
    }

    public String getVltnDis() {
        return offence_desc;
    }

    public void setVltnDis(String vltnDis) {
        this.offence_desc = vltnDis;
    }

    public String getVltnSec() {
        return section;
    }

    public void setVltnSec(String vltnSec) {
        this.section = vltnSec;
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
}

