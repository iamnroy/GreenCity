package com.example.greencity;

import static com.example.greencity.Register.onResetPasswordFragment;

import android.content.Intent;
import android.graphics.Color;
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

/**
 * A simple {@link Fragment} subclass.

 */
public class SignInFragment extends Fragment {


    public SignInFragment() {
        // Required empty public constructor
    }
    private TextView creAcc;
    private FrameLayout parentFrameLayout;
    private TextView forgotpassword;
    private EditText email;
    private EditText password;

    private ProgressBar progressBar;

    private ImageButton closeBtn;
    private Button signInbtn;

    private FirebaseAuth firebaseAuth;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableCloseBtn = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);
        creAcc = view.findViewById(R.id.create_acc);
        //forgotpassword = getActivity().findViewById(R.id.reset_pass);
        forgotpassword = view.findViewById(R.id.reset_pass);
      closeBtn = view.findViewById(R.id.close_btn_sign);
        email = view.findViewById(R.id.email_text);
        password = view.findViewById(R.id.signIn_pass);

        progressBar = view.findViewById(R.id.sign_in_progress);
        //closeBtn = view.findViewById(R.id.close_btn_sign);
        signInbtn = view.findViewById(R.id.log_button);

       firebaseAuth = FirebaseAuth.getInstance();

        if (disableCloseBtn == true) {
            closeBtn.setVisibility(View.GONE);
        }else {
            closeBtn.setVisibility(View.VISIBLE);
        }

    return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        creAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mainIntent();
                startActivity(new Intent(getActivity(),MainActivity.class));
                //startActivity(new Intent(getActivity(),ProductDetails.class));
                getActivity().finish();
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onResetPasswordFragment = true;
                setFragment(new Reset_pw_Fragment());
            }
        });
        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailandPass();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.sliding,R.anim.leftout_slide);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(password.getText())) {
                signInbtn.setEnabled(true);
                signInbtn.setTextColor(Color.rgb(255,255,255));

            }else{
                signInbtn.setEnabled(false);
                signInbtn.setTextColor(Color.argb(50,255,255,255));
                //signInbtn.setTextColor(Color.argb(10,172,248,164));

            }

        }else {
            signInbtn.setEnabled(false);
            signInbtn.setTextColor(Color.argb(50,255,255,255));
            //signInbtn.setTextColor(Color.argb(10,172,248,164));

        }
    }
    private void checkEmailandPass() {
            if (email.getText().toString().matches(emailpattern)){
                if (password.length() >= 8){

                    progressBar.setVisibility(View.VISIBLE);
                    signInbtn.setEnabled(false);
                    //signInbtn.setTextColor(Color.argb(10,172,248,164));
                    signInbtn.setTextColor(Color.argb(50,255,255,255));


                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                   .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                               // Intent mainIntent = new Intent(getActivity(),splash.class);
                            Intent mainIntent = new Intent(getActivity(),MainActivity.class);
                            startActivity(mainIntent);
                                getActivity().finish();
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            signInbtn.setEnabled(true);
                            signInbtn.setTextColor(Color.rgb(255,255,255));
                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                        }
                       }
                   });

                }else {
                    Toast.makeText(getActivity(), "Incorrect Email or Password!", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getActivity(), "Incorrect Email or Password!", Toast.LENGTH_SHORT).show();

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