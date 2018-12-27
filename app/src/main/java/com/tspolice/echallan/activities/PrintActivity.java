package com.tspolice.echallan.activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.tspolice.echallan.R;
import com.tspolice.echallan.models.SettingsModel;
import com.tspolice.echallan.utils.SharedPrefsHelper;
import com.tspolice.echallan.utils.Toaster;

/**
 * Created by Srinivas on 12/1/2018.
 */

public class PrintActivity extends AppCompatActivity {

    BluetoothAdapter bluetooth_Adapter = null;

    final AnalogicsThermalPrinter at_printer = new AnalogicsThermalPrinter();

    SettingsModel settingsModel;

    AppCompatTextView txt_printMsg;

    AppCompatButton btn_Cancel, btn_Print;

    String str_printMsg = "", printer_Adress = "";

    private int REQUEST_ENABLE_BT = 1;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        initviews();
        settingsModel = new SettingsModel();
        settingsModel = SharedPrefsHelper.getSettinsFromPref(getApplicationContext(), "mPreference", "mSettingsInfo", SettingsModel.class);
        if (settingsModel != null) {
            printer_Adress = settingsModel.getStr_BluTHAdres();
        }
        progressDialog = new ProgressDialog(this);
        bluetooth_Adapter = BluetoothAdapter.getDefaultAdapter();
        checkBlueTooth_ActiveState();
        registerReceiver(null, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        Intent intent = getIntent();
        str_printMsg = intent.getStringExtra("printInfo");
        if (!str_printMsg.equals("")) {
            txt_printMsg.setText(str_printMsg);
        }
        btn_Print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async_Print().execute();
            }
        });

    }

    private class Async_Print extends AsyncTask<Void, Void, String> {
        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog.setMessage("Please Wait \n Printing the Challan");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            if (bluetooth_Adapter.isEnabled()) {
                if (printer_Adress.equals("") || printer_Adress == null) {
                    Toaster.longToast("Please set bluetooth address in settings");
                } else {
                    try {
                        Bluetooth_Printer_3inch_ThermalAPI printer = new Bluetooth_Printer_3inch_ThermalAPI();
                        String print_data = printer.font_Courier_41("" + str_printMsg);
                        at_printer.openBT(printer_Adress);
                        at_printer.printData(print_data);
                        Thread.sleep(5000);
                        at_printer.closeBT();
                    } catch (Exception e) {
                        // TODO: handle exception
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toaster.longToast("Please set bluetooth Address in Setting");
                            }
                        });
                    }
                }
            } else {
                Toaster.longToast("Please Enable Bluetooth!");
            }

            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
        }

    }

    private void initviews() {
        txt_printMsg = findViewById(R.id.txt_printMsg);
        btn_Cancel = findViewById(R.id.btn_Cancel);
        btn_Print = findViewById(R.id.btn_Print);
    }

    private void checkBlueTooth_ActiveState() {
        if (bluetooth_Adapter == null) {
            bluetooth_Adapter = BluetoothAdapter.getDefaultAdapter();
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

}
