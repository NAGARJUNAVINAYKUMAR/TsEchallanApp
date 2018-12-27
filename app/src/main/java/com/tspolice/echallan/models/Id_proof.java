package com.tspolice.echallan.models;

import java.io.Serializable;

/**
 * Created by Srinivas on 11/20/2018.
 */

public class Id_proof implements Serializable {

    private String idProofCd;

    private String idProofDetails;

    public String getIdProofCd ()
    {
        return idProofCd;
    }

    public void setIdProofCd (String idProofCd)
    {
        this.idProofCd = idProofCd;
    }

    public String getIdProofDetails ()
    {
        return idProofDetails;
    }

    public void setIdProofDetails (String idProofDetails)
    {
        this.idProofDetails = idProofDetails;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [idProofCd = "+idProofCd+", idProofDetails = "+idProofDetails+"]";
    }
}
