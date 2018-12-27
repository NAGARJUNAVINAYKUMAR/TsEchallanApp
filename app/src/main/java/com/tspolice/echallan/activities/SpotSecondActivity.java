package com.tspolice.echallan.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.tspolice.echallan.R;
import com.tspolice.echallan.configs.AppConfig;
import com.tspolice.echallan.models.DetaindModel;
import com.tspolice.echallan.models.SpotGenModel;
import com.tspolice.echallan.utils.DialogUtils;
import com.tspolice.echallan.utils.Toaster;
import com.tspolice.echallan.utils.ValidationUtils;
import com.tspolice.echallan.utils.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SpotSecondActivity extends AppCompatActivity {

    SpotGenModel spotGenModel;
    DetaindModel detaindModel;

    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    RequestQueue requestQueue;

    LinearLayout lyt_Spotpaymnt;

    RadioGroup radio_GrpSpotPaymnt;
    RadioButton rBtn_Yes, rBtn_No;
    ToggleButton t_BtnRC, t_BtnVehicle, t_BtnDL, t_BtnPermit, t_BtnNone;

    AppCompatEditText edtTxt_MobileNo, edtTxt_OTP;

    AppCompatButton btn_Submit, btn_SendOtp;

    AppCompatTextView txt_otp, txt_ElpsdTime, txt_hdngSpot;

    AppCompatImageButton imgBt_OTPSubmit;

    CountDownTimer countDownTimer;

    ArrayList<DetaindModel> dt_items = new ArrayList<>();

    String paymentStatus = "U", challanType = "";

    String str_OTP, str_contact_no = "";

    Bitmap bitmap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_second_screen);
        initviews();
        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        spotGenModel = (SpotGenModel) intent.getSerializableExtra("spotObj");
        challanType = spotGenModel.getChallan_type();

        if (challanType.equals("22")) {
            lyt_Spotpaymnt.setVisibility(View.VISIBLE);
            txt_hdngSpot.setText(getResources().getString(R.string.spot_Hedng));
        } else {
            lyt_Spotpaymnt.setVisibility(View.GONE);
            txt_hdngSpot.setText(getResources().getString(R.string.crane_Hedng));
        }

        if (SpotChallanActivity.spot_OTP == 1 && challanType.equals("22")) {
            btn_SendOtp.setVisibility(View.VISIBLE);
        } else {
            btn_SendOtp.setVisibility(View.INVISIBLE);
        }

        if (SpotChallanActivity.detainedStatus.equals("1") || challanType.equals("24")) {
            t_BtnVehicle.setChecked(true);
            t_BtnNone.setChecked(false);
        }

        if (SpotChallanActivity.bitmapCaptredImg == null) {
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.profleimg);
        } else {
            bitmap = SpotChallanActivity.bitmapCaptredImg;
        }


        radio_GrpSpotPaymnt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rBtn_Yes:
                        paymentStatus = "P";
                        break;
                    case R.id.rBtn_No:
                        paymentStatus = "U";
                        break;
                    default:
                        break;
                }
            }
        });

        t_BtnRC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    t_BtnNone.setChecked(false);
                } else {
                    if (!t_BtnVehicle.isChecked() && !t_BtnDL.isChecked() && !t_BtnPermit.isChecked()) {
                        t_BtnNone.setChecked(true);
                    }
                }
            }
        });
        t_BtnVehicle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    t_BtnNone.setChecked(false);
                } else {
                    if (!t_BtnRC.isChecked() && !t_BtnDL.isChecked() && !t_BtnPermit.isChecked()) {
                        t_BtnNone.setChecked(true);
                    }
                }
            }
        });

        t_BtnDL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    t_BtnNone.setChecked(false);
                } else {
                    if (!t_BtnRC.isChecked() && !t_BtnVehicle.isChecked() && !t_BtnPermit.isChecked()) {
                        t_BtnNone.setChecked(true);
                    }
                }
            }
        });

        t_BtnPermit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    t_BtnNone.setChecked(false);
                } else {
                    if (!t_BtnRC.isChecked() && !t_BtnDL.isChecked() && !t_BtnVehicle.isChecked()) {
                        t_BtnNone.setChecked(true);
                    }
                }
            }
        });

        t_BtnNone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    t_BtnRC.setChecked(false);
                    t_BtnDL.setChecked(false);
                    t_BtnVehicle.setChecked(false);
                    t_BtnPermit.setChecked(false);
                }
            }
        });

        btn_SendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtTxt_MobileNo.getText().toString().trim().isEmpty()) {
                    edtTxt_MobileNo.setError("Please Enter Mobile Number !");
                } else if (!ValidationUtils.isValidMobile(edtTxt_MobileNo.getText().toString().trim())) {
                    edtTxt_MobileNo.setError("Please Enter Valid Mobile Number !");
                } else {
                    str_contact_no = edtTxt_MobileNo.getText().toString().trim();
                    user_OTP(edtTxt_MobileNo.getText().toString().trim());
                }

            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (challanType.equals("22") && edtTxt_MobileNo.getText().toString().trim().isEmpty()) {
                    edtTxt_MobileNo.setError("Please Enter Mobile Number !");
                } else if (!edtTxt_MobileNo.getText().toString().isEmpty() && !ValidationUtils.isValidMobile(edtTxt_MobileNo.getText().toString().trim())) {
                    edtTxt_MobileNo.setError("Please Enter Valid Mobile Number !");
                } else if (challanType.equals("22") && SpotChallanActivity.detainedStatus.equals("1") && !t_BtnVehicle.isChecked() &&
                        !t_BtnDL.isChecked() && !t_BtnPermit.isChecked() && !t_BtnRC.isChecked()) {
                    Toaster.showWarningMessage("Please Detain Any of Item !");
                } else {
                    detaindModel = new DetaindModel();
                    dt_items = new ArrayList<>();

                    if (t_BtnDL.isChecked()) {    // 1-DL, 2-RC, 3-Vehicle, 4-Permit, 5-None
                        detaindModel = new DetaindModel();
                        detaindModel.setDetainedItemCode("1");
                        detaindModel.setDetainedItemName("DL");
                        detaindModel.setDetainedStatus("1");
                        dt_items.add(detaindModel);
                    }
                    if (t_BtnRC.isChecked()) {
                        detaindModel = new DetaindModel();
                        detaindModel.setDetainedItemCode("2");
                        detaindModel.setDetainedItemName("RC");
                        detaindModel.setDetainedStatus("1");
                        dt_items.add(detaindModel);
                    }
                    if (t_BtnVehicle.isChecked()) {
                        detaindModel = new DetaindModel();
                        detaindModel.setDetainedItemCode("3");
                        detaindModel.setDetainedItemName("VEHICLE");
                        detaindModel.setDetainedStatus("1");
                        dt_items.add(detaindModel);
                    }
                    if (t_BtnPermit.isChecked()) {
                        detaindModel = new DetaindModel();
                        detaindModel.setDetainedItemCode("4");
                        detaindModel.setDetainedItemName("PERMIT");
                        detaindModel.setDetainedStatus("1");
                        dt_items.add(detaindModel);
                    }
                    if (t_BtnNone.isChecked()) {
                        detaindModel = new DetaindModel();
                        detaindModel.setDetainedItemCode("5");
                        detaindModel.setDetainedItemName("NONE");
                        detaindModel.setDetainedStatus("1");
                        dt_items.add(detaindModel);
                    }
                    spotGenModel.setContact_no(edtTxt_MobileNo.getText().toString().trim());
                    spotGenModel.setPaymentStatus(paymentStatus);
                    spotGenModel.setDt_items(dt_items);
                    Gson gson = new Gson();
                    String str_Data = gson.toJson(spotGenModel);
                    Log.d("FinalRequest", "" + str_Data);
                    spotTicketGen(str_Data, bitmap);
                }
            }
        });


    }

    private void initviews() {
        txt_hdngSpot = findViewById(R.id.txt_hdngSpot);
        lyt_Spotpaymnt = findViewById(R.id.lyt_Spotpaymnt);
        radio_GrpSpotPaymnt = findViewById(R.id.radio_GrpSpotPaymnt);
        rBtn_Yes = findViewById(R.id.rBtn_Yes);
        rBtn_No = findViewById(R.id.rBtn_No);
        t_BtnRC = findViewById(R.id.t_BtnRC);
        t_BtnVehicle = findViewById(R.id.t_BtnVehicle);
        t_BtnDL = findViewById(R.id.t_BtnDL);
        t_BtnPermit = findViewById(R.id.t_BtnPermit);
        t_BtnNone = findViewById(R.id.t_BtnNone);
        edtTxt_MobileNo = findViewById(R.id.edtTxt_MobileNo);
        btn_Submit = findViewById(R.id.btn_Submit);
        btn_SendOtp = findViewById(R.id.btn_SendOtp);
        txt_otp = findViewById(R.id.txt_otp);
    }

    public void user_OTP(final String mobile) {

        String otp_Url = AppConfig.user_OTP_URL + mobile;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest otp_Req = new StringRequest(Request.Method.GET, otp_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("respDesc");

                    if ("Success".equalsIgnoreCase(status)) {
                        str_OTP = jsonObject.getString("respRemark");

                        AlertDialog.Builder builder = new AlertDialog.Builder(SpotSecondActivity.this);
                        LayoutInflater inflater = LayoutInflater.from(SpotSecondActivity.this);
                        @SuppressLint("InflateParams")
                        View view = inflater.inflate(R.layout.otpverification_activity, null);
                        builder.setView(view);
                        builder.setCancelable(false);
                        alertDialog = builder.create();
                        alertDialog.show();
                        edtTxt_OTP = alertDialog.findViewById(R.id.otp_view);
                        imgBt_OTPSubmit = alertDialog.findViewById(R.id.imgBt_OTPSubmit);
                        txt_otp = alertDialog.findViewById(R.id.txt_otp);
                        txt_otp.setText(getString(R.string.please_type_verification_code) + mobile);
                        txt_ElpsdTime = alertDialog.findViewById(R.id.txt_ElpsdTime);
                        countTimer(SpotChallanActivity.spot_OTP_Time * 1000);

                        imgBt_OTPSubmit.setVisibility(View.GONE);

                        edtTxt_OTP.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                Log.d("OTPMSG", s.length() + "OTP" + count);
                                if (s.length() == 4) {
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

                        imgBt_OTPSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!edtTxt_OTP.getText().toString().isEmpty() && str_OTP.equals(edtTxt_OTP.getText().toString())) {
                                    alertDialog.dismiss();
                                    countDownTimer.cancel();
                                } else {
                                    edtTxt_OTP.setError("Please enter Valid OTP !");
                                }

                            }
                        });
                    } else {
                        Toaster.longToast("Failed !");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("error in response", "Error: " + error.getMessage());

                    }
                });

        requestQueue.add(otp_Req);
    }

    public void countTimer(final int time) {

        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                ;
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

    public void spotTicketGen(final String sData, final Bitmap bitmap) {
        DialogUtils.showProgressDialog(SpotSecondActivity.this);
        VolleyMultipartRequest spotGenReq = new VolleyMultipartRequest(Request.Method.POST, AppConfig.spotChallanGen_URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                DialogUtils.dismissDialog();
                try {
                    String resPonse = new String(response.data);
                    JSONObject jsonObject_Print = new JSONObject(resPonse);
                    int resCode = jsonObject_Print.getInt("respCode");
                    if (resCode == 1) {
                        String res_PrinData = jsonObject_Print.getString("respRemark");
                        Log.d("FinalRes", "" + res_PrinData);
                        Intent intent_Print = new Intent(SpotSecondActivity.this, PrintActivity.class);
                        intent_Print.putExtra("printInfo", res_PrinData);
                        startActivity(intent_Print);
                    } else {
                        Toaster.longToast("Please check Network!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                params.put("json", sData);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
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
