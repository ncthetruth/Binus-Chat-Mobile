package com.example.binucchatmobile.page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.binucchatmobile.R;

public class Support_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_page);

        ImageView back_support_btn = findViewById(R.id.back_support_btn);

        back_support_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBackSupport = new Intent(Support_page.this, setting_page.class);
                startActivity(intentBackSupport);
            }
        });
    }
}