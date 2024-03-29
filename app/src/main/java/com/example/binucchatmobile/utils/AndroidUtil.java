package com.example.binucchatmobile.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.binucchatmobile.model.UserModel;

public class AndroidUtil {

   public static  void showToast(Context context,String message){
       Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserModel model){
       intent.putExtra("username",model.getUsername());
       intent.putExtra("nim",model.getNim());
       intent.putExtra("userId",model.getUserId());
    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setNim(intent.getStringExtra("nim"));
        userModel.setUserId(intent.getStringExtra("userId"));
        return userModel;
    }

    public static void setProfPic(Context context, Uri imageUri, ImageView imageView){
       Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
