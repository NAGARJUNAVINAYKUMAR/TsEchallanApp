package com.tspolice.echallan.configs;

/**
 * Created by Srinivas on 10/4/2018.
 */

public class AppConfig {
    private static final String c_URL="http://61.95.168.181:8080/echallan/";

    public static final String cadreMaster_URL=c_URL+"cadreMaster";

    public static final String unitMaster_URL=c_URL+"unitMaster?stateCode=";

    public static final String psNamesBY_unitMaster_URL=c_URL+"psMasterbyUnit?unitCode=";

    public static final String pntNamesBY_PSMaster_URL=c_URL+"pointsMaster?psCode=";

    public static final String user_Register_URL=c_URL+"userRegistration";

    public static final String user_OTP_URL=c_URL+"sendOTP?mobileNo=";

    public static final String user_Login_URL=c_URL+"officerDetails";

    public static final String sectionMasterByWheeler_URL=c_URL+"sectionMasterByWheeler?wheelerCd=";

    public static final String getwheelerMaster=c_URL+"wheelerMaster";

    public static final String getrc_DLMaster_URL=c_URL+"rta/rcdl?regnNo=";

    public static final String wheelerMasterByVClassId_URL=c_URL+"wheelerMasterByVClassId?VClassId=";

    public static final String detainRules=c_URL+"service/detainRules?unitCode=";

    public static final String chargeSheetRules_URL=c_URL+"service/chargeSheetRules?unitCode=";

    public static final String spotChallanGen_URL=c_URL+"spotChallanGen";



}
