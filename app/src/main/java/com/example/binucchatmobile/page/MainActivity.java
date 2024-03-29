package com.example.binucchatmobile.page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.binucchatmobile.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button loginBtn;
    EditText Emailinput, PasswordInput;
    private FirebaseAuth fireAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fireAuth = FirebaseAuth.getInstance();

        loginBtn = findViewById(R.id.loginBtn);
        Emailinput = findViewById(R.id.Emailinput);
        PasswordInput = findViewById(R.id.PasswordInput);

        loginBtn.setOnClickListener(v -> {
            if (Emailinput.getText().length() >= 8 && PasswordInput.getText().length() >= 8) {
                login(Emailinput.getText().toString(), PasswordInput.getText().toString());
            }else if(Emailinput.getText().length() > 0 && PasswordInput.getText().length() > 0 ){
                Toast.makeText(getApplicationContext(), "Minimum Email/Password 8 Character", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Silahkan masukan semua data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(String email, String password){
        fireAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().getUser() != null) {
                reload();
            } else {
                Toast.makeText(getApplicationContext(), "Login gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reload() {
        startActivity(new Intent(getApplicationContext(), home_page.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = fireAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }
}