package com.example.greencity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MyAccountFragment extends Fragment {


    public MyAccountFragment() {
        // Required empty public constructor
    }

    private Button viewAllAddressBtn;
    public static final int MANAGE_ADDRESS = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_my_account, container, false);
    View view = inflater.inflate(R.layout.fragment_my_account,container,false);

    viewAllAddressBtn = view.findViewById(R.id.view_all_address_btn);

    viewAllAddressBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent myAddressesIntent = new Intent(getContext(),MyAddressActivity.class);
            myAddressesIntent.putExtra("MODE",MANAGE_ADDRESS);
            startActivity(myAddressesIntent);
        }
    });

    return view;
    }
}