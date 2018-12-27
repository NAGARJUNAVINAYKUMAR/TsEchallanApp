package com.tspolice.echallan.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.tspolice.echallan.R;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


public class DialogUtils {

    public static KProgressHUD kProgressHUD;

    @SuppressLint("StaticFieldLeak")
    private static PrettyDialog pDialog;

    public static ProgressDialog progressDialog;

    public static ProgressDialog getProgressDialog(Context context, String text) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(text);
        return progressDialog;
    }

    public static Dialog createDialog(Context context,
                                      @StringRes int titleId, @StringRes int messageId, View view,
                                      DialogInterface.OnClickListener positiveClickListener,
                                      DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleId);
        builder.setMessage(messageId);
        builder.setView(view);
        builder.setPositiveButton("OK", positiveClickListener);
        builder.setNegativeButton("CANCEL", negativeClickListener);

        return builder.create();
    }


    public static void showBTProgressDialog(Context context) {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait \\n BlueTooth Scaning is in Process!!!")
                .setGraceTime(9000)
                .setWindowColor(context.getResources().getColor(R.color.headerBG_clr))
                .setAnimationSpeed(2);

    }

    public static void showProgressDialog(Context context) {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();


    }

    public static void dismissDialog() {
        kProgressHUD.dismiss();
    }

    public static void showAlertDlg(Context context, String sTitle, String sMessage) {
        pDialog = new PrettyDialog(context);
        pDialog
                .setTitle(sTitle)
                .setMessage(sMessage)
                .addButton(
                        "OK",
                        R.color.pdlg_color_white,
                        R.color.pdlg_color_red,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                pDialog.dismiss();
                            }
                        }
                )
                .show();
    }

}