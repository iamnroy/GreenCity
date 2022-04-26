package com.example.greencity;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                sleep (3000);
                    Intent registerIntent = new Intent(splash.this,Register.class);
                    startActivity(registerIntent);
                    finish();

            }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }
        });
        thread.start();
    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        if (currentUser == null){
//            Intent registerIntent = new Intent(splash.this,Register.class);
//            startActivity(registerIntent);
//            finish();
//        }else {
//            Intent mainIntent = new Intent(splash.this,MainActivity.class);
//            startActivity(mainIntent);
//            finish();
//        }
    }
}