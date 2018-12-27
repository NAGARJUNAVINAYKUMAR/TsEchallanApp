package com.tspolice.echallan.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.tspolice.echallan.R;
import com.tspolice.echallan.configs.AppConfig;
import com.tspolice.echallan.models.LoginResModel;
import com.tspolice.echallan.models.RespRemarksModel;
import com.tspolice.echallan.utils.ConnectivityUtils;
import com.tspolice.echallan.utils.DateUtil;
import com.tspolice.echallan.utils.DeviceUtils;
import com.tspolice.echallan.utils.DialogUtils;
import com.tspolice.echallan.utils.SharedPrefsHelper;
import com.tspolice.echallan.utils.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;

/**
 * Created by Srinivas on 10/4/2018.
 */

public class LoginActivity extends AppCompatActivity {

    AppCompatEditText edtTxt_User_PID, edtTxt_User_Pwd;

    AppCompatButton btn_Cadre;

    AppCompatImageButton imgBt_Login;

    AppCompatTextView txt_register;

    private ProgressDialog progressDialog;
    RequestQueue requestQueue;

    LoginResModel loginResModel;
    RespRemarksModel respRemarksModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initviews();

        edtTxt_User_PID.setText("Srini456");
        edtTxt_User_Pwd.setText("srinivas");

        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);

        imgBt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtTxt_User_PID.getText().toString().isEmpty()) {
                    edtTxt_User_PID.setError("Please enter the UserId !");
                    edtTxt_User_PID.requestFocus();
                } else if (edtTxt_User_Pwd.getText().toString().isEmpty()) {
                    edtTxt_User_Pwd.setError("Please enter the Password ! ");
                    edtTxt_User_Pwd.requestFocus();
                } else {
                    requestPermissions();
                    // user_Login(edtTxt_User_PID.getText().toString(), edtTxt_User_Pwd.getText().toString());
                }
            }
        });

        btn_Cadre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Cadre = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_Cadre);
            }
        });

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Register = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent_Register);
            }
        });
    }

    public void user_Login(final String userId, final String pwd) {

        //  http://125.16.1.70:8191/echallan/officerDetails?userID=Maruthiios&pwd=8341646667

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String url_Login = AppConfig.user_Login_URL + "?userID=" + userId + "&pwd=" + pwd;

        /*progressDialog.setMessage("Loading");
        progressDialog.show();*/

        DialogUtils.showProgressDialog(LoginActivity.this);

        StringRequest user_Login_Req = new StringRequest(Request.Method.GET, url_Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressDialog.dismiss();
                DialogUtils.dismissDialog();

                try {
                    loginResModel = new LoginResModel();
                    respRemarksModel = new RespRemarksModel();

                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("respCode");
                    if (1 == status) {
                        JsonParser jsonParser = new JsonParser();
                        JsonElement jsonElement = jsonParser.parse(jsonObject.getString("respRemark"));
                        Gson gson = new Gson();
                        RespRemarksModel respRemarksModel = gson.fromJson(jsonElement, RespRemarksModel.class);
                        loginResModel.setRespRemark(respRemarksModel);
                        SharedPrefsHelper.saveObjectToSharedPreference(getApplicationContext(), "mPreference", "mLoginRes", loginResModel);

                        Intent intent_Login = new Intent(getApplicationContext(), DashBoardActivity.class);
                        intent_Login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_Login);
                        finish();

                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());
                progressDialog.dismiss();
                Toaster.longToast("Please check the Network And Try again!");

            }
        });

        requestQueue.add(user_Login_Req);
    }

    public void requestPermissions() {
        Permissions.check(this, new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,},
                "Camera and storage permissions are required because...", new Permissions.Options()
                        .setRationaleDialogTitle("Info"),
                new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        if (ConnectivityUtils.locationServicesEnabled(LoginActivity.this)) {
                            try {
                                int locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
                                if (locationMode == LOCATION_MODE_HIGH_ACCURACY) {
                                    String imei_No = DeviceUtils.getDeviceUid();
                                    user_Login(edtTxt_User_PID.getText().toString(), edtTxt_User_Pwd.getText().toString());
                                } else {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            } catch (Settings.SettingNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ConnectivityUtils.locationSettings(LoginActivity.this);
                        }

                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        Toast.makeText(context, "Camera+Storage Denied:\n" + Arrays.toString(deniedPermissions.toArray()),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public boolean onBlocked(Context context, ArrayList<String> blockedList) {
                        Toast.makeText(context, "Camera+Storage blocked:\n" + Arrays.toString(blockedList.toArray()),
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public void onJustBlocked(Context context, ArrayList<String> justBlockedList,
                                              ArrayList<String> deniedPermissions) {
                        Toast.makeText(context, "Camera+Storage just blocked:\n" + Arrays.toString(deniedPermissions.toArray()),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void initviews() {
        edtTxt_User_PID = findViewById(R.id.edtTxt_User_PID);
        edtTxt_User_Pwd = findViewById(R.id.edtTxt_User_Pwd);
        imgBt_Login = findViewById(R.id.imgBt_Login);
        btn_Cadre = findViewById(R.id.btn_Cadre);
        txt_register = findViewById(R.id.txt_register);
    }

    public static int minIndex(ArrayList<Float> list) {
        return list.indexOf(Collections.min(list));
    }
}
