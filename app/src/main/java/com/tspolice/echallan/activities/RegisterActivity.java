package com.tspolice.echallan.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.tspolice.echallan.R;
import com.tspolice.echallan.configs.AppConfig;
import com.tspolice.echallan.utils.ValidationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    EditText edtTxt_EmpID, edtTxt_UserId, edtTxt_Name, edtTxt_Pwd, edtTxt_MobNumber,
            edtTxt_EmailId, edtTxt_OTP;

    String str_EmpId, str_UserId, str_Name, str_PWD, str_MobNumber, str_Email;
    String str_OTP, str_CadreCode, str_UnitCode, str_PSCode;

    AppCompatButton btn_OTPSubmit;

    AppCompatImageButton imgBt_Reg,imgBt_OTPSubmit;

    AppCompatTextView txt_Login;

    ArrayAdapter<String> cadre_Adapter;
    ArrayAdapter<String> distByState_Adapter;
    ArrayAdapter<String> psNames_Adapter;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;

    ArrayList<String> mArrayList_Cadre = new ArrayList<>();
    ArrayList<String> mArrayList_DistBYState = new ArrayList<>();
    ArrayList<String> mArrayList_PSNames = new ArrayList<>();

    HashMap<String, String> params_Cadre = new HashMap<>();
    HashMap<String, String> params_Dist = new HashMap<>();
    HashMap<String, String> params_PSNames = new HashMap<>();

    SearchableSpinner spinner_Rank, spinner_Dist, spinner_PS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initviews();
        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        caderMaster();
        unit_Dist_Master();

        imgBt_Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTxt_EmpID.getText().toString().isEmpty()) {
                    edtTxt_EmpID.setError("Please Enter the Employee Id !");
                } else if (edtTxt_UserId.getText().toString().isEmpty()) {
                    edtTxt_UserId.setError("Please Enter User Id !");
                } else if (edtTxt_Name.getText().toString().isEmpty()) {
                    edtTxt_Name.setError("Please Enter the Name ! ");
                } else if (edtTxt_Pwd.getText().toString().isEmpty()) {
                    edtTxt_Pwd.setError("Please Enter the Password !");
                } else if (edtTxt_MobNumber.getText().toString().isEmpty() || !ValidationUtils.isValidMobile(edtTxt_MobNumber.getText().toString())) {
                    edtTxt_MobNumber.setError("Please Enter the MobileNumber !");
                } else if (edtTxt_EmailId.getText().toString().isEmpty() || !ValidationUtils.isValidEmaillId(edtTxt_EmailId.getText().toString())) {
                    edtTxt_EmailId.setError("Please Enter Valid EmailId !");
                } else if (str_CadreCode == null) {
                    showToast("Please select Rank !");
                } else if (str_UnitCode == null) {
                    showToast("Please select District !");
                } else if (str_PSCode == null) {
                    showToast("Please select PS name !");
                } else {
                    str_EmpId = edtTxt_EmpID.getText().toString();
                    str_UserId = edtTxt_UserId.getText().toString();
                    str_Name = edtTxt_Name.getText().toString();
                    str_PWD = edtTxt_Pwd.getText().toString();
                    str_MobNumber = edtTxt_MobNumber.getText().toString();
                    str_Email = edtTxt_EmailId.getText().toString();
                    user_OTP(str_MobNumber);
                }
            }
        });

        txt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent_Login);
            }
        });

    }

    private void initviews() {
        edtTxt_EmpID = findViewById(R.id.edtTxt_EmpID);
        edtTxt_UserId = findViewById(R.id.edtTxt_UserId);
        edtTxt_Name = findViewById(R.id.edtTxt_Name);
        edtTxt_Pwd = findViewById(R.id.edtTxt_Pwd);
        edtTxt_MobNumber = findViewById(R.id.edtTxt_MobNumber);
        edtTxt_EmailId = findViewById(R.id.edtTxt_EmailId);
        imgBt_Reg=findViewById(R.id.imgBt_Reg);
        txt_Login = findViewById(R.id.txt_Login);
        spinner_Rank = findViewById(R.id.spinner_Rank);
        spinner_Dist = findViewById(R.id.spinner_Dist);
        spinner_PS = findViewById(R.id.spinner_PS);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        LayoutInflater inflater = LayoutInflater.from(RegisterActivity.this);
                        @SuppressLint("InflateParams")
                        View view = inflater.inflate(R.layout.otpverification_activity, null);
                        builder.setView(view);
                        builder.setCancelable(false);
                        alertDialog = builder.create();
                        alertDialog.show();
                        edtTxt_OTP = alertDialog.findViewById(R.id.otp_view);
                        imgBt_OTPSubmit = alertDialog.findViewById(R.id.imgBt_OTPSubmit);
                        imgBt_OTPSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!edtTxt_OTP.getText().toString().isEmpty() && str_OTP.equals(edtTxt_OTP.getText().toString())) {
                                    user_Registration();
                                } else {
                                    edtTxt_OTP.setError("Please enter Valid OTP !");
                                }

                            }
                        });
                    } else {
                        showToast("Failed");
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

    private void caderMaster() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest caderMaster_Req = new StringRequest(Request.Method.GET, AppConfig.cadreMaster_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject_Cadre = new JSONObject(response);
                    int respCode=jsonObject_Cadre.getInt("respCode");
                    if (respCode==1){
                        JSONArray jsonArray = new JSONArray(jsonObject_Cadre.getString("respRemark"));
                        mArrayList_Cadre = new ArrayList<>(jsonArray.length());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String str_CadreCode = jsonObject.getString("cadre_cd");
                            String str_CadreName = jsonObject.getString("cadre_name");
                            mArrayList_Cadre.add(str_CadreName);
                            params_Cadre.put(str_CadreName, str_CadreCode);
                        }
                        cadre_Adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, mArrayList_Cadre);
                        cadre_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_Rank.setAdapter(cadre_Adapter);

                        spinner_Rank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    String cadre_Name = spinner_Rank.getSelectedItem().toString();

                                    for (String mapCadreName : params_Cadre.keySet()) {
                                        if (cadre_Name.equals(mapCadreName)) {
                                            str_CadreCode = params_Cadre.get(mapCadreName);
                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }else{
                        showToast("Please check the Network and try again !");
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Please check the Network and try again !");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());

            }
        });

        // add the request object to the queue to be executed
        requestQueue.add(caderMaster_Req);
    }

    private void unit_Dist_Master() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest unit_Dist_Master_Req = new StringRequest(Request.Method.GET, AppConfig.unitMaster_URL + 1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject_Dist=new JSONObject(response);
                    int respCode=jsonObject_Dist.getInt("respCode");
                    if (respCode==1){
                        JSONArray jsonArray = new JSONArray(jsonObject_Dist.getString("respRemark"));
                        mArrayList_DistBYState = new ArrayList<>(jsonArray.length());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String unitCode = jsonObject.getString("unit_cd");
                            String unitName = jsonObject.getString("unit_name");
                            mArrayList_DistBYState.add(unitName);
                            params_Dist.put(unitName, unitCode);
                        }
                        distByState_Adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, mArrayList_DistBYState);
                        distByState_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_Dist.setAdapter(distByState_Adapter);

                        spinner_Dist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    String unit_Name = spinner_Dist.getSelectedItem().toString();

                                    for (String mapUnitName : params_Dist.keySet()) {
                                        if (unit_Name.equals(mapUnitName)) {
                                            str_UnitCode = params_Dist.get(mapUnitName);
                                            psNamesByUnit_Master(str_UnitCode);
                                            break;
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }else{
                        showToast("Please check the Network and try again !");
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Please check the Network and try again !");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());

            }
        });

        requestQueue.add(unit_Dist_Master_Req);
    }

    private void psNamesByUnit_Master(final String unitCD) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest psNamesByUnit_Master_Req = new StringRequest(Request.Method.GET, AppConfig.psNamesBY_unitMaster_URL + unitCD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject_psNames=new JSONObject(response);
                    int respCode=jsonObject_psNames.getInt("respCode");
                    if (respCode==1) {
                        JSONArray jsonArray = new JSONArray(jsonObject_psNames.getString("respRemark"));
                        mArrayList_PSNames = new ArrayList<>(jsonArray.length());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String ps_cd = jsonObject.getString("ps_cd");
                            String ps_name = jsonObject.getString("ps_name");
                            mArrayList_PSNames.add(ps_name);
                            params_PSNames.put(ps_name, ps_cd);
                        }
                        psNames_Adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, mArrayList_PSNames);
                        psNames_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_PS.setAdapter(psNames_Adapter);

                        spinner_PS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    String unit_Name = spinner_PS.getSelectedItem().toString();

                                    for (String mapUnitName : params_PSNames.keySet()) {
                                        if (unit_Name.equals(mapUnitName)) {
                                            str_PSCode = params_PSNames.get(mapUnitName);
                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }else{
                        showToast("Please check the Network and try again !");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Please check the Network and try again !");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());

            }
        });

        // add the request object to the queue to be executed
        requestQueue.add(psNamesByUnit_Master_Req);
    }

    private void user_Registration() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressDialog.setMessage("Loading");
        progressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("emp_id", str_EmpId);
        params.put("loginId", str_UserId);
        params.put("pwd", str_PWD);
        params.put("emp_name", str_Name);
        params.put("email_id", str_Email);
        params.put("contact_no", str_MobNumber);
        params.put("role_cd", "0");
        params.put("cadre_cd", str_CadreCode);
        params.put("ps_cd", str_PSCode);
        params.put("otp_no", str_OTP);
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConfig.user_Register_URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    if (null != response.toString()) {
                        String status = response.getString("respCode");
                        if ("1".equals(status)) {
                            showToast("User Registered Successfully !");
                            //  session.createLoginSession(str_UserName, str_Email, str_mobile);
                            alertDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            showToast("Failed");
                        }
                    } else {
                        showToast("User Registration Failed !");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        requestQueue.add(req);
    }

    public void showToast(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
