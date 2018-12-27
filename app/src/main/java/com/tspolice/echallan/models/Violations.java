package com.tspolice.echallan.models;

import java.io.Serializable;

/**
 * Created by Srinivas on 11/20/2018.
 */

public class Violations implements Serializable {

    private String fine_min;

    private String fine_max;

    private String penalty_points;

    private String offence_desc;

    private String offence_cd;

    private String section;

    public String getFine_min ()
    {
        return fine_min;
    }

    public void setFine_min (String fine_min)
    {
        this.fine_min = fine_min;
    }

    public String getFine_max ()
    {
        return fine_max;
    }

    public void setFine_max (String fine_max)
    {
        this.fine_max = fine_max;
    }

    public String getPenalty_points ()
    {
        return penalty_points;
    }

    public void setPenalty_points (String penalty_points)
    {
        this.penalty_points = penalty_points;
    }

    public String getOffence_desc ()
    {
        return offence_desc;
    }

    public void setOffence_desc (String offence_desc)
    {
        this.offence_desc = offence_desc;
    }

    public String getOffence_cd ()
    {
        return offence_cd;
    }

    public void setOffence_cd (String offence_cd)
    {
        this.offence_cd = offence_cd;
    }

    public String getSection ()
    {
        return section;
    }

    public void setSection (String section)
    {
        this.section = section;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [fine_min = "+fine_min+", fine_max = "+fine_max+", penalty_points = "+penalty_points+", offence_desc = "+offence_desc+", offence_cd = "+offence_cd+", section = "+section+"]";
    }
}
