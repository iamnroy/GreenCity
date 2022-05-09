package com.example.greencity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MyCartFragment extends Fragment {


    public MyCartFragment() {
        // Required empty public constructor
    }

   private RecyclerView cartItemsRecyclerView;
    private Button btnContinue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        cartItemsRecyclerView = view.findViewById(R.id.cart_items_recycler_view);
        btnContinue = view.findViewById(R.id.cart_continue_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(layoutManager);

        List<CartitemModel> cartitemModelList = new ArrayList<>();
        cartitemModelList.add(new CartitemModel(0,R.drawable.can,"Can",2,"Rs.3999/-","Rs.4999/-",1,0,0));
        cartitemModelList.add(new CartitemModel(0,R.drawable.can,"Can",0,"Rs.3999/-","Rs.4999/-",1,1,0));
        cartitemModelList.add(new CartitemModel(0,R.drawable.can,"Can",2,"Rs.3999/-","Rs.4999/-",1,2,0));
        cartitemModelList.add(new CartitemModel(1,"Price (2 items)","Rs.5999/-","Free","Rs.5999/-","Rs.2999/-"));


        CartAdapter cartAdapter = new CartAdapter(cartitemModelList);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent deliveryIntent = new Intent(getContext(),DeliveryActivity.class);
                Intent deliveryIntent = new Intent(getContext(),AddressActivity.class);
                getContext().startActivity(deliveryIntent);
            }
        });
        return view;
    }
}