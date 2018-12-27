package com.tspolice.echallan.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.tspolice.echallan.R;

public class MainActivity extends AppCompatActivity {
    AppCompatEditText eT_Cadres;

    AppCompatButton btn_Save;

    public static String str_Maincadres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eT_Cadres=findViewById(R.id.eT_cadres);
        btn_Save=findViewById(R.id.btn_Save);
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_Maincadres=eT_Cadres.getText().toString();
                Intent intent_Login=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent_Login);
            }
        });
    }
}
