package com.tspolice.echallan.models;

import java.io.Serializable;

/**
 * Created by Srinivas on 10/8/2018.
 */

public class LoginResModel implements Serializable{

    private String respCode;

    private RespRemarksModel respRemark;

    private String respDesc;

    public String getRespCode ()
    {
        return respCode;
    }

    public void setRespCode (String respCode)
    {
        this.respCode = respCode;
    }

    public RespRemarksModel getRespRemark() {
        return respRemark;
    }

    public void setRespRemark(RespRemarksModel respRemark) {
        this.respRemark = respRemark;
    }

    public String getRespDesc ()
    {
        return respDesc;
    }

    public void setRespDesc (String respDesc)
    {
        this.respDesc = respDesc;
    }



    @Override
    public String toString()
    {
        return "ClassPojo [respCode = "+respCode+", respRemark = "+respRemark+", respDesc = "+respDesc+"]";
    }
}
