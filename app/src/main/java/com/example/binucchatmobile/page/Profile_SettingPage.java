package com.example.binucchatmobile.page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.binucchatmobile.R;
import com.example.binucchatmobile.model.UserModel;
import com.example.binucchatmobile.utils.FirebaseUtil;

public class Profile_SettingPage extends AppCompatActivity {
    ImageView back_profile_btn;
    TextView Profile_name, Profile_binusianid, Profile_email, Profile_nim;
    Button resetpassBtn, editProfileBtn;
    UserModel currentUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting_page);

        back_profile_btn = findViewById(R.id.back_profile_btn);
        Profile_name = findViewById(R.id.Profile_name);
        Profile_binusianid = findViewById(R.id.Profile_binusianid);
        Profile_email = findViewById(R.id.Profile_email);
        Profile_nim = findViewById(R.id.Profile_nim);
        resetpassBtn = findViewById(R.id.resetpassBtn);
        editProfileBtn = findViewById(R.id.editProfileBtn);

        getUserData();

        resetpassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ResetPassword = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mail.exchange.microsoft.com/"));
                startActivity(ResetPassword);
            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EditProfile = new Intent(Profile_SettingPage.this, EditProfile.class);
                startActivity(EditProfile);
            }
        });

        back_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBackProfile = new Intent(Profile_SettingPage.this, setting_page.class);
                startActivity(intentBackProfile);
            }
        });
    }
    void getUserData(){
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            currentUserModel = task.getResult().toObject(UserModel.class);
            Profile_name.setText(currentUserModel.getUsername());
            Profile_nim.setText(currentUserModel.getNim());
            Profile_email.setText(currentUserModel.getEmail());
            Profile_binusianid.setText(currentUserModel.getBinusianId());
        });
    }
}