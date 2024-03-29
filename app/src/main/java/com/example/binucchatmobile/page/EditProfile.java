package com.example.binucchatmobile.page;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.binucchatmobile.R;
import com.example.binucchatmobile.model.UserModel;
import com.example.binucchatmobile.utils.AndroidUtil;
import com.example.binucchatmobile.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class EditProfile extends AppCompatActivity {
    ImageView profile_imgView, backEditProf;
    EditText Profile_UsernameInput;
    Button updateProfileBtn;
    UserModel currentUserModel;
    ActivityResultLauncher<Intent> imgPickerLaunch;
    Uri selectedImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        profile_imgView = findViewById(R.id.profile_imgView);
        backEditProf = findViewById(R.id.backEditProf);
        Profile_UsernameInput = findViewById(R.id.Profile_UsernameInput);
        updateProfileBtn = findViewById(R.id.updateProfileBtn);

        updateProfileBtn.setOnClickListener((v -> {
            updateBtnClick();
        }));

        getUserData();

        backEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBackProfile = new Intent(EditProfile.this, Profile_SettingPage.class);
                startActivity(intentBackProfile);
            }
        });

        profile_imgView.setOnClickListener((v -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imgPickerLaunch.launch(intent);
                            return null;
                        }
                    });
        }));

        imgPickerLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result ->{
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null && data.getData() != null){
                            selectedImg = data.getData();
                            AndroidUtil.setProfPic(this, selectedImg, profile_imgView);
                        }
                    }
                }
        );
    }

    void updateBtnClick(){
        String newUsername = Profile_UsernameInput.getText().toString();
        if(newUsername.isEmpty() || newUsername.length() < 3){
            Profile_UsernameInput.setError("Username length should be at least 3 characters");
            return;
        }
        currentUserModel.setUsername(newUsername);

        if(selectedImg != null) {
            FirebaseUtil.getCurrProfPicStorage().putFile(selectedImg)
                    .addOnCompleteListener(task -> {
                        updateToFireStore();
                    });
        }else{
            updateToFireStore();
        }

    }

    void updateToFireStore(){
        FirebaseUtil.currentUserDetails().set(currentUserModel)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(EditProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(EditProfile.this, "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void getUserData(){

        FirebaseUtil.getCurrProfPicStorage().getDownloadUrl()
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Uri uri = task.getResult();
                                AndroidUtil.setProfPic(this, uri, profile_imgView);
                            }
                        });

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            currentUserModel = task.getResult().toObject(UserModel.class);
            Profile_UsernameInput.setText(currentUserModel.getUsername());
        });
    }
}