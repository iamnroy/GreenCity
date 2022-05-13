package com.example.greencity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {


    public SignUpFragment() {
        // Required empty public constructor
    }

    private TextView alreadyAmember;
    private FrameLayout parentFrameLayout;

    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText pass;
    private EditText confirmpass;

    //private ImageButton close;
    private Button signup;
    private ProgressBar progressBar;
    //private FirebaseAuthException firebaseAuth;
    private FirebaseAuth firebaseAuth;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableCloseBtn = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);
        alreadyAmember = view.findViewById(R.id.already_a_member);

        name = view.findViewById(R.id.register_name);
        email = view.findViewById(R.id.register_email);
        phone = view.findViewById(R.id.register_phone);
        pass = view.findViewById(R.id.register_pass);
        confirmpass = view.findViewById(R.id.register_confirm_pass);

        //close = view.findViewById(R.id.close_btn);
        signup = view.findViewById(R.id.signUp);
        progressBar = view.findViewById(R.id.sign_up_progress);

        firebaseAuth = FirebaseAuth.getInstance();

//        if (disableCloseBtn == true) {
//            closeBtn.setVisibility(View.GONE);
//        }else {
//            closeBtn.setVisibility(View.VISIBLE);
//        }

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyAmember.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFragment(new SignInFragment());

            }
        });

       name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
      });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             checkEmailAndPass();
            }
        });
    }

            private void setFragment(Fragment fragment) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.left_side,R.anim.slide_out);
                fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
                fragmentTransaction.commit();
            }


    //});
    private void checkInput(){
        if(!TextUtils.isEmpty(name.getText())){
            if(!TextUtils.isEmpty(email.getText())){
                if(!TextUtils.isEmpty(phone.getText()) && phone.length() == 10) {
                    if(!TextUtils.isEmpty(pass.getText()) && pass.length() >= 8){
                        if(!TextUtils.isEmpty(confirmpass.getText())){
                            signup.setEnabled(true);
                            signup.setTextColor(Color.rgb(255,255,255));
                        }else{
                            signup.setEnabled(false);
                            signup.setTextColor(Color.argb(50,255,255,255));
                        }
                    }else{
                        signup.setEnabled(false);
                        signup.setTextColor(Color.argb(50,255,255,255));
                    }
                }else{
                    signup.setEnabled(false);
                    signup.setTextColor(Color.argb(50,255,255,255));
                }
        }else{

                signup.setEnabled(false);
                signup.setTextColor(Color.argb(50,255,255,255));
        }
        }else {
            signup.setEnabled(false);
            signup.setTextColor(Color.argb(50,255,255,255));
        }
    }
    private void checkEmailAndPass(){
        if(email.getText().toString().matches(emailpattern)){
            if(pass.getText().equals(confirmpass.getText())){

                progressBar.setVisibility(View.VISIBLE);
                signup.setEnabled(false);
                signup.setTextColor(Color.argb(50,255,255,255));

            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent mainIntent = new Intent(getActivity(),MainActivity.class);
                            startActivity(mainIntent);
                            getActivity().finish();

                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            signup.setEnabled(true);
                            signup.setTextColor(Color.rgb(255,255,255));
                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(),"error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }else{
                confirmpass.setError("Password doesn't matched!!!");
            }
        }else{
              email.setError("Invalid Email");
        }
    }

    private void mainIntent(){
        if (disableCloseBtn){
            disableCloseBtn = false;
        }else {
            //Intent mainIntent = new Intent(getActivity(),splash.class);
            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(mainIntent);
        }
        getActivity().finish();
    }
    }
//}