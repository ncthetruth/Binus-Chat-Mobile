package com.example.binucchatmobile.page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.binucchatmobile.R;
import com.example.binucchatmobile.model.UserModel;
import com.example.binucchatmobile.utils.AndroidUtil;
import com.example.binucchatmobile.utils.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;


public class setting_page extends AppCompatActivity {
    Button profile_setting_btn, support_setting_btn, logout_setting_btn;
    ImageView nav_chatlist_profile, nav_search_profile, nav_profile_profile;
    TextView UserProfileName, UserProfileNIM;
    CircleImageView navbarProfile;
    UserModel currentUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        profile_setting_btn = findViewById(R.id.profile_setting_btn);
        support_setting_btn = findViewById(R.id.support_setting_btn);
        logout_setting_btn = findViewById(R.id.logout_setting_btn);
        nav_chatlist_profile = findViewById(R.id.nav_chatlist_profile);
        nav_search_profile =  findViewById(R.id.nav_search_profile);
        nav_profile_profile = findViewById(R.id.nav_profile_profile);
        UserProfileName = findViewById(R.id.UserProfileName);
        UserProfileNIM = findViewById(R.id.UserProfileNIM);
        navbarProfile = findViewById(R.id.navbarProfile);
        getUserData();



        profile_setting_btn.setOnClickListener(v -> {
            Intent intentProfileSetting = new Intent(setting_page.this, Profile_SettingPage.class);
            startActivity(intentProfileSetting);
        });
        support_setting_btn.setOnClickListener(v -> {
            Intent intentSupportSetting = new Intent(setting_page.this, Support_page.class);
            startActivity(intentSupportSetting);
        });
        logout_setting_btn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
        nav_chatlist_profile.setOnClickListener(v -> {
            Intent intentSearch = new Intent(setting_page.this, home_page.class);
            startActivity(intentSearch);
        });
        nav_search_profile.setOnClickListener(v -> {
            Intent intentSearch = new Intent(setting_page.this, user_search_page.class);
            startActivity(intentSearch);
        });

        FirebaseUtil.getCurrProfPicStorage().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        AndroidUtil.setProfPic(this, uri, navbarProfile);
                        AndroidUtil.setProfPic(this, uri, nav_profile_profile);
                    }
                });
    }
    void getUserData(){
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            currentUserModel = task.getResult().toObject(UserModel.class);
            UserProfileName.setText(currentUserModel.getUsername());
            UserProfileNIM.setText(currentUserModel.getNim());
        });

    }


}