package com.tspolice.echallan.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;
import com.tspolice.echallan.BuildConfig;
import com.tspolice.echallan.R;
import com.tspolice.echallan.configs.AppConfig;
import com.tspolice.echallan.models.DLModel;
import com.tspolice.echallan.models.Id_proof;
import com.tspolice.echallan.models.LoginResModel;
import com.tspolice.echallan.models.MultiSelectModel;
import com.tspolice.echallan.models.RCModel;
import com.tspolice.echallan.models.SettingsModel;
import com.tspolice.echallan.models.SpotGenModel;
import com.tspolice.echallan.models.VltnListModel;
import com.tspolice.echallan.utils.ConnectivityUtils;
import com.tspolice.echallan.utils.DateUtil;
import com.tspolice.echallan.utils.SharedPrefsHelper;
import com.tspolice.echallan.utils.SingleMediaScanner;
import com.tspolice.echallan.utils.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;

public class DrunkDriveActivity extends AppCompatActivity implements
        RadioGroup.OnCheckedChangeListener,
        View.OnClickListener,
        DatePickerFragmentDialog.OnDateSetListener,
        LocationListener {

    ScrollView srolView_Spot;

    RadioGroup radio_GrpSpot, radio_GrpGender;

    RadioButton rBtn_RegNo, rBtn_TRNo, rBtn_ChasNo, rBtn_EngNo, rBtn_Male, rBtn_Female, rBtn_Other;

    AppCompatTextView txt_Reg_Chas;
    //RC Details
    AppCompatTextView txt_RCDtlsFnd, txt_RCRegChasEngNo, txt_RCOwnrName, txt_RcAddress, txt_VehcileType, txt_RCChasisNo, txt_EngineNo;
    //DL Details
    AppCompatTextView txt_DLDtlsFnd, txt_DLNoHdng, txt_DLName, txt_DLFName, txt_DLAddress, txt_DLPntNo;
    AppCompatTextView txt_PndgChalns, txt_PndgAmnt, txt_TotalFineAmnt, txt_sltdVltnAmnt;

    AppCompatEditText edt_Txt_RegChasEngNo, edt_Txt_DLNo, edt_Txt_DLDOB, edt_Txt_DRName, edt_Txt_DRFName, edt_Txt_DRADRRESS,
            edt_Txt_DRAge, edt_Txt_DRGender;

    AppCompatImageView img_DLDOB, img_DvrCamera, img_DvrGallery, img_SpotCaptured;

    AppCompatButton btn_GetDetails, btn_Spinr_WlrCode, btn_Spinr_Viltn, btn_DDNext;

    LinearLayout lyt_RCCardInfo, lyt_DLCardInfo, lyt_DL_DOB, lyt_vltnsList, lyt_GetInfo, lyt_DriverInfo;

    DateUtil dateUtil;

    RequestQueue requestQueue;

    ProgressDialog progressDialog;

    SpotGenModel ddGenModel;
    LoginResModel loginResModel;
    SettingsModel settingsModel;
    RCModel rcModel;
    DLModel dlModel;
    MultiSelectModel mselectModel;

    public String str_UtCode, str_WlrCode = "", str_RegChaEngNo = "", str_DLNo = "", str_DLDOB = "";
    public String str_UtName, str_UtShtForm, str_StateCode, str_DptCode, str_PSCode, str_ResPsCode, str_PntCode, str_PidCode, str_PaidAmnt,
            str_Name = "", str_PName = "", str_Address = "";
    public String chargeSheetStatus = "0", dlStatus = "0", slctdwithoutDL = "0", imageStatus = "0",
            challan_base_cd = "1", challan_type = "23", enforcementType = "CONTACT", paymentStatus = "U", str_Date = "", str_GtWayCode = "",
            str_DateOfPay = "", str_Gender = "1", str_Age = "";

    public int selctdVltnsfineAmnt = 0, pending_FineAmnt = 0, total_FineAmnt = 0, pending_challans = 0, penalty_points = 0, selctdPnltyPnts = 0,
            total_PnltyPnts = 0;
    public static int spot_OTP = 0, spot_OTP_Time = 0;
    public static String detainedStatus = "0";

    ArrayList<MultiSelectModel> mArrayList_DetainRules = new ArrayList<>();
    ArrayList<String> mArrayList_Wheelers = new ArrayList<>();
    ArrayList<Id_proof> id_proofs = new ArrayList<>();
    HashMap<String, String> params_Wheelers = new HashMap<>();
    ArrayList<VltnListModel> vltnListModels = new ArrayList<>();

    private SpinnerDialog spinner_WheelerDialog;

    Calendar calendar;

    DatePickerFragmentDialog datePickerDialog;

    public double latitude = 0.0, longitude = 0.0;

    public static Bitmap bitmapCaptredImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drunk_drive);

        initViews();

        dateUtil = new DateUtil();
        str_Date = dateUtil.getDate();

        requestQueue = Volley.newRequestQueue(this);

        progressDialog = new ProgressDialog(this);

        ddGenModel = new SpotGenModel();

        lyt_GetInfo.setVisibility(View.GONE);

        ConnectivityUtils.getLocation(this);

        getSharedPreferences();

        radio_GrpSpot.setOnCheckedChangeListener(DrunkDriveActivity.this);
        radio_GrpGender.setOnCheckedChangeListener(DrunkDriveActivity.this);

        img_DLDOB.setOnClickListener(DrunkDriveActivity.this);
        btn_GetDetails.setOnClickListener(DrunkDriveActivity.this);
        img_DvrCamera.setOnClickListener(DrunkDriveActivity.this);
        btn_DDNext.setOnClickListener(DrunkDriveActivity.this);
        btn_Spinr_WlrCode.setOnClickListener(DrunkDriveActivity.this);

        edt_Txt_RegChasEngNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lyt_GetInfo.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_Txt_DLNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lyt_GetInfo.setVisibility(View.GONE);
                if (count >= 5) {
                    lyt_DL_DOB.setVisibility(View.VISIBLE);
                } else {
                    lyt_DL_DOB.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                edt_Txt_DLDOB.setError(null);
            }
        });
    }

    private void initViews() {
        srolView_Spot = findViewById(R.id.srolView_Spot);
        radio_GrpSpot = findViewById(R.id.radio_GrpSpot);
        rBtn_RegNo = findViewById(R.id.rBtn_RegNo);
        rBtn_TRNo = findViewById(R.id.rBtn_TRNo);
        rBtn_ChasNo = findViewById(R.id.rBtn_ChasNo);
        rBtn_EngNo = findViewById(R.id.rBtn_EngNo);

        txt_Reg_Chas = findViewById(R.id.txt_Reg_Chas);

        edt_Txt_RegChasEngNo = findViewById(R.id.edt_Txt_RegChasEngNo);
        edt_Txt_DLNo = findViewById(R.id.edt_Txt_DLNo);
        edt_Txt_DLDOB = findViewById(R.id.edt_Txt_DLDOB);

        img_DLDOB = findViewById(R.id.img_DLDOB);

        btn_GetDetails = findViewById(R.id.btn_GetDetails);

        lyt_GetInfo = findViewById(R.id.lyt_GetInfo);

        txt_RCDtlsFnd = findViewById(R.id.txt_RCDtlsFnd);
        lyt_RCCardInfo = findViewById(R.id.lyt_RCCardInfo);
        txt_RCRegChasEngNo = findViewById(R.id.txt_RCRegChasEngNo);
        txt_RCOwnrName = findViewById(R.id.txt_RCOwnrName);
        txt_RcAddress = findViewById(R.id.txt_RcAddress);
        txt_VehcileType = findViewById(R.id.txt_VehcileType);
        txt_RCChasisNo = findViewById(R.id.txt_RCChasisNo);
        txt_EngineNo = findViewById(R.id.txt_EngineNo);

        txt_DLDtlsFnd = findViewById(R.id.txt_DLDtlsFnd);
        lyt_DLCardInfo = findViewById(R.id.lyt_DLCardInfo);
        lyt_DL_DOB = findViewById(R.id.lyt_DL_DOB);
        txt_DLNoHdng = findViewById(R.id.txt_DLNoHdng);
        txt_DLName = findViewById(R.id.txt_DLName);
        txt_DLFName = findViewById(R.id.txt_DLFName);
        txt_DLAddress = findViewById(R.id.txt_DLAddress);
        txt_DLPntNo = findViewById(R.id.txt_DLPntNo);

        lyt_DriverInfo = findViewById(R.id.lyt_DriverInfo);
        edt_Txt_DRName = findViewById(R.id.edt_Txt_DRName);
        edt_Txt_DRFName = findViewById(R.id.edt_Txt_DRFName);
        edt_Txt_DRADRRESS = findViewById(R.id.edt_Txt_DRADRRESS);
        edt_Txt_DRAge = findViewById(R.id.edt_Txt_DRAge);

        radio_GrpGender = findViewById(R.id.radio_GrpGender);
        rBtn_Male = findViewById(R.id.rBtn_Male);
        rBtn_Female = findViewById(R.id.rBtn_Female);
        rBtn_Other = findViewById(R.id.rBtn_Other);

        txt_PndgChalns = findViewById(R.id.txt_PndgChalns);
        txt_PndgAmnt = findViewById(R.id.txt_PendingAmnt);
        txt_sltdVltnAmnt = findViewById(R.id.txt_sltdVltnAmnt);
        txt_TotalFineAmnt = findViewById(R.id.txt_TotalFineAmnt);

        btn_Spinr_WlrCode = findViewById(R.id.btn_Spinr_WlrCode);

        img_DvrCamera = findViewById(R.id.img_DvrCamera);
        img_DvrGallery = findViewById(R.id.img_DvrGallery);
        img_SpotCaptured = findViewById(R.id.img_SpotCaptured);

        btn_DDNext = findViewById(R.id.btn_DDNext);
    }

    private void getSharedPreferences() {
        loginResModel = SharedPrefsHelper.getSavedObjectFromPreference(getApplicationContext(),
                "mPreference", "mLoginRes", LoginResModel.class);
        if (loginResModel != null) {
            str_UtCode = String.valueOf(loginResModel.getRespRemark().getUnitcd());
            str_UtName = loginResModel.getRespRemark().getUnit_name();
            str_UtShtForm = loginResModel.getRespRemark().getUnit_sf();
            str_StateCode = String.valueOf(loginResModel.getRespRemark().getState_cd());
            str_DptCode = String.valueOf(loginResModel.getRespRemark().getDept_cd());
            str_PidCode = String.valueOf(loginResModel.getRespRemark().getUserID());
        }

        settingsModel = SharedPrefsHelper.getSettinsFromPref(getApplicationContext(),
                "mPreference", "mSettingsInfo", SettingsModel.class);
        if (settingsModel != null) {
            str_PSCode = settingsModel.getStr_PSCode();
            str_ResPsCode = settingsModel.getStr_Res_PSCode();
            str_PntCode = settingsModel.getStr_PntCode();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rBtn_RegNo:
                txt_Reg_Chas.setText(getResources().getString(R.string.txt_RegNo));
                challan_base_cd = "1";
                break;
            case R.id.rBtn_TRNo:
                txt_Reg_Chas.setText(getResources().getString(R.string.txt_TrNo));
                challan_base_cd = "2";
                break;
            case R.id.rBtn_ChasNo:
                txt_Reg_Chas.setText(getResources().getString(R.string.txt_ChasisNo));
                challan_base_cd = "3";
                break;
            case R.id.rBtn_EngNo:
                txt_Reg_Chas.setText(getResources().getString(R.string.txt_engNo));
                challan_base_cd = "4";
                break;
            case R.id.rBtn_Male:
                str_Gender = "1";
                break;
            case R.id.rBtn_Female:
                str_Gender = "2";
                break;
            case R.id.rBtn_Other:
                str_Gender = "3";
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_DLDOB:
                calendar = Calendar.getInstance();
                datePickerDialog = new DatePickerFragmentDialog();
                datePickerDialog = DatePickerFragmentDialog.newInstance(DrunkDriveActivity.this,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show(getSupportFragmentManager(), "DatePicker");
                break;
            case R.id.btn_Spinr_WlrCode:
                getwheelerMaster();
                break;
            case R.id.btn_GetDetails:

                edt_Txt_DRName.setText("");
                edt_Txt_DRFName.setText("");
                edt_Txt_DRADRRESS.setText("");
                edt_Txt_DRAge.setText("");
                img_SpotCaptured.setVisibility(View.GONE);
                ddGenModel=new SpotGenModel();
                if (edt_Txt_RegChasEngNo.getText().toString().isEmpty()) {
                    edt_Txt_RegChasEngNo.setError("Please Enter Registration Number !");
                    edt_Txt_RegChasEngNo.requestFocus();
                } else if (!edt_Txt_DLNo.getText().toString().isEmpty()
                        && edt_Txt_DLNo.getText().toString().length() < 5) {
                    edt_Txt_DLNo.requestFocus();
                    edt_Txt_DLNo.setError("Enter Valid Licence Number !");
                } else if (!edt_Txt_DLNo.getText().toString().isEmpty()
                        && edt_Txt_DLDOB.getText().toString().isEmpty()) {
                    edt_Txt_DLDOB.setError("Enter DOB of DL !");
                    edt_Txt_DLDOB.requestFocus();
                } else {
                    if (edt_Txt_DLNo.getText().toString().isEmpty()
                            || edt_Txt_DLNo.getText().toString().length() < 5) {
                        dlStatus = "1"; // Prompt to Without Dl Violation
                    } else {
                        dlStatus = "0";
                    }
                    str_RegChaEngNo = edt_Txt_RegChasEngNo.getText().toString();
                    str_DLNo = edt_Txt_DLNo.getText().toString();
                    str_DLDOB = edt_Txt_DLDOB.getText().toString();
                    lyt_GetInfo.setVisibility(View.GONE);
                    rc_DL_Details(str_RegChaEngNo, str_DLNo, str_DLDOB, challan_base_cd);
                }
                break;
            case R.id.img_DvrCamera:
                if (ConnectivityUtils.locationServicesEnabled(DrunkDriveActivity.this)) {
                    try {
                        int locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
                        if (locationMode == LOCATION_MODE_HIGH_ACCURACY) {
                            latitude = ConnectivityUtils.latitude;
                            longitude = ConnectivityUtils.longitude;
                            captureImagefrom_Camera();
                            //Toast.makeText(getApplicationContext(), "Enable" + ConnectivityUtils.latitude + "Enable" + ConnectivityUtils.longitude, Toast.LENGTH_LONG).show();
                            Toaster.showInfoMessage("Enable " + ConnectivityUtils.latitude + "Enable" + ConnectivityUtils.longitude);
                        } else {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    ConnectivityUtils.locationSettings(DrunkDriveActivity.this);
                }
                break;
            case R.id.btn_DDNext:
                if (edt_Txt_DRName.getText().toString().isEmpty() && edt_Txt_DRName.isShown()) {
                    edt_Txt_DRName.setError("Please Enter Driver Name !");
                    edt_Txt_DRName.requestFocus();
                } else if (edt_Txt_DRAge.getText().toString().isEmpty() && edt_Txt_DRAge.isShown()) {
                    edt_Txt_DRAge.setError("Please Enter Driver Age !");
                    edt_Txt_DRAge.requestFocus();
                } else if (str_WlrCode.equals("") && btn_Spinr_WlrCode.getText().toString().equalsIgnoreCase(getResources().getString(R.string.txt_WelerCode))) {
                    Toaster.showWarningMessage("Please Select Vehicle Type !");
                } else if (imageStatus.equals("0")) {
                    Toaster.showWarningMessage("Please Capture the Image !");
                } else {
                    try {
                        str_Name = str_Name.equals("") ? edt_Txt_DRName.getText().toString() : str_Name;
                        str_PName = str_PName.equals("") ? edt_Txt_DRFName.getText().toString() : str_PName;
                        str_Address = str_Address.equals("") ? edt_Txt_DRADRRESS.getText().toString() : str_Address;
                        str_Age = str_Age.equals("") ? edt_Txt_DRAge.getText().toString() : str_Age;
                        Id_proof id_proof = new Id_proof();
                        id_proofs = new ArrayList<>();
                        if (!str_DLNo.equals("") && !str_DLDOB.equals("")) {
                            id_proof.setIdProofCd("2");
                            id_proof.setIdProofDetails(str_DLNo);
                            id_proofs.add(id_proof);
                        } else {
                            id_proof.setIdProofCd("0");
                            id_proof.setIdProofDetails("");
                            id_proofs.add(id_proof);
                        }
                        ddGenModel.setId_proof(id_proofs);
                        ddGenModel.setDob(str_DLDOB);
                        ddGenModel.setGender(str_Gender);
                        ddGenModel.setAge(str_Age);
                        ddGenModel.setUnit_cd(str_UtCode);
                        ddGenModel.setUnitName(str_UtName);
                        ddGenModel.setUnitShortForm(str_UtShtForm);
                        ddGenModel.setStateCode(str_StateCode);
                        ddGenModel.setChallan_type(challan_type);
                        ddGenModel.setEnforcementType(enforcementType);
                        ddGenModel.setChargeSheetStatus(chargeSheetStatus);
                        ddGenModel.setDept_cd(str_DptCode);
                        ddGenModel.setPs_juris_cd(str_PSCode);
                        ddGenModel.setResponsible_ps_cd(str_ResPsCode);
                        ddGenModel.setPoint_cd(str_PntCode);
                        ddGenModel.setRegn_no(str_RegChaEngNo);
                        ddGenModel.setOffence_dt(str_Date);
                        ddGenModel.setPid_cd(str_PidCode);
                        ddGenModel.setImageStatus(imageStatus);
                        ddGenModel.setChallan_base_cd(challan_base_cd);
                        ddGenModel.setName(str_Name);
                        ddGenModel.setParent_name(str_PName);
                        ddGenModel.setAddress(str_Address);
                        ddGenModel.setPenalty_points(String.valueOf(total_PnltyPnts));
                        ddGenModel.setGps_lat(String.valueOf(latitude));
                        ddGenModel.setGps_long(String.valueOf(longitude));
                        ddGenModel.setViolations(vltnListModels);
                        Intent intent = new Intent(DrunkDriveActivity.this, DDSecondActivity.class);
                        intent.putExtra("ddObj", ddGenModel);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void rc_DL_Details(final String regChaEngNo, final String dLNo, final String dlDOB, final String chalnCd) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String url = AppConfig.getrc_DLMaster_URL + regChaEngNo + "&dlNo=" + dLNo + "&dlDOB=" + dlDOB + "&challanBaseCd="
                + chalnCd + "&unitCode=" + str_UtCode + "&challanType=" + challan_type;

        progressDialog.setMessage("Loading");
        progressDialog.show();

        lyt_GetInfo.setVisibility(View.VISIBLE);

        StringRequest rc_DL_Details_Req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject_RCDLInfo = new JSONObject(response);
                    int rcRespCode = jsonObject_RCDLInfo.getInt("rcRespCode");
                    if (rcRespCode == 1) {
                        try {
                            lyt_RCCardInfo.setVisibility(View.VISIBLE);
                            txt_RCDtlsFnd.setText("RC Details");
                            JSONObject object_RC = jsonObject_RCDLInfo.getJSONObject("rcentity");
                            rcModel = new RCModel();
                            rcModel.setRegnNo(object_RC.getString("regnNo") != null ? object_RC.getString("regnNo") : "");
                            rcModel.setO_name(object_RC.getString("o_name") != null ? object_RC.getString("o_name") : "");
                            rcModel.setAddress(object_RC.getString("address") != null ? object_RC.getString("address") : "");
                            rcModel.setVeh_type(object_RC.getString("veh_type") != null ? object_RC.getString("veh_type") : "");
                            rcModel.setChas_no(object_RC.getString("chasNo") != null ? object_RC.getString("chasNo") : "");
                            rcModel.setEng_no(object_RC.getString("engNo") != null ? object_RC.getString("engNo") : "");
                            rcModel.setVclass_id(object_RC.getString("vclass_id") != null ? object_RC.getString("vclass_id") : "");
                            txt_RCRegChasEngNo.setText(rcModel.getRegnNo());
                            txt_RCOwnrName.setText(rcModel.getO_name());
                            txt_RcAddress.setText(rcModel.getAddress());
                            txt_VehcileType.setText(rcModel.getVeh_type());
                            txt_RCChasisNo.setText(rcModel.getChas_no());
                            txt_EngineNo.setText(rcModel.getEng_no());

                            if (rcModel != null && !"null".equals(rcModel.getVclass_id())) {
                                wheelerMasterByVClassId(rcModel.getVclass_id());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            rcModel = new RCModel();
                            txt_RCDtlsFnd.setText("RC Details Not Found");
                            lyt_RCCardInfo.setVisibility(View.GONE);
                        }
                    } else {
                        txt_RCDtlsFnd.setText("RC Details Not Found");
                        rcModel = new RCModel();
                        lyt_RCCardInfo.setVisibility(View.GONE);
                        btn_Spinr_WlrCode.setEnabled(true);
                    }

                    int dlRespCode = jsonObject_RCDLInfo.getInt("dlRespCode");
                    if (dlRespCode == 1) {
                        try {
                            lyt_DLCardInfo.setVisibility(View.VISIBLE);
                            lyt_DriverInfo.setVisibility(View.GONE);
                            txt_DLDtlsFnd.setText("DL Details");
                            JSONObject object_DL = jsonObject_RCDLInfo.getJSONObject("dlentity");
                            dlModel = new DLModel();
                            dlModel.setLdcDlNO(object_DL.getString("ldcDlNO") != null ? object_DL.getString("ldcDlNO") : "");
                            dlModel.setLdc_F_NM(object_DL.getString("ldc_F_NM") != null ? object_DL.getString("ldc_F_NM") : "");
                            dlModel.setLdc_PG_NM(object_DL.getString("ldc_PG_NM") != null ? object_DL.getString("ldc_PG_NM") : "");
                            dlModel.setLdc_PADDR2(object_DL.getString("ldc_PADDR1") != null ? object_DL.getString("ldc_PADDR1") : "");
                            dlModel.setTraffic_POINTS(object_DL.getInt("traffic_POINTS") == 0 ? 0 : object_DL.getInt("traffic_POINTS"));
                            dlModel.setLdc_SEX(object_DL.getString("ldc_SEX") != null ? object_DL.getString("ldc_SEX") : "");
                            if ("M".equals(dlModel.getLdc_SEX())){
                                str_Gender="1";
                            } else if ("F".equals(dlModel.getLdc_SEX())){
                                str_Gender="0";
                            }else{
                                str_Gender="2";
                            }
                            txt_DLNoHdng.setText(dlModel.getLdcDlNO()); //DL No
                            txt_DLFName.setText(dlModel.getLdc_PG_NM()); //Father Name
                            txt_DLName.setText(dlModel.getLdc_F_NM()); // Name
                            txt_DLAddress.setText(dlModel.getLdc_PADDR2());
                            txt_DLPntNo.setText("Penalty Points : "+ String.valueOf(dlModel.getTraffic_POINTS()));
                            penalty_points = dlModel.getTraffic_POINTS();
                            penalty_points = 0;
                            str_Name = dlModel.getLdc_F_NM();
                            str_PName = dlModel.getLdc_PG_NM();
                            str_Address = dlModel.getLdc_PADDR2();
                        } catch (Exception e) {
                            e.printStackTrace();
                            dlModel = new DLModel();
                            lyt_DLCardInfo.setVisibility(View.GONE);
                            txt_DLDtlsFnd.setText("DL Details Not Found !");
                            penalty_points = 0;
                            str_Name = "";
                            str_PName = "";
                            str_Address = "";
                        }
                    } else {
                        dlModel = new DLModel();
                        txt_DLDtlsFnd.setText("DL Details Not Found !");
                        lyt_DriverInfo.setVisibility(View.VISIBLE);
                        lyt_DLCardInfo.setVisibility(View.GONE);
                        str_Name = "";
                        str_PName = "";
                        str_Address = "";
                    }

                    int pcRespCode = jsonObject_RCDLInfo.getInt("pcRespCode");
                    if (pcRespCode == 1) {
                        try {
                            JSONObject object_PC = jsonObject_RCDLInfo.getJSONObject("pc_regn_no_v"); // Pending Challans Info
                            pending_FineAmnt = object_PC.getInt("pending_amt");
                            pending_challans = object_PC.getInt("pending_challans");
                            txt_PndgAmnt.setText(String.valueOf(pending_FineAmnt));
                            txt_PndgChalns.setText(String.valueOf(pending_challans));
                        } catch (Exception e) {
                            e.printStackTrace();
                            pending_FineAmnt = 0;
                            pending_challans = 0;
                        }
                    } else {
                        Toaster.showInfoMessage("No pending challans !");
                        pending_FineAmnt = 0;
                        pending_challans = 0;
                    }

                    int serviceRespCode = jsonObject_RCDLInfo.getInt("serviceRespCode"); //OTP StatusInfo
                    if (serviceRespCode == 1) {
                        try {
                            JSONObject jsonObject_Otp = jsonObject_RCDLInfo.getJSONObject("serviceControls");
                            spot_OTP = jsonObject_Otp.getInt("spot_OTP");
                            spot_OTP_Time = jsonObject_Otp.getInt("spot_OTP_Time");
                        } catch (Exception e) {
                            e.printStackTrace();
                            spot_OTP = 0;
                            spot_OTP_Time = 0;
                        }
                    } else {
                        spot_OTP = 0;
                        spot_OTP_Time = 0;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toaster.showWarningMessage("Please check the Network and try again !");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());
                lyt_DriverInfo.setVisibility(View.VISIBLE);

            }
        });
        requestQueue.add(rc_DL_Details_Req);
    }

    private void wheelerMasterByVClassId(final String v_clsId) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressDialog.setMessage("Loading");
        progressDialog.show();
        progressDialog.setCancelable(false);

        StringRequest wheelerMasterByVClassId_Req = new StringRequest(Request.Method.GET, AppConfig.wheelerMasterByVClassId_URL + v_clsId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject_wheelerMasterByVClassId = new JSONObject(response);
                    int respCode = jsonObject_wheelerMasterByVClassId.getInt("respCode");
                    if (respCode == 1) {
                        JSONObject object_VclsId = jsonObject_wheelerMasterByVClassId.getJSONObject("respRemark");
                        str_WlrCode = object_VclsId.getString("wheelercd");
                        btn_Spinr_WlrCode.setText(str_WlrCode);
                        btn_Spinr_WlrCode.setEnabled(false);
                        //sectionMasterByWheelerCode(str_WlrCode);
                    } else {
                        Toaster.showWarningMessage("Please check the Network and try again !");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toaster.showWarningMessage("Please check the Network and try again !");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        requestQueue.add(wheelerMasterByVClassId_Req);
    }

    private void getwheelerMaster() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest getwheelerMaster_Req = new StringRequest(Request.Method.GET, AppConfig.getwheelerMaster, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject_SecVltnNames = new JSONObject(response);
                    int respCode = jsonObject_SecVltnNames.getInt("respCode");
                    if (respCode == 1) {
                        JSONArray jsonArray = new JSONArray(jsonObject_SecVltnNames.getString("respRemark"));
                        mArrayList_Wheelers = new ArrayList<>(jsonArray.length());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String wheelercd = jsonObject.getString("wheelercd");
                            String vclass_id = jsonObject.getString("vclass_id");
                            String veh_class = jsonObject.getString("veh_class");
                            mArrayList_Wheelers.add(veh_class);
                            params_Wheelers.put(veh_class, vclass_id);
                        }

                        spinner_WheelerDialog = new SpinnerDialog(DrunkDriveActivity.this, mArrayList_Wheelers,
                                getResources().getString(R.string.txt_WelerCode), "CLOSE");
                        spinner_WheelerDialog.showSpinerDialog();
                        spinner_WheelerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String s_Text, int i) {
                                btn_Spinr_WlrCode.setText(s_Text);
                                for (String map_Res_PSName : params_Wheelers.keySet()) {
                                    if (s_Text.equals(map_Res_PSName)) {
                                        str_WlrCode = params_Wheelers.get(map_Res_PSName);
                                        break;
                                    }
                                }
                                //sectionMasterByWheelerCode(str_WlrCode);
                            }
                        });
                    } else {
                        Toaster.showWarningMessage("Please check the Network and try again !");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toaster.showWarningMessage("Please check the Network and try again !");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        requestQueue.add(getwheelerMaster_Req);
    }

    private void captureImagefrom_Camera() {
        if (Build.VERSION.SDK_INT <= 23) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(intent, 1);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(DrunkDriveActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider", f));
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imageStatus = "1";
            String picturePath = "";
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    String current_date = new DateUtil().getTodaysDate();
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

                    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "E-Ticket"
                            + File.separator + current_date;
                    File camerapath = new File(path);

                    if (!camerapath.exists()) {
                        camerapath.mkdirs();
                    }
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {
                        outFile = new FileOutputStream(file);
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas = new Canvas(mutableBitmap);

                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setTextSize(80);
                        paint.setTextAlign(Paint.Align.CENTER);

                        int xPos = (canvas.getWidth() / 2);
                        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                        canvas.rotate(-90, xPos, yPos);
                        canvas.drawText("Date & Time: " + new DateUtil().getPresentDateandTime(), xPos, yPos + 300, paint);
                        canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                        canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);
                        Display d = getWindowManager().getDefaultDisplay();
                        int x = d.getWidth();
                        int y = d.getHeight();
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(mutableBitmap, x, y, true);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        mutableBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, outFile);
                        outFile.flush();
                        outFile.close();
                        new SingleMediaScanner(this, file);
                        img_SpotCaptured.setVisibility(View.VISIBLE);
                        bitmapCaptredImg = mutableBitmap;
                        img_SpotCaptured.setImageBitmap(mutableBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        bitmapCaptredImg = null;
                        imageStatus = "0";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    bitmapCaptredImg = null;
                    imageStatus = "0";
                }
            }
        }
    }

    @Override
    public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
        String s_Date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(s_Date);
            @SuppressLint("SimpleDateFormat")
            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            String selectedDLDOB = formatDate.format(date);
            String currentDate = new DateUtil().getDate();
            long daysDL = new DateUtil().DaysCalucate(selectedDLDOB, currentDate);
            if (daysDL > 5824) {
                edt_Txt_DLDOB.setText(formatDate.format(date));
            }else{
                Toaster.showWarningMessage("Date should be Greater than 16 years ");
                edt_Txt_DLDOB.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
        } else {
            latitude = 0.0;
            longitude = 0.0;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
