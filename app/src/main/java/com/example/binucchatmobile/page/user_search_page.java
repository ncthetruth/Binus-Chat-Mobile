package com.example.binucchatmobile.page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.binucchatmobile.R;
import com.example.binucchatmobile.adapter.SearchUserRecyclerAdapter;
import com.example.binucchatmobile.model.UserModel;
import com.example.binucchatmobile.utils.AndroidUtil;
import com.example.binucchatmobile.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class user_search_page extends AppCompatActivity {
    EditText user_search_bar;
    ImageView nav_chatlist_search, nav_profile_search;
    RecyclerView user_recyclerview;
    SearchUserRecyclerAdapter Useradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_page);

        user_search_bar = findViewById(R.id.user_search_bar);
        nav_chatlist_search = findViewById(R.id.nav_chatlist_search);
        nav_profile_search = findViewById(R.id.nav_profile_search);
        user_recyclerview = findViewById(R.id.user_recyclerview);
        user_search_bar.requestFocus();

        FirebaseUtil.getCurrProfPicStorage().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        AndroidUtil.setProfPic(this, uri, nav_profile_search);
                    }
                });

        user_search_bar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchTerm = user_search_bar.getText().toString().trim();
                if (searchTerm.isEmpty() || searchTerm.length() < 3) {
                    user_search_bar.setError("Invalid Username");
                    return true;
                }
                setupSearchRecyclerView(searchTerm);
                return true;
            }
            return false;
        });

        nav_chatlist_search.setOnClickListener(v -> {
            Intent intentSearch = new Intent(user_search_page.this, home_page.class);
            startActivity(intentSearch);
        });

        nav_profile_search.setOnClickListener(v -> {
            Intent intentProfile = new Intent(user_search_page.this, setting_page.class);
            startActivity(intentProfile);
        });


    }

    void setupSearchRecyclerView(String searchTerm){

        Query query = FirebaseUtil.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("nim",searchTerm)
                .whereLessThanOrEqualTo("nim",searchTerm+'\uf8ff');

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class).build();

        Useradapter = new SearchUserRecyclerAdapter(options,getApplicationContext());
        user_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        user_recyclerview.setAdapter(Useradapter);
        Useradapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Useradapter != null)
            Useradapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Useradapter != null)
            Useradapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Useradapter != null)
            Useradapter.startListening();
    }
}



