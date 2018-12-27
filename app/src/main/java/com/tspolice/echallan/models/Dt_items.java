package com.tspolice.echallan.models;

import java.io.Serializable;

/**
 * Created by Srinivas on 11/20/2018.
 */

public class Dt_items implements Serializable{
    private String detainedItemName;

    private String detainedStatus;

    private String detainedItemCode;

    public String getDetainedItemName ()
    {
        return detainedItemName;
    }

    public void setDetainedItemName (String detainedItemName)
    {
        this.detainedItemName = detainedItemName;
    }

    public String getDetainedStatus ()
    {
        return detainedStatus;
    }

    public void setDetainedStatus (String detainedStatus)
    {
        this.detainedStatus = detainedStatus;
    }

    public String getDetainedItemCode ()
    {
        return detainedItemCode;
    }

    public void setDetainedItemCode (String detainedItemCode)
    {
        this.detainedItemCode = detainedItemCode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [detainedItemName = "+detainedItemName+", detainedStatus = "+detainedStatus+", detainedItemCode = "+detainedItemCode+"]";
    }
}
