package com.example.greencity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class Register extends AppCompatActivity {
    private FrameLayout frameLayout;
    public static boolean onResetPasswordFragment = false;
    public static Boolean setSignUpFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        frameLayout = findViewById(R.id.register_framelayout);

        if (setSignUpFragment){
            setSignUpFragment = false;
            setFragment(new SignUpFragment());
        }else {
            setDefaultFragment(new SignInFragment());
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){

            SignInFragment.disableCloseBtn = false;
            SignUpFragment.disableCloseBtn = false;

            if (onResetPasswordFragment){
                onResetPasswordFragment = false;
                setFragment(new SignInFragment());
                return false;
            }
        }
            return super.onKeyDown(keyCode, event);
    }

    private void setDefaultFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.left_side,R.anim.slide_out);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}