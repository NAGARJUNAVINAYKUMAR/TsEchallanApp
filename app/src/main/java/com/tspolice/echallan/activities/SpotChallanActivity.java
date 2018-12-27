package com.tspolice.echallan.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;
import com.tspolice.echallan.BuildConfig;
import com.tspolice.echallan.R;
import com.tspolice.echallan.adapters.MultiSelectSearchSpinnerDlg;
import com.tspolice.echallan.adapters.ViolationAdapter;
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
import com.tspolice.echallan.utils.DialogUtils;
import com.tspolice.echallan.utils.SharedPrefsHelper;
import com.tspolice.echallan.utils.SingleMediaScanner;
import com.tspolice.echallan.utils.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;

public class SpotChallanActivity extends AppCompatActivity implements
        DatePickerFragmentDialog.OnDateSetListener, LocationListener {

    RequestQueue requestQueue;

    LoginResModel loginResModel;
    SettingsModel settingsModel;
    MultiSelectModel mselectModel;
    VltnListModel vltnListModel;
    SpotGenModel spotGenModel;
    RCModel rcModel;
    DLModel dlModel;
    DateUtil dateUtil;

    ScrollView srolView_Spot;
    RadioGroup radio_GrpSpot;
    RadioButton rBtn_RegNo, rBtn_TRNo, rBtn_ChasNo, rBtn_EngNo;

    ArrayList<MultiSelectModel> mArrayList_SecVltnNames = new ArrayList<>();
    ArrayList<MultiSelectModel> mArrayList_SelectedVltnLst = new ArrayList<>();
    ArrayList<Integer> preVltnSelectdIds = new ArrayList<>();
    ArrayList<String> mArrayList_Wheelers = new ArrayList<>();
    ArrayList<VltnListModel> mArrayList_DetainRules = new ArrayList<>();
    ArrayList<VltnListModel> mArrayList_CSheetRules = new ArrayList<>();
    ArrayList<Id_proof> id_proofs = new ArrayList<>();
    ArrayList<VltnListModel> vltnListModels = new ArrayList<>();

    HashMap<String, String> params_Wheelers = new HashMap<>();

    LinearLayout lyt_RCCardInfo, lyt_DLCardInfo, lyt_DL_DOB, lyt_vltnsList, lyt_GetInfo, lyt_DriverInfo;

    AppCompatTextView txt_hdngSpot,txt_Reg_Chas,txt_DrName;
    //RC Details
    AppCompatTextView txt_RCDtlsFnd, txt_RCRegChasEngNo, txt_RCOwnrName, txt_RcAddress, txt_VehcileType, txt_RCChasisNo, txt_EngineNo;
    //DL Details
    AppCompatTextView txt_DLDtlsFnd, txt_DLNoHdng, txt_DLName, txt_DLFName, txt_DLAddress, txt_DLPntNo;

    AppCompatTextView txt_PndgChalns, txt_PndgAmnt, txt_TotalFineAmnt, txt_sltdVltnAmnt;

    AppCompatEditText edt_Txt_RegChasEngNo, edt_Txt_DLNo, edt_Txt_DLDOB, edt_Txt_DRName, edt_Txt_DRFName, edt_Txt_DRADRRESS;

    AppCompatImageView img_DLDOB, img_DvrCamera, img_DvrGallery, img_SpotCaptured;

    AppCompatButton btn_GetDetails, btn_Spinr_WlrCode, btn_Spinr_Viltn, btn_SpotNext;

    CardView card_TotlAmountInfo;

    DatePickerFragmentDialog datePickerDialog;

    Calendar calendar;

    ProgressDialog progressDialog;

    MultiSelectSearchSpinnerDlg multiSelectSearchSpinnerDlg;

    private SpinnerDialog spinner_WheelerDialog;

    ListView vltn_List;

    public double latitude = 0.0, longitude = 0.0;

    public int selctdVltnsfineAmnt = 0, pending_FineAmnt = 0, total_FineAmnt = 0, pending_challans = 0, penalty_points = 0, selctdPnltyPnts = 0,
            total_PnltyPnts = 0;
    public static int spot_OTP = 0, spot_OTP_Time = 0;
    public static String detainedStatus = "0";

    public String str_UtCode, str_WlrCode = "", str_RegChaEngNo = "", str_DLNo = "", str_DLDOB = "";

    public String chargeSheetStatus = "0", dlStatus = "0", slctdwithoutDL = "0", imageStatus = "0",
            challan_base_cd = "1", challan_type = "22", enforcementType = "CONTACT", str_Date = "", str_Gender = "1";

    public String str_UtName, str_UtShtForm, str_StateCode, str_DptCode, str_PSCode, str_ResPsCode, str_PntCode, str_PidCode,
            str_Name = "", str_PName = "", str_Address = "";

    public static Bitmap bitmapCaptredImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotchallan);
        initviews();
        dateUtil = new DateUtil();
        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(SpotChallanActivity.this);
        spotGenModel = new SpotGenModel();
        lyt_GetInfo.setVisibility(View.GONE);
        Intent intent=getIntent();
        challan_type=intent.getStringExtra("challanType");
        if ("22".equals(challan_type)){
            txt_hdngSpot.setText(getResources().getString(R.string.spot_Hedng));
        }else{
            txt_hdngSpot.setText(getResources().getString(R.string.crane_Hedng));
            txt_DrName.setText(getResources().getString(R.string.txt_DRNameCrane));
        }
        getPrefferences();
        ConnectivityUtils.getLocation(SpotChallanActivity.this);

        latitude = ConnectivityUtils.latitude;
        longitude = ConnectivityUtils.longitude;

        radio_GrpSpot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
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
                    default:
                        break;

                }
            }
        });

        img_DLDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                datePickerDialog = new DatePickerFragmentDialog();
                datePickerDialog = DatePickerFragmentDialog.newInstance(SpotChallanActivity.this,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show(getSupportFragmentManager(), "DatePicker");

            }
        });

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
                    edt_Txt_DLDOB.setText("");
                } else {
                    lyt_DL_DOB.setVisibility(View.GONE);
                    edt_Txt_DLDOB.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                edt_Txt_DLDOB.setError(null);

            }
        });

        btn_GetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArrayList_SelectedVltnLst = new ArrayList<>();
                vltnListModels = new ArrayList<>();
                lyt_vltnsList.removeAllViews();
                edt_Txt_DRName.setText("");edt_Txt_DRFName.setText("");edt_Txt_DRADRRESS.setText("");
                img_SpotCaptured.setVisibility(View.GONE);
                lyt_DriverInfo.setVisibility(View.GONE);
                spotGenModel = new SpotGenModel();
                bitmapCaptredImg = null;
                detainedStatus = "0";
                if (edt_Txt_RegChasEngNo.getText().toString().isEmpty()) {
                    edt_Txt_RegChasEngNo.setError("Please Enter Registration Number !");
                    edt_Txt_RegChasEngNo.requestFocus();
                } else if (!edt_Txt_DLNo.getText().toString().isEmpty() && edt_Txt_DLNo.getText().toString().length() < 5) {
                    edt_Txt_DLNo.requestFocus();
                    edt_Txt_DLNo.setError("Enter Valid Licence Number !");
                } else if (!edt_Txt_DLNo.getText().toString().isEmpty() && edt_Txt_DLDOB.getText().toString().isEmpty()) {
                    edt_Txt_DLDOB.setError(" Enter DOB of DL !");
                    edt_Txt_DLDOB.requestFocus();
                } else {

                    if (edt_Txt_DLNo.getText().toString().isEmpty() || edt_Txt_DLNo.getText().toString().length() < 5) {
                        dlStatus = "1"; // Prompt to Without Dl Violation
                    } else {
                        dlStatus = "0";
                    }
                    str_RegChaEngNo = edt_Txt_RegChasEngNo.getText().toString().trim().toUpperCase();
                    str_DLNo = edt_Txt_DLNo.getText().toString().trim().toUpperCase();
                    str_DLDOB = edt_Txt_DLDOB.getText().toString().trim().toUpperCase();
                    lyt_GetInfo.setVisibility(View.GONE);
                    rc_DL_Details(str_RegChaEngNo, str_DLNo, str_DLDOB, challan_base_cd);
                }
            }
        });

        btn_Spinr_WlrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getwheelerMaster();
            }
        });

        btn_Spinr_Viltn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str_WlrCode.equals("")) {
                    Toaster.showInfoMessage("Please select Wheeler Type!");
                } else {
                    detainedStatus = "0";
                    multiSelectSearchSpinnerDlg.show(getSupportFragmentManager(), "MultiSelectDlg");
                }
            }
        });

        img_DvrCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityUtils.locationServicesEnabled(SpotChallanActivity.this)) {
                    try {
                        int locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
                        if (locationMode == LOCATION_MODE_HIGH_ACCURACY) {
                            latitude = ConnectivityUtils.latitude;
                            longitude = ConnectivityUtils.longitude;
                            captureImagefrom_Camera();
                            Toast.makeText(getApplicationContext(), "Enable" + ConnectivityUtils.latitude + "Enable" + ConnectivityUtils.longitude, Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    ConnectivityUtils.locationSettings(SpotChallanActivity.this);
                }
            }
        });

        img_DvrGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityUtils.locationServicesEnabled(SpotChallanActivity.this)) {
                    try {
                        int locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
                        if (locationMode == LOCATION_MODE_HIGH_ACCURACY) {
                            latitude = ConnectivityUtils.latitude;
                            longitude = ConnectivityUtils.longitude;
                            selectImagefrom_Gallery();
                            Toast.makeText(getApplicationContext(), "Enable" + ConnectivityUtils.latitude + "Enable" + ConnectivityUtils.longitude, Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    ConnectivityUtils.locationSettings(SpotChallanActivity.this);
                }
            }
        });


        btn_SpotNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (challan_type.equals("22")&& edt_Txt_DRName.getText().toString().isEmpty() && edt_Txt_DRName.isShown()) {
                    edt_Txt_DRName.setError("Please Enter Driver Name !");
                    edt_Txt_DRName.requestFocus();
                } else if (str_WlrCode.equals("") && btn_Spinr_WlrCode.getText().toString().equalsIgnoreCase(getResources().getString(R.string.txt_WelerCode))) {
                    Toaster.showWarningMessage("Please Select Vehicle Type !");
                } else if (vltnListModels.size() == 0) {
                    Toaster.showWarningMessage("Please Select Violation !");
                } else if (challan_type.equals("22") && dlStatus.equals("1") && slctdwithoutDL.equals("0")) {
                    Toaster.showWarningMessage("Please select Without DL Violation !");
                } else if (challan_type.equals("22")&& detainedStatus.equals("1") && imageStatus.equals("0")) {
                    Toaster.showWarningMessage("Please Capture the Image !");
                }else if (challan_type.equals("24")&& imageStatus.equals("0")) {
                    Toaster.showWarningMessage("Please Capture the Image !");
                } else {
                    try {
                        str_Name = str_Name.equals("") ? edt_Txt_DRName.getText().toString().trim().toUpperCase() : str_Name;
                        str_PName = str_PName.equals("") ? edt_Txt_DRFName.getText().toString().trim().toUpperCase() : str_PName;
                        str_Address = str_Address.equals("") ? edt_Txt_DRADRRESS.getText().toString().trim().toUpperCase() : str_Address;

                        if (!str_DLNo.equals("") && !str_DLDOB.equals("")) {
                            id_proofs = new ArrayList<>();
                            Id_proof id_proof = new Id_proof();
                            id_proof.setIdProofCd("2");
                            id_proof.setIdProofDetails(str_DLNo);
                            id_proofs.add(id_proof);
                        }
                        spotGenModel.setId_proof(id_proofs);
                        spotGenModel.setWheeler_cd(str_WlrCode);
                        spotGenModel.setDob(str_DLDOB);
                        spotGenModel.setUnit_cd(str_UtCode);
                        spotGenModel.setUnitName(str_UtName);
                        spotGenModel.setUnitShortForm(str_UtShtForm);
                        spotGenModel.setStateCode(str_StateCode);
                        spotGenModel.setChallan_type(challan_type);
                        spotGenModel.setEnforcementType(enforcementType);
                        spotGenModel.setChargeSheetStatus(chargeSheetStatus);
                        spotGenModel.setDept_cd(str_DptCode);
                        spotGenModel.setPs_juris_cd(str_PSCode);
                        spotGenModel.setResponsible_ps_cd(str_ResPsCode);
                        spotGenModel.setPoint_cd(str_PntCode);
                        spotGenModel.setRegn_no(str_RegChaEngNo);
                        spotGenModel.setOffence_dt(str_Date);
                        spotGenModel.setPid_cd(str_PidCode);
                        spotGenModel.setImageStatus(imageStatus);
                        spotGenModel.setChallan_base_cd(challan_base_cd);
                        spotGenModel.setName(str_Name);
                        spotGenModel.setGender(str_Gender);
                        spotGenModel.setParent_name(str_PName);
                        spotGenModel.setAddress(str_Address);
                        spotGenModel.setPenalty_points(String.valueOf(total_PnltyPnts));
                        spotGenModel.setGps_lat(String.valueOf(latitude));
                        spotGenModel.setGps_long(String.valueOf(longitude));
                        spotGenModel.setViolations(vltnListModels);

                        Intent intent_SpotSecond = new Intent(SpotChallanActivity.this, SpotSecondActivity.class);
                        intent_SpotSecond.putExtra("spotObj", spotGenModel);
                        startActivity(intent_SpotSecond);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initviews() {

        txt_hdngSpot=findViewById(R.id.txt_hdngSpot);
        txt_DrName=findViewById(R.id.txt_DrName);
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

        lyt_vltnsList = findViewById(R.id.lyt_vltnsList);
        card_TotlAmountInfo = findViewById(R.id.card_TotlAmountInfo);
        txt_PndgChalns = findViewById(R.id.txt_PndgChalns);
        txt_PndgAmnt = findViewById(R.id.txt_PendingAmnt);
        txt_sltdVltnAmnt = findViewById(R.id.txt_sltdVltnAmnt);
        txt_TotalFineAmnt = findViewById(R.id.txt_TotalFineAmnt);

        btn_Spinr_WlrCode = findViewById(R.id.btn_Spinr_WlrCode);
        btn_Spinr_Viltn = findViewById(R.id.btn_Spinr_Viltn);
        vltn_List = findViewById(R.id.vltn_List);
        img_DvrCamera = findViewById(R.id.img_DvrCamera);
        img_DvrGallery = findViewById(R.id.img_DvrGallery);
        img_SpotCaptured = findViewById(R.id.img_SpotCaptured);

        btn_SpotNext = findViewById(R.id.btn_SpotNext);

    }

    private void getPrefferences() {

        loginResModel = SharedPrefsHelper.getSavedObjectFromPreference(getApplicationContext(), "mPreference", "mLoginRes", LoginResModel.class);
        if (loginResModel != null) {
            str_UtCode = String.valueOf(loginResModel.getRespRemark().getUnitcd());
            getdetainRules(str_UtCode);
            getchargeSheetRules(str_UtCode);
            str_UtName = loginResModel.getRespRemark().getUnit_name();
            str_UtShtForm = loginResModel.getRespRemark().getUnit_sf();
            str_StateCode = String.valueOf(loginResModel.getRespRemark().getState_cd());
            str_DptCode = String.valueOf(loginResModel.getRespRemark().getDept_cd());
            str_PidCode = String.valueOf(loginResModel.getRespRemark().getUserID());
        }

        settingsModel = SharedPrefsHelper.getSettinsFromPref(getApplicationContext(), "mPreference", "mSettingsInfo", SettingsModel.class);
        if (settingsModel != null) {
            str_PSCode = settingsModel.getStr_PSCode();
            str_ResPsCode = settingsModel.getStr_Res_PSCode();
            str_PntCode = settingsModel.getStr_PntCode();
        }
        str_Date = dateUtil.getFullDateandTime();
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

    private void sectionMasterByWheelerCode(final String wlrCode) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String url = AppConfig.sectionMasterByWheeler_URL + wlrCode + "&challanType=" + challan_type;
        DialogUtils.showProgressDialog(SpotChallanActivity.this);

        StringRequest sectionByWheelerCode_Req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DialogUtils.dismissDialog();
                try {
                    JSONObject jsonObject_SecVltnNames = new JSONObject(response);
                    int respCode = jsonObject_SecVltnNames.getInt("respCode");
                    if (respCode == 1) {
                        JSONArray jsonArray = new JSONArray(jsonObject_SecVltnNames.getString("respRemark"));
                        mArrayList_SecVltnNames = new ArrayList<>(jsonArray.length());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            mselectModel = new MultiSelectModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            mselectModel.setId(i);
                            mselectModel.setOffence_cd(jsonObject.getInt("offence_cd"));
                            mselectModel.setVltnSec(jsonObject.getString("section"));
                            mselectModel.setVltnDis(jsonObject.getString("offence_desc"));
                            mselectModel.setVltnSecName("(" + jsonObject.getInt("offence_cd")
                                    + ")" + jsonObject.getString("section") + "," + jsonObject.getString("offence_desc"));
                            mselectModel.setFine_min(jsonObject.getInt("fine_min"));
                            mselectModel.setFine_max(jsonObject.getInt("fine_max"));
                            mselectModel.setPenalty_points(jsonObject.getInt("penalty_points"));
                            mArrayList_SecVltnNames.add(mselectModel);
                        }

                        multiSelectSearchSpinnerDlg = new MultiSelectSearchSpinnerDlg()
                                .title(getResources().getString(R.string.txt_SlctVltn))
                                .titleSize(25)
                                .positiveText("Done")
                                .negativeText("Cancel")
                                .setMinSelectionLimit(0)
                                .setMaxSelectionLimit(mArrayList_SecVltnNames.size())
                                .preSelectIDsList(preVltnSelectdIds) // List of ids that you need to be selected
                                .multiSelectList(mArrayList_SecVltnNames) // the multi select model list with ids and name
                                .onSubmit(new MultiSelectSearchSpinnerDlg.SubmitCallbackListener() {
                                    @Override
                                    public void onSelected(final ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                                        mArrayList_SelectedVltnLst = new ArrayList<>(selectedIds.size());
                                        preVltnSelectdIds = selectedIds;
                                        selctdVltnsfineAmnt = 0;
                                        selctdPnltyPnts = 0;
                                        total_FineAmnt = 0;
                                        total_PnltyPnts = 0;
                                        lyt_vltnsList.removeAllViews();
                                        card_TotlAmountInfo.setVisibility(View.VISIBLE);

                                        for (int i = 0; i < selectedIds.size(); i++) {
                                            mArrayList_SelectedVltnLst.add(mArrayList_SecVltnNames.get(selectedIds.get(i)));
                                            int sum = mArrayList_SelectedVltnLst.get(i).getFine_min();
                                            selctdVltnsfineAmnt = selctdVltnsfineAmnt + sum;
                                            selctdPnltyPnts = selctdPnltyPnts + mArrayList_SelectedVltnLst.get(i).getPenalty_points();
                                            int of_CD = mArrayList_SelectedVltnLst.get(i).getOffence_cd();

                                            if (64 == of_CD || 123 == of_CD) {
                                                slctdwithoutDL = "1";
                                            }
                                        }
                                        vltnListModels = new ArrayList<>(mArrayList_SelectedVltnLst.size());

                                        for (int i = 0; i < mArrayList_SelectedVltnLst.size(); i++) {

                                            vltnListModel = new VltnListModel();
                                            vltnListModel.setOffence_cd(mArrayList_SelectedVltnLst.get(i).getOffence_cd());
                                            vltnListModel.setVltnSec(mArrayList_SelectedVltnLst.get(i).getVltnSec());
                                            vltnListModel.setVltnDis(mArrayList_SelectedVltnLst.get(i).getVltnDis());
                                            vltnListModel.setFine_max(mArrayList_SelectedVltnLst.get(i).getFine_max());
                                            vltnListModel.setFine_min(mArrayList_SelectedVltnLst.get(i).getFine_min());
                                            vltnListModel.setPenalty_points(mArrayList_SelectedVltnLst.get(i).getPenalty_points());
                                            vltnListModels.add(vltnListModel);

                                            LayoutInflater inflater = null;
                                            inflater = (LayoutInflater) getApplicationContext()
                                                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            final View mLinearView = inflater.inflate(R.layout.item_vltn, null);
                                            AppCompatTextView vltn_Sec = mLinearView.findViewById(R.id.vltn_Sec);
                                            AppCompatTextView vltn_Dis = mLinearView.findViewById(R.id.vltn_Dis);
                                            AppCompatTextView vltn_Amnt = mLinearView.findViewById(R.id.amount);
                                            vltn_Sec.setText(vltnListModels.get(i).getVltnSec());
                                            vltn_Dis.setText(vltnListModels.get(i).getVltnDis());
                                            vltn_Amnt.setText(String.valueOf(vltnListModels.get(i).getFine_min()));
                                            lyt_vltnsList.addView(mLinearView);

                                            final int finalI = i;
                                            mLinearView.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    try {
                                                        ArrayList<Integer> preRemovedIds = new ArrayList<>();
                                                        lyt_vltnsList.removeView(mLinearView);
                                                        int of_CD = mArrayList_SelectedVltnLst.get(finalI).getOffence_cd();

                                                        if (64 == of_CD || 123 == of_CD) {
                                                            slctdwithoutDL = "0";
                                                            detainedStatus = "0";
                                                        }
                                                        selctdVltnsfineAmnt = selctdVltnsfineAmnt - mArrayList_SelectedVltnLst.get(finalI).getFine_min();
                                                        total_FineAmnt = total_FineAmnt - mArrayList_SelectedVltnLst.get(finalI).getFine_min();
                                                        txt_sltdVltnAmnt.setText(String.valueOf(selctdVltnsfineAmnt));
                                                        txt_TotalFineAmnt.setText(String.valueOf(total_FineAmnt));

                                                        Iterator<VltnListModel> iterator = vltnListModels.iterator();
                                                        while (iterator.hasNext()) {
                                                            VltnListModel vltn = iterator.next();
                                                            if (Objects.equals(vltn.getOffence_cd(), vltnListModels.get(finalI).getOffence_cd())) {
                                                                iterator.remove();
                                                                break;
                                                            }
                                                        }
                                                        removeFromSelection(finalI);

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }


                                        for (VltnListModel msmodel : vltnListModels) {
                                            for (VltnListModel msmdl : mArrayList_DetainRules) {
                                                if (Objects.equals(msmodel.getOffence_cd(), msmdl.getOffence_cd())) {
                                                    detainedStatus = "1";
                                                    break;
                                                }
                                            }
                                        }

                                        for (VltnListModel msmodel : vltnListModels) {
                                            for (VltnListModel msmdl : mArrayList_CSheetRules) {
                                                if (Objects.equals(msmodel.getOffence_cd(), msmdl.getOffence_cd())) {
                                                    chargeSheetStatus = "1";
                                                    break;
                                                }
                                            }
                                        }

                                        total_FineAmnt = pending_FineAmnt + selctdVltnsfineAmnt;
                                        total_PnltyPnts = penalty_points + selctdPnltyPnts;
                                        txt_TotalFineAmnt.setText(String.valueOf(total_FineAmnt));
                                        txt_sltdVltnAmnt.setText(String.valueOf(selctdVltnsfineAmnt));
                                    }

                                    @Override
                                    public void onCancel() {
                                        Log.d("Spot", "Dialog cancelled");

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
        requestQueue.add(sectionByWheelerCode_Req);
    }

    private void removeFromSelection(Integer id) {
        for (int i = 0; i < mArrayList_SelectedVltnLst.size(); i++) {
            if (Objects.equals(mArrayList_SelectedVltnLst.get(id).getId(), MultiSelectSearchSpinnerDlg.selectedIdsForCallback.get(i))) {
                MultiSelectSearchSpinnerDlg.selectedIdsForCallback.remove(i);
                break;
            }
        }


    }

    private void rc_DL_Details(final String regChaEngNo, final String dLNo, final String dlDOB, final String chalnCd) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String url = AppConfig.getrc_DLMaster_URL + regChaEngNo + "&dlNo=" + dLNo + "&dlDOB=" + dlDOB + "&challanBaseCd="
                + chalnCd + "&unitCode=" + str_UtCode + "&challanType=" + challan_type;

        DialogUtils.showProgressDialog(SpotChallanActivity.this);
        lyt_GetInfo.setVisibility(View.VISIBLE);

        StringRequest rc_DL_Details_Req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DialogUtils.dismissDialog();
                try {
                    JSONObject jsonObject_RCDLInfo = new JSONObject(response);

                    int rcRespCode = jsonObject_RCDLInfo.getInt("rcRespCode"); //RC Info

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
                    }

                    int dlRespCode = jsonObject_RCDLInfo.getInt("dlRespCode"); //DL Info
                    if (dlRespCode == 1) {

                        try {
                            lyt_DLCardInfo.setVisibility(View.VISIBLE);
                            lyt_DriverInfo.setVisibility(View.GONE);
                            txt_DLDtlsFnd.setText("DL Details");
                            JSONObject object_DL = jsonObject_RCDLInfo.getJSONObject("dlentity");
                            Log.d("DL Info",""+object_DL.toString());
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
                        sectionMasterByWheelerCode(str_WlrCode);
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

        // add the request object to the queue to be executed
        requestQueue.add(wheelerMasterByVClassId_Req);
    }

    private void getwheelerMaster() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DialogUtils.showProgressDialog(SpotChallanActivity.this);

        StringRequest getwheelerMaster_Req = new StringRequest(Request.Method.GET, AppConfig.getwheelerMaster, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DialogUtils.dismissDialog();
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

                        spinner_WheelerDialog = new SpinnerDialog(SpotChallanActivity.this, mArrayList_Wheelers, getResources().getString(R.string.txt_WelerCode), "CLOSE");
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
                                sectionMasterByWheelerCode(str_WlrCode);
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

    private void getdetainRules(final String unitCode) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest getdetainRules_Req = new StringRequest(Request.Method.GET, AppConfig.detainRules + unitCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject_getdetainRules = new JSONObject(response);
                    int respCode = jsonObject_getdetainRules.getInt("respCode");
                    if (respCode == 1) {
                        JSONArray jsonArray = new JSONArray(jsonObject_getdetainRules.getString("respRemark"));
                        mArrayList_DetainRules = new ArrayList<>(jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            vltnListModel = new VltnListModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            vltnListModel.setOffence_cd(jsonObject.getInt("offence_Cd"));
                            mArrayList_DetainRules.add(vltnListModel);
                        }

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

        requestQueue.add(getdetainRules_Req);
    }

    private void getchargeSheetRules(final String unitCode) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest getwheelerMaster_Req = new StringRequest(Request.Method.GET, AppConfig.chargeSheetRules_URL + unitCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject_SecVltnNames = new JSONObject(response);
                    int respCode = jsonObject_SecVltnNames.getInt("respCode");
                    if (respCode == 1) {
                        JSONArray jsonArray = new JSONArray(jsonObject_SecVltnNames.getString("respRemark"));
                        mArrayList_CSheetRules = new ArrayList<>(jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            vltnListModel = new VltnListModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            vltnListModel.setOffence_cd(jsonObject.getInt("offence_Cd"));
                            mArrayList_CSheetRules.add(vltnListModel);
                        }

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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(SpotChallanActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider", f));
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, 1);
        }
    }

    private void selectImagefrom_Gallery() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
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
                        String address = getAddressFromLatLng(latitude,longitude);
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

            } else if (requestCode == 2) {

                try {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                    if (thumbnail != null) {
                        Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas = new Canvas(mutableBitmap); // bmp is the

                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setTextSize(80);
                        paint.setTextAlign(Paint.Align.CENTER);

                        int xPos = (canvas.getWidth() / 2);
                        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                        canvas.rotate(-90, xPos, yPos);
                       /* canvas.drawText("Date & Time: " + new DateUtil().getPresentDateandTime(), xPos, yPos + 300, paint);
                        canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                        canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);*/
                        Display d = getWindowManager().getDefaultDisplay();
                        int x = d.getWidth();
                        int y = d.getHeight();
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(mutableBitmap, x, y, true);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        mutableBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                        img_SpotCaptured.setImageBitmap(mutableBitmap);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                        bitmapCaptredImg = mutableBitmap;
                        img_SpotCaptured.setVisibility(View.VISIBLE);

                    } else if (thumbnail == null) {
                        bitmapCaptredImg = null;
                        imageStatus = "0";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    bitmapCaptredImg = null;
                    imageStatus = "0";

                }
            } else {
                Toaster.showWarningMessage("Image not Captured !");
                bitmapCaptredImg = null;
                imageStatus = "0";
            }
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

    private String getAddressFromLatLng(double lat, double lng) {
        String getAddress = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null) {
                Address address = addresses.get(0);
                StringBuilder addressBuilder = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressBuilder.append(address.getAddressLine(i)).append("\n");
                }
                getAddress = addressBuilder.toString();
            } else {
                getAddress = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            getAddress = "";
        }
        return getAddress;
    }

}
