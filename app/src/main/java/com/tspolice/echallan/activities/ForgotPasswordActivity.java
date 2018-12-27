package com.tspolice.echallan.activities;

import android.app.ProgressDialog;
import android.os.StrictMode;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tspolice.echallan.R;
import com.tspolice.echallan.configs.AppConfig;
import com.tspolice.echallan.utils.Toaster;
import com.tspolice.echallan.utils.ValidationUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText edtTxt_UserID, edtTxt_MobNumber, edtTxt_Password, edtTxt_NewPassword;
    private AppCompatImageButton imgBt_Submit;
    private String userId, mobileNumber, password, newPassword;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();

        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);

        imgBt_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtTxt_UserID.getText().toString().isEmpty()) {
                    edtTxt_UserID.setError("Please Enter User Id !");
                } else if (edtTxt_MobNumber.getText().toString().isEmpty()
                        || (!ValidationUtils.isValidMobile(edtTxt_MobNumber.getText().toString()))) {
                    edtTxt_MobNumber.setError("Please Enter the Mobile Number !");
                } else if (edtTxt_Password.getText().toString().isEmpty()) {
                    edtTxt_Password.setError("Please Enter the Password !");
                } else if (edtTxt_NewPassword.getText().toString().isEmpty()) {
                    edtTxt_NewPassword.setError("Please Enter the New Password !");
                } else {
                    userId = edtTxt_UserID.getText().toString().trim();
                    forgotPswd(userId);
                }
            }
        });
    }

    void initViews() {
        edtTxt_UserID = findViewById(R.id.edtTxt_UserID);
        edtTxt_MobNumber = findViewById(R.id.edtTxt_MobNumber);
        edtTxt_Password = findViewById(R.id.edtTxt_Password);
        edtTxt_NewPassword = findViewById(R.id.edtTxt_NewPassword);

        imgBt_Submit = findViewById(R.id.imgBt_Submit);
    }

    private void forgotPswd(String userId) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.forgotPswd + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String respDesc = jsonObject.getString("respDesc");
                    if ("Sucess".equals(respDesc)) {
                        Toaster.showSuccessMessage("Success");
                    } else {
                        Toaster.showErrorMessage("Failed");
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
        requestQueue.add(stringRequest);
    }
}
