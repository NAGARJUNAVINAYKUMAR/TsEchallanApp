package com.tspolice.echallan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tspolice.echallan.R;
import com.tspolice.echallan.adapters.DashBDGridViewAdapter;
import com.tspolice.echallan.models.LoginResModel;
import com.tspolice.echallan.models.RespRemarksModel;
import com.tspolice.echallan.models.SettingsModel;
import com.tspolice.echallan.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class DashBoardActivity extends AppCompatActivity {

    private static String[] app_name = {"SCMC", "SPOT", "DRUNK & DRIVE", "CRANE", "GHMC", "SEIZURE", "COURT",
            "RELEASE DOCUMENTS", "REPORTS", "SETTINGS", "VEHICLE HISTORY", "NONE", "PRINT", "Camera"};

    private static int[] app_icon = { R.drawable.spot, R.drawable.spot, R.drawable.drunkdrive, R.drawable.crane, R.drawable.ghmc,
            R.drawable.seizure, R.drawable.court, R.drawable.releaseitms, R.drawable.reports, R.drawable.settings,
            R.drawable.hcp_right, R.drawable.logo_hyd, R.drawable.rac_logo, R.drawable.cyb_logo };

    String str_cadres = "";
    String[] strArray_cadres = new String[20];
    int[] cadres_array;

    LinearLayout lyt_Spot, lyt_DD, lyt_CpAct, lyt_RelseItms, lyt_Reports, lyt_Settngs;
    RelativeLayout rlyt_ghmc, rlyt_szre, rlyt_court;
    AppCompatImageView img_spot, img_DD, img_cpAct, img_GHMC, img_Seizre, img_Court, img_relseItms, img_Reports, img_Settngs;
    AppCompatTextView txt_Spot, txt_DD, txt_cpAct, txt_GHMC, txt_Seizure, txt_Court, txt_relseItms, txt_Reports, txt_Settngs;

    ArrayList<String> selected_AppNames_Array;
    ArrayList<Integer> selected_appIcons_Array;
    GridView gridView_dashBoard;
    DashBDGridViewAdapter dashBDGridViewAdapter;
    LoginResModel loginResModel;
    SettingsModel settingsModel;
    RespRemarksModel respRemarksModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardtest);
        initViews();
        selected_AppNames_Array = new ArrayList<>();
        selected_appIcons_Array = new ArrayList<>();
        loginResModel = SharedPrefsHelper.getSavedObjectFromPreference(getApplicationContext(), "mPreference", "mLoginRes", LoginResModel.class);

        settingsModel = SharedPrefsHelper.getSettinsFromPref(getApplicationContext(), "mPreference", "mSettingsInfo", SettingsModel.class);

        //if (settingsModel != null) {

            assert loginResModel != null;
            str_cadres=loginResModel.getRespRemark().getDuties();
            //str_cadres = MainActivity.str_Maincadres;
            strArray_cadres = str_cadres.split(",");
            cadres_array = new int[strArray_cadres.length];

            try {

                for (int i = 0; i < strArray_cadres.length; i++) {
                    cadres_array[i] = Integer.parseInt(strArray_cadres[i]);
                }

                for (int aDuties_Array : cadres_array) {
                    selected_AppNames_Array.add(app_name[aDuties_Array]);
                    selected_appIcons_Array.add(app_icon[aDuties_Array]);
                }

                if (cadres_array.length > 0) {
                    lyt_Spot.setVisibility(View.VISIBLE);
                    img_spot.setImageResource(selected_appIcons_Array.get(0));
                    txt_Spot.setText(selected_AppNames_Array.get(0));
                    img_spot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedModule(selected_AppNames_Array.get(0));

                        }
                    });
                }

                if (cadres_array.length > 1) {
                    lyt_DD.setVisibility(View.VISIBLE);
                    img_DD.setImageResource(selected_appIcons_Array.get(1));
                    txt_DD.setText(selected_AppNames_Array.get(1));
                    img_DD.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedModule(selected_AppNames_Array.get(1));

                        }
                    });
                }

                if (cadres_array.length > 2) {
                    lyt_CpAct.setVisibility(View.VISIBLE);
                    img_cpAct.setImageResource(selected_appIcons_Array.get(2));
                    txt_cpAct.setText(selected_AppNames_Array.get(2));
                    img_cpAct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedModule(selected_AppNames_Array.get(2));

                        }
                    });
                }

                if (cadres_array.length > 3) {
                    rlyt_ghmc.setVisibility(View.VISIBLE);
                    img_GHMC.setImageResource(selected_appIcons_Array.get(3));
                    txt_GHMC.setText(selected_AppNames_Array.get(3));
                    img_GHMC.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedModule(selected_AppNames_Array.get(3));

                        }
                    });
                }

                if (cadres_array.length > 4) {
                    rlyt_szre.setVisibility(View.VISIBLE);
                    img_Seizre.setImageResource(selected_appIcons_Array.get(4));
                    txt_Seizure.setText(selected_AppNames_Array.get(4));
                    img_Seizre.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedModule(selected_AppNames_Array.get(4));

                        }
                    });
                }

                if (cadres_array.length > 5) {
                    rlyt_court.setVisibility(View.VISIBLE);
                    img_Court.setImageResource(selected_appIcons_Array.get(5));
                    txt_Court.setText(selected_AppNames_Array.get(5));
                    img_Court.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedModule(selected_AppNames_Array.get(5));
                        }
                    });
                }

                if (cadres_array.length > 6) {
                    lyt_RelseItms.setVisibility(View.VISIBLE);
                    img_relseItms.setImageResource(selected_appIcons_Array.get(6));
                    txt_relseItms.setText(selected_AppNames_Array.get(6));
                    img_relseItms.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedModule(selected_AppNames_Array.get(6));
                        }
                    });
                }

                if (cadres_array.length > 7) {
                    lyt_Reports.setVisibility(View.VISIBLE);
                    img_Reports.setImageResource(selected_appIcons_Array.get(7));
                    txt_Reports.setText(selected_AppNames_Array.get(7));
                    img_Reports.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedModule(selected_AppNames_Array.get(7));
                        }
                    });
                }

                if (cadres_array.length > 8) {
                    lyt_Settngs.setVisibility(View.VISIBLE);
                    img_Settngs.setImageResource(selected_appIcons_Array.get(8));
                    txt_Settngs.setText(selected_AppNames_Array.get(8));
                    img_Settngs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedModule(selected_AppNames_Array.get(8));
                        }
                    });
                }



            /* dashBDGridViewAdapter = new DashBDGridViewAdapter(this, selected_AppNames_Array, selected_appIcons_Array);
            gridView_dashBoard.setAdapter(dashBDGridViewAdapter); */

            } catch (Exception e) {
                e.printStackTrace();

            /* dashBDGridViewAdapter = new DashBDGridViewAdapter(this, selected_AppNames_Array, selected_appIcons_Array);
            gridView_dashBoard.setAdapter(dashBDGridViewAdapter); */
            }
        /*}else {
            Intent intent_settings=new Intent(getApplicationContext(),SettingsActivity.class);
            intent_settings.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent_settings);
        }*/
    }

    private void selectedModule(String module) {
        switch (module) {
            case "SPOT":
                showToast(module + "s");
                Intent intent_Spot=new Intent(getApplicationContext(),SpotChallanActivity.class);
                intent_Spot.putExtra("challanType","22");
                startActivity(intent_Spot);
                break;
            case "DRUNK & DRIVE":
                Intent intent_DD=new Intent(getApplicationContext(),DrunkDriveActivity.class);
                intent_DD.putExtra("challanType","23");
                startActivity(intent_DD);
                break;
            case "CRANE":
                Intent intent_Crane=new Intent(getApplicationContext(),SpotChallanActivity.class);
                intent_Crane.putExtra("challanType","24");
                startActivity(intent_Crane);
                break;
            case "SEIZURE":
                showToast(module);
                break;
            case "GHMC":
                showToast(module);
                break;
            case "RELEASE DOCUMENTS":
                showToast(module);
                break;
            case "REPORTS":
                showToast(module);
                break;
            case "SETTINGS":
                showToast(module);
                Intent intent_settings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent_settings);
                break;
            default:
                showToast(module);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_LONG).show();
    }

    private void initViews() {

        gridView_dashBoard = findViewById(R.id.gridView_dashBoard);

        lyt_Spot = findViewById(R.id.lyt_Spot);
        lyt_DD = findViewById(R.id.lyt_DD);
        lyt_CpAct = findViewById(R.id.lyt_cpAct);
        lyt_RelseItms = findViewById(R.id.lyt_RelseItms);
        lyt_Reports = findViewById(R.id.lyt_Reports);
        lyt_Settngs = findViewById(R.id.lyt_Settngs);
        rlyt_ghmc = findViewById(R.id.rlyt_ghmc);
        rlyt_szre = findViewById(R.id.rlyt_szre);
        rlyt_court = findViewById(R.id.rlyt_court);

        img_spot = findViewById(R.id.img_spot);
        img_DD = findViewById(R.id.img_DD);
        img_cpAct = findViewById(R.id.img_cpAct);
        img_GHMC = findViewById(R.id.img_GHMC);
        img_Seizre = findViewById(R.id.img_Seizre);
        img_Court = findViewById(R.id.img_Court);
        img_relseItms = findViewById(R.id.img_relseItms);
        img_Reports = findViewById(R.id.img_Reports);
        img_Settngs = findViewById(R.id.img_Settngs);

        txt_Spot = findViewById(R.id.txt_Spot);
        txt_DD = findViewById(R.id.txt_DD);
        txt_cpAct = findViewById(R.id.txt_cpAct);
        txt_GHMC = findViewById(R.id.txt_GHMC);
        txt_Seizure = findViewById(R.id.txt_Seizure);
        txt_Court = findViewById(R.id.txt_Court);
        txt_relseItms = findViewById(R.id.txt_relseItms);
        txt_Reports = findViewById(R.id.txt_Reports);
        txt_Settngs = findViewById(R.id.txt_Settngs);


    }
}
