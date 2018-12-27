package com.tspolice.echallan.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.tspolice.echallan.R;
import com.tspolice.echallan.configs.AppConfig;
import com.tspolice.echallan.models.DetaindModel;
import com.tspolice.echallan.models.SpotGenModel;
import com.tspolice.echallan.utils.DateUtil;
import com.tspolice.echallan.utils.DialogUtils;
import com.tspolice.echallan.utils.Toaster;
import com.tspolice.echallan.utils.ValidationUtils;
import com.tspolice.echallan.utils.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DDSecondActivity extends AppCompatActivity implements
        View.OnClickListener {

    private AppCompatEditText edt_Txt_Date, edt_Txt_Time, edtTxt_Breath_SerialNo, edtTxt_Alcohol_Reading,
            edtTxt_Mobile_No,edtTxt_OTP;
    private AppCompatImageView img_DD_Date, img_DD_Time;
    private AppCompatButton btn_SendOtp, btn_DDCancel, btn_DDSubmit;
    private AppCompatImageButton imgBt_OTPSubmit;
    private AppCompatTextView txt_otp, txt_ElpsdTime;
    private OtpView otpView;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private DateUtil dateUtil;
    private SpotGenModel ddGenModel;
    private CountDownTimer countDownTimer;
    public String paymentStatus = "U", str_OTP, str_contact_no = "", detainFlag = "0", otpViewValue, offenceDate, offenceTime;
    Calendar calendar;
    private int year, month, day, hour, minute;
    private boolean isOTPVerified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dd_second);

        initViews();

        initObjects();

        Intent intent = getIntent();
        ddGenModel = (SpotGenModel) intent.getSerializableExtra("ddObj");

        if (DrunkDriveActivity.spot_OTP == 1) {
            btn_SendOtp.setVisibility(View.VISIBLE);
        } else {
            btn_SendOtp.setVisibility(View.GONE);
        }

        edt_Txt_Date.setText(dateUtil.getDate());
        edt_Txt_Time.setText(dateUtil.getPresentTime());

        img_DD_Date.setOnClickListener(DDSecondActivity.this);
        img_DD_Time.setOnClickListener(DDSecondActivity.this);
        btn_SendOtp.setOnClickListener(DDSecondActivity.this);
        btn_DDSubmit.setOnClickListener(DDSecondActivity.this);
    }

    private void initObjects() {
        requestQueue = Volley.newRequestQueue(DDSecondActivity.this);
        progressDialog = new ProgressDialog(DDSecondActivity.this);
        dateUtil = new DateUtil();

    }


    private void initViews() {
        edt_Txt_Date = findViewById(R.id.edt_Txt_Date);
        edt_Txt_Time = findViewById(R.id.edt_Txt_Time);
        edtTxt_Breath_SerialNo = findViewById(R.id.edtTxt_Breath_SerialNo);
        edtTxt_Alcohol_Reading = findViewById(R.id.edtTxt_Alcohol_Reading);
        edtTxt_Mobile_No = findViewById(R.id.edtTxt_Mobile_No);

        img_DD_Date = findViewById(R.id.img_DD_Date);
        img_DD_Time = findViewById(R.id.img_DD_Time);

        btn_SendOtp = findViewById(R.id.btn_SendOtp);
        btn_DDCancel = findViewById(R.id.btn_DDCancel);
        btn_DDSubmit = findViewById(R.id.btn_DDSubmit);
    }

    public void userOTP(final String mobile) {

        String otp_Url = AppConfig.user_OTP_URL + mobile;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DialogUtils.showProgressDialog(DDSecondActivity.this);

        StringRequest otp_Req = new StringRequest(Request.Method.GET, otp_Url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("respDesc");
                    if ("Success".equalsIgnoreCase(status)) {
                        str_OTP = jsonObject.getString("respRemark");
                        AlertDialog.Builder builder = new AlertDialog.Builder(DDSecondActivity.this);
                        LayoutInflater inflater = LayoutInflater.from(DDSecondActivity.this);
                        @SuppressLint("InflateParams")
                        View view = inflater.inflate(R.layout.otpverification_activity, null);
                        builder.setView(view);
                        builder.setCancelable(false);
                        alertDialog = builder.create();
                        alertDialog.show();

                        imgBt_OTPSubmit = alertDialog.findViewById(R.id.imgBt_OTPSubmit);
                        imgBt_OTPSubmit.setVisibility(View.GONE);
                        txt_otp = alertDialog.findViewById(R.id.txt_otp);
                        txt_otp.setText(getString(R.string.please_type_verification_code) + mobile);
                        txt_ElpsdTime = alertDialog.findViewById(R.id.txt_ElpsdTime);
                        edtTxt_OTP = alertDialog.findViewById(R.id.otp_view);
                        countTimer(DrunkDriveActivity.spot_OTP_Time * 1000);

                        edtTxt_OTP.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (s.length()== 4){
                                    if (str_OTP.equals(s.toString())) {
                                        alertDialog.dismiss();
                                        countDownTimer.cancel();
                                        Toaster.showSuccessMessage("Otp verified Successfully !");
                                    } else {
                                        edtTxt_OTP.setError("Please enter Valid OTP !");
                                    }
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                    } else {
                        Toaster.showErrorMessage("Failed !");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        requestQueue.add(otp_Req);
    }

    private void countTimer(final int time) {
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txt_ElpsdTime.setText("Elapsed Time 00:" + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + "");
            }

            @Override
            public void onFinish() {
                txt_ElpsdTime.setText("Time's Up!");
                alertDialog.dismiss();
                imgBt_OTPSubmit.setVisibility(View.GONE);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_DD_Date:
                calendar=Calendar.getInstance();
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                day=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DDSecondActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        offenceDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                        edt_Txt_Date.setText(offenceDate);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setSpinnersShown(true);
                datePickerDialog.getDatePicker().setCalendarViewShown(false);
                Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.setTitle("Please Select Date");
                datePickerDialog.show();
                break;
            case R.id.img_DD_Time:
                calendar=Calendar.getInstance();
                hour=calendar.get(Calendar.HOUR_OF_DAY);
                minute=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(DDSecondActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        offenceTime = hourOfDay + ":" + minute;
                        edt_Txt_Time.setText(offenceTime);
                    }
                }, hour, minute, true);
                Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.setTitle("Please Select Time");
                timePickerDialog.show();
                break;
            case R.id.btn_SendOtp:
                if (edtTxt_Mobile_No.getText().toString().trim().isEmpty()) {
                    edtTxt_Mobile_No.setError("Please Enter Mobile Number !");
                } else {
                    str_contact_no = edtTxt_Mobile_No.getText().toString().trim();
                    isOTPVerified = false;
                    userOTP(edtTxt_Mobile_No.getText().toString().trim());
                }
                break;
            case R.id.imgBt_OTPSubmit:
                if (otpView.getText().toString().trim().isEmpty()) {
                    otpView.setError("Please enter OTP");
                } else if (otpViewValue.equals(str_OTP)) {
                    alertDialog.dismiss();
                    countDownTimer.cancel();
                    Toaster.showSuccessMessage("OTP Verified");
                } else {
                    Toaster.showErrorMessage("Invalid OTP");
                }
                break;
            case R.id.btn_DDSubmit:    //TS07GK2466
                if (edtTxt_Breath_SerialNo.getText().toString().trim().isEmpty()) {
                    edtTxt_Breath_SerialNo.setError("Please Enter Breath Serial Number !");
                    edtTxt_Breath_SerialNo.requestFocus();
                } else if (edtTxt_Alcohol_Reading.getText().toString().trim().isEmpty()) {
                    edtTxt_Alcohol_Reading.setError("Please Enter Alcohol Reading !");
                    edtTxt_Alcohol_Reading.requestFocus();
                } else if (Integer.parseInt(edtTxt_Alcohol_Reading.getText().toString().trim()) <=35){
                    Toaster.showWarningMessage("Alcohol reading should be greater than 36 !");
                } else if (edtTxt_Mobile_No.getText().toString().trim().isEmpty()) {
                    edtTxt_Mobile_No.setError("Please Enter Mobile Number !");
                    edtTxt_Mobile_No.requestFocus();
                } else if (!edtTxt_Mobile_No.getText().toString().isEmpty() && !ValidationUtils.isValidMobile(edtTxt_Mobile_No.getText().toString().trim())) {
                    edtTxt_Mobile_No.setError("Please Enter Valid Mobile Number !");
                    edtTxt_Mobile_No.requestFocus();
                }else if (!isOTPVerified) {
                    Toaster.showWarningMessage("Please Verify OTP");
                } else {
                    DetaindModel detaindModel = new DetaindModel();
                    ArrayList<DetaindModel> dt_items = new ArrayList<>();
                    detaindModel.setDetainedItemCode("2");
                    detaindModel.setDetainedItemName("VEHICLE");
                    detaindModel.setDetainedStatus("1");
                    dt_items.add(detaindModel);
                    ddGenModel.setBacSerialNo(edtTxt_Breath_SerialNo.getText().toString().trim());
                    ddGenModel.setBaclevel(edtTxt_Alcohol_Reading.getText().toString().trim());
                    ddGenModel.setContact_no(edtTxt_Mobile_No.getText().toString().trim());
                    ddGenModel.setPaymentStatus(paymentStatus);
                    ddGenModel.setDt_items(dt_items);
                    Gson gson = new Gson();
                    String str_Data = gson.toJson(ddGenModel);
                    Log.d("FinalRequest--->",str_Data);
                    ddTicketGen(str_Data, DrunkDriveActivity.bitmapCaptredImg);
                }
                break;
            default:
                break;
        }
    }

    private void ddTicketGen(final String str_data, final Bitmap bitmapCaptredImg) {
        progressDialog.setMessage("Loading");
        progressDialog.show();
        VolleyMultipartRequest spotGenReq = new VolleyMultipartRequest(Request.Method.POST,
                AppConfig.spotChallanGen_URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                progressDialog.dismiss();
                try {
                    String resPonse = new String(response.data);
                    JSONObject jsonObject_Print = new JSONObject(resPonse);
                    int resCode = jsonObject_Print.getInt("respCode");
                    if (resCode == 1) {
                        String res_PrinData = jsonObject_Print.getString("respRemark");
                        Log.d("FinalRes", "" + res_PrinData);
                        Intent intent_Print = new Intent(DDSecondActivity.this, PrintActivity.class);
                        intent_Print.putExtra("printInfo", res_PrinData);
                        startActivity(intent_Print);
                    } else {
                        Toaster.longToast("Please check Network!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toaster.longToast("Please check Network!");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("json", str_data);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmapCaptredImg)));
                return params;
            }
        };
        requestQueue.add(spotGenReq);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
