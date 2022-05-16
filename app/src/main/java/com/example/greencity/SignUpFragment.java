package com.example.greencity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //private ImageButton closeBtn;
    private Button signup;
    private ProgressBar progressBar;
    //private FirebaseAuthException firebaseAuth;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
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

       // closeBtn = view.findViewById(R.id.close_btn);
        signup = view.findViewById(R.id.signUp);
        progressBar = view.findViewById(R.id.sign_up_progress);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

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

                checkInput();
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
                checkInput();
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
        Drawable customErrorIcon = getResources().getDrawable(R.drawable.warning);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());
        if(email.getText().toString().matches(emailpattern)){
            if(pass.getText().toString().equals(confirmpass.getText().toString())){

                progressBar.setVisibility(View.VISIBLE);
                signup.setEnabled(false);
                signup.setTextColor(Color.argb(50,255,255,255));

            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Map<String,Object> userdata = new HashMap<>();
                            userdata.put("name",name.getText().toString());

                            firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                    .set(userdata)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){

                                                CollectionReference userDataDeference =  firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");


                                                //Maps
                                                Map<String,Object> wishlistMap = new HashMap<>();
                                                wishlistMap.put("list_size",(long) 0);

                                                Map<String,Object> ratingsMap = new HashMap<>();
                                                ratingsMap.put("list_size",(long) 0);

                                                Map<String,Object> cartMap = new HashMap<>();
                                                cartMap.put("list_size",(long) 0);

                                                Map<String,Object> MyAddressesMap = new HashMap<>();
                                                MyAddressesMap.put("list_size",(long) 0);
                                                //Maps

                                                List<String> documentName = new ArrayList<>();
                                                documentName.add("MY_WISHLIST");
                                                documentName.add("MY_RATINGS");
                                                documentName.add("MY_CART");
                                                documentName.add("MY_ADDRESSES");


                                                List<Map<String,Object>> documentFields = new ArrayList<>();
                                                documentFields.add(wishlistMap);
                                                documentFields.add(ratingsMap);
                                                documentFields.add(cartMap);
                                                documentFields.add(MyAddressesMap);



                                                for (int x = 0;x<documentName.size();x++){

                                                    int finalX = x;
                                                    userDataDeference.document(documentName.get(x))
                                                            .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                if (finalX == documentName.size() -1) {
                                                                    mainIntent();
                                                                }
                                                            }else{
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                signup.setEnabled(true);
                                                                signup.setTextColor(Color.rgb(255,255,255));
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(getActivity(),"error", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }

                                            }else{

                                                String error = task.getException().getMessage();
                                                Toast.makeText(getActivity(),"error", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


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
                confirmpass.setError("Password doesn't matched!!!",customErrorIcon);
            }
        }else{
              email.setError("Invalid Email!!!",customErrorIcon);
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