package com.tspolice.echallan.models;

/**
 * Created by Srinivas on 11/29/2018.
 */

public class DetaindModel {

    private String detainedItemCode;
    private String detainedItemName;
    private String detainedStatus;

    public String getDetainedItemCode() {
        return detainedItemCode;
    }

    public void setDetainedItemCode(String detainedItemCode) {
        this.detainedItemCode = detainedItemCode;
    }

    public String getDetainedItemName() {
        return detainedItemName;
    }

    public void setDetainedItemName(String detainedItemName) {
        this.detainedItemName = detainedItemName;
    }

    public String getDetainedStatus() {
        return detainedStatus;
    }

    public void setDetainedStatus(String detainedStatus) {
        this.detainedStatus = detainedStatus;
    }
}
