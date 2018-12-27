package com.tspolice.echallan.activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tspolice.echallan.R;
import com.tspolice.echallan.configs.AppConfig;
import com.tspolice.echallan.models.LoginResModel;
import com.tspolice.echallan.models.SettingsModel;
import com.tspolice.echallan.utils.DialogUtils;
import com.tspolice.echallan.utils.SharedPrefsHelper;
import com.tspolice.echallan.utils.Toaster;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class SettingsActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    private BluetoothAdapter bluetooth_Adapter = null;

    private ArrayAdapter<String> arrayAdapter_BlueTH;

    ArrayList<String> mArrayList_PSNames = new ArrayList<>();
    ArrayList<String> mArrayList_PntNames = new ArrayList<>();
    ArrayList<String> mArrayList_Res_PSNames = new ArrayList<>();

    HashMap<String, String> params_PSNames = new HashMap<>();
    HashMap<String, String> params_PntNames = new HashMap<>();
    HashMap<String, String> params_Res_PSNames = new HashMap<>();

    AppCompatButton btn_Spinr_PsName, btn_Spinr_PointName, btn_Spinr_PsResName, btn_scanBT, btn_Set_Save;
    ListView list_scanBT;
    AppCompatEditText edt_Txt_BTAdres;

    private int REQUEST_ENABLE_BT = 1;

    boolean blueTH_ScanFlag = false;

    public String str_BTAddres, str_PSCode, str_PSName, str_PntName, str_PntCode, str_Res_PSName, str_Res_PSCode, str_UtCode;

    private SpinnerDialog spinner_PSDialog, spinner_PntsDialog, spinner_Res_PSDialog;

    ProgressDialog progressDialog;

    LoginResModel loginResModel;

    SettingsModel settingsModel;

    DialogUtils dialogUtils;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initviews();
        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(SettingsActivity.this);
        dialogUtils=new DialogUtils();

        settingsModel=SharedPrefsHelper.getSettinsFromPref(getApplicationContext(),"mPreference","mSettingsInfo",SettingsModel.class);

        if (settingsModel!=null){
            btn_Spinr_PsName.setText(settingsModel.getStr_PSName());
            btn_Spinr_PointName.setText(settingsModel.getStr_PntName());
            btn_Spinr_PsResName.setText(settingsModel.getStr_Res_PSName());
            edt_Txt_BTAdres.setText(settingsModel.getStr_BluTHAdres());
            str_PSCode=settingsModel.getStr_PSCode();
            str_PntCode=settingsModel.getStr_PntCode();
            str_Res_PSCode=settingsModel.getStr_Res_PSCode();
        }



        loginResModel = SharedPrefsHelper.getSavedObjectFromPreference(getApplicationContext(), "mPreference", "mLoginRes", LoginResModel.class);
        if (loginResModel != null) {
            str_UtCode = String.valueOf(loginResModel.getRespRemark().getUnitcd());
        }

        bluetooth_Adapter = BluetoothAdapter.getDefaultAdapter();

        checkBlueTooth_ActiveState();

        btn_Spinr_PsName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psNamesByUnit_Master("23");
            }
        });

        btn_Spinr_PointName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_PntsDialog.showSpinerDialog();
            }
        });

        btn_Spinr_PsResName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resPSNamesByUnitMaster("23");
            }
        });

        registerReceiver(broadcastReceiver_BlueTH, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        arrayAdapter_BlueTH = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);

        list_scanBT.setAdapter(arrayAdapter_BlueTH);

        list_scanBT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_BTAdres = (String) list_scanBT.getItemAtPosition(position);

                if (!selected_BTAdres.isEmpty()) {
                    list_scanBT.setVisibility(View.GONE);
                    str_BTAddres = selected_BTAdres.substring(0, 17);
                    edt_Txt_BTAdres.setText(str_BTAddres);
                }
                bluetooth_Adapter = BluetoothAdapter.getDefaultAdapter();
                alertmessage();
            }
        });


        btn_Set_Save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (btn_Spinr_PsName.getText().toString().equalsIgnoreCase(getResources().getString(R.string.select_PS))){
                    DialogUtils.showAlertDlg(SettingsActivity.this,"Alert Message","Please select PS Name !!!");
                }else if (btn_Spinr_PointName.getText().toString().equalsIgnoreCase(getResources().getString(R.string.select_Point))){
                    DialogUtils.showAlertDlg(SettingsActivity.this,"Alert Message","Please select Point Name !!!");
                }else if (btn_Spinr_PsResName.getText().toString().equalsIgnoreCase(getResources().getString(R.string.select_Res_PS))){
                    DialogUtils.showAlertDlg(SettingsActivity.this,"Alert Message","Please select Responsible PS Name !!!");
                }else if (edt_Txt_BTAdres.getText().toString().isEmpty()){
                    edt_Txt_BTAdres.requestFocus();
                    edt_Txt_BTAdres.setError("Please configure the BlueTooth Printer !!!");
                }else{
                    if (settingsModel!=null){
                        SharedPrefsHelper.clearSettingsData("mSettingsInfo");
                    }

                    settingsModel=SharedPrefsHelper.getSettinsFromPref(getApplicationContext(),"mPreference","mSettingsInfo",SettingsModel.class);

                    loginResModel = SharedPrefsHelper.getSavedObjectFromPreference(getApplicationContext(), "mPreference", "mLoginRes", LoginResModel.class);

                    settingsModel=new SettingsModel();
                    settingsModel.setStr_PSName(btn_Spinr_PsName.getText().toString());
                    settingsModel.setStr_PSCode(str_PSCode);
                    settingsModel.setStr_PntName(btn_Spinr_PointName.getText().toString());
                    settingsModel.setStr_PntCode(str_PntCode);
                    settingsModel.setStr_Res_PSName(btn_Spinr_PsResName.getText().toString());
                    settingsModel.setStr_Res_PSCode(str_Res_PSCode);
                    settingsModel.setStr_BluTHAdres(edt_Txt_BTAdres.getText().toString());

                    SharedPrefsHelper.saveSettingsToS_Pref(getApplicationContext(), "mPreference", "mSettingsInfo", settingsModel);

                    Intent intent_dashBD=new Intent(getApplicationContext(),DashBoardActivity.class);
                    startActivity(intent_dashBD);
                }

            }
        });

        btn_scanBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list_scanBT.setVisibility(View.VISIBLE);

                final ProgressDialog progressDialog = new ProgressDialog(
                        SettingsActivity.this);
                progressDialog
                        .setMessage("Please wait \n BlueTooth Scan is in Process!!!");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        blueTH_ScanFlag = true;
                        arrayAdapter_BlueTH.clear();
                        bluetooth_Adapter.cancelDiscovery();
                        bluetooth_Adapter.startDiscovery();
                    }
                });

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(6000);
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();


            }
        });

    }

    private void initviews() {
        btn_Spinr_PsName = findViewById(R.id.btn_Spinr_PsName);
        btn_Spinr_PointName = findViewById(R.id.btn_Spinr_PointName);
        btn_Spinr_PsResName = findViewById(R.id.btn_Spinr_PsResName);
        list_scanBT = findViewById(R.id.list_scanBT);
        edt_Txt_BTAdres = findViewById(R.id.edt_Txt_BTAdres);
        btn_scanBT = findViewById(R.id.btn_scanBT);
        btn_Set_Save = findViewById(R.id.btn_Set_Save);
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
                    JSONObject jsonObject_psNames = new JSONObject(response);
                    int respCode = jsonObject_psNames.getInt("respCode");
                    if (respCode == 1) {
                        JSONArray jsonArray = new JSONArray(jsonObject_psNames.getString("respRemark"));
                        mArrayList_PSNames = new ArrayList<>(jsonArray.length());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String ps_cd = jsonObject.getString("psCode");
                            String ps_name = jsonObject.getString("ps_name");
                            mArrayList_PSNames.add(ps_name);
                            params_PSNames.put(ps_name, ps_cd);
                        }

                        spinner_PSDialog = new SpinnerDialog(SettingsActivity.this, mArrayList_PSNames, getResources().getString(R.string.select_PS), "CLOSE");
                        spinner_PSDialog.showSpinerDialog();
                        spinner_PSDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String s_Text, int i) {
                                btn_Spinr_PsName.setText(s_Text);
                                str_PSName = s_Text;
                                for (String map_PSName : params_PSNames.keySet()) {
                                    if (s_Text.equals(map_PSName)) {
                                        str_PSCode = params_PSNames.get(map_PSName);
                                        pointsNamesByPS_Master(str_PSCode);
                                        break;
                                    }
                                }
                            }
                        });
                    } else {
                        Toaster.longToast("Please check the Network and try again !");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toaster.longToast("Please check the Network and try again !");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());

            }
        });
        requestQueue.add(psNamesByUnit_Master_Req);
    }

    private void pointsNamesByPS_Master(final String pnt_Code) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest pntNamesByPS_Master_Req = new StringRequest(Request.Method.GET, AppConfig.pntNamesBY_PSMaster_URL + pnt_Code, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject_psNames = new JSONObject(response);
                    int respCode = jsonObject_psNames.getInt("respCode");
                    if (respCode == 1) {
                        JSONArray jsonArray = new JSONArray(jsonObject_psNames.getString("respRemark"));
                        mArrayList_PntNames = new ArrayList<>(jsonArray.length());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String pnt_cd = jsonObject.getString("point_cd");
                            String pnt_name = jsonObject.getString("point_name");
                            mArrayList_PntNames.add(pnt_name);
                            params_PntNames.put(pnt_name, pnt_cd);
                        }

                        spinner_PntsDialog = new SpinnerDialog(SettingsActivity.this, mArrayList_PntNames, getResources().getString(R.string.select_Point), "CLOSE");
                        spinner_PntsDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String s_Text, int i) {
                                btn_Spinr_PointName.setText(s_Text);
                                str_PntName = s_Text;
                                for (String map_PntName : params_PntNames.keySet()) {
                                    if (s_Text.equals(map_PntName)) {
                                        str_PntCode = params_PntNames.get(map_PntName);
                                        break;
                                    }
                                }

                            }
                        });
                    } else {
                        Toaster.longToast("Please check the Network and try again !");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toaster.longToast("Please check the Network and try again !");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());

            }
        });

        requestQueue.add(pntNamesByPS_Master_Req);
    }

    private void resPSNamesByUnitMaster(final String unitCD) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest resPSNamesByUnitMaster_Req = new StringRequest(Request.Method.GET, AppConfig.psNamesBY_unitMaster_URL + unitCD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject_psNames = new JSONObject(response);
                    int respCode = jsonObject_psNames.getInt("respCode");
                    if (respCode == 1) {
                        JSONArray jsonArray = new JSONArray(jsonObject_psNames.getString("respRemark"));
                        mArrayList_Res_PSNames = new ArrayList<>(jsonArray.length());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String ps_cd = jsonObject.getString("psCode");
                            String ps_name = jsonObject.getString("ps_name");
                            mArrayList_Res_PSNames.add(ps_name);
                            params_Res_PSNames.put(ps_name, ps_cd);
                        }

                        spinner_Res_PSDialog = new SpinnerDialog(SettingsActivity.this, mArrayList_Res_PSNames, getResources().getString(R.string.select_PS), "CLOSE");
                        spinner_Res_PSDialog.showSpinerDialog();
                        spinner_Res_PSDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String s_Text, int i) {
                                btn_Spinr_PsResName.setText(s_Text);
                                str_Res_PSName = s_Text;
                                for (String map_Res_PSName : params_Res_PSNames.keySet()) {
                                    if (s_Text.equals(map_Res_PSName)) {
                                        str_Res_PSCode = params_Res_PSNames.get(map_Res_PSName);
                                        break;
                                    }
                                }

                            }
                        });
                    } else {
                        Toaster.longToast("Please check the Network and try again !");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toaster.longToast("Please check the Network and try again !");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());

            }
        });

        // add the request object to the queue to be executed
        requestQueue.add(resPSNamesByUnitMaster_Req);
    }

    private void checkBlueTooth_ActiveState() {
        if (bluetooth_Adapter == null) {

        } else {
            if (bluetooth_Adapter.isEnabled()) {
                if (bluetooth_Adapter.isDiscovering()) {

                } else {

                }
            } else {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private final BroadcastReceiver broadcastReceiver_BlueTH = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                arrayAdapter_BlueTH.add(device.getAddress() + "\n"
                        + device.getName());
                arrayAdapter_BlueTH.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == REQUEST_ENABLE_BT) {
            checkBlueTooth_ActiveState();
        }
    }

    public void alertmessage() {

        if (bluetooth_Adapter == null) {
            Toaster.longToast("Bluetooth is not available");
        }
        if (!bluetooth_Adapter.isEnabled()) {
            Toaster.longToast("Please enable your BT and re-run this program");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDestroy() {
        System.runFinalizersOnExit(true);
        super.onDestroy();
        unregisterReceiver(broadcastReceiver_BlueTH);
    }

}
