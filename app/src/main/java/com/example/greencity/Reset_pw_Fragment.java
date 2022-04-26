package com.example.greencity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Reset_pw_Fragment extends Fragment {

    public Reset_pw_Fragment() {
        // Required empty public constructor
    }

    private EditText reg_email;
    private Button reset_button;
    private TextView goback;
    private FrameLayout parentFrameLayout;

    private ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView emailIconText;
    private ProgressBar progressbar;

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_pw_, container, false);

         reg_email = view.findViewById(R.id.forgotpass_email);
         reset_button = view.findViewById(R.id.reset_pw_btn);
         goback = view.findViewById(R.id.forgot_goback);

         emailIconContainer = view.findViewById(R.id.forgot_pw_email_icon);
         emailIconText = view.findViewById(R.id.forgot_pw_email_text);
         progressbar = view.findViewById(R.id.forgot_pw_prgress);

         parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);
         firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reg_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkfield();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIconText.setVisibility(View.GONE);
                //TransitionManager.beginDelayedTransition(emailIconContainer);

//                emailIcon.setVisibility(View.VISIBLE);
//                progressbar.setVisibility(View.VISIBLE);


                reset_button.setEnabled(false);
                reset_button.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.sendPasswordResetEmail(reg_email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                   Toast.makeText(getActivity(), "Email Sent Successfully!", Toast.LENGTH_SHORT).show();
                                    //ScaleAnimation scaleAnimation = new ScaleAnimation()

                                }else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(),error, Toast.LENGTH_SHORT).show();
//                                    progressbar.setVisibility(View.GONE);
//                                    emailIconText.setText(error);
//                                    emailIconText.setTextColor(getResources().getColor(R.color.red));
//                                    TransitionManager.beginDelayedTransition(emailIconContainer);
//                                    emailIconText.setVisibility(View.VISIBLE);


                                }
                                reset_button.setEnabled(true);
                                reset_button.setTextColor(Color.rgb(255,255,255));
                            }
                        });
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });
    }
    private void checkfield(){
        if (TextUtils.isEmpty(reg_email.getText())){
            reset_button.setEnabled(false);
            reset_button.setTextColor(Color.argb(50,255,255,255));

        }else {
            reset_button.setEnabled(true);
            reset_button.setTextColor(Color.rgb(255,255,255));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.left_side,R.anim.slide_out);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}