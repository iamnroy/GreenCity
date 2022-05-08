package com.example.greencity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MyWishlistFragment extends Fragment {


    public MyWishlistFragment() {
        // Required empty public constructor
    }

    private RecyclerView wishlistRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_wishlist, container, false);

        wishlistRecyclerView = view.findViewById(R.id.my_wishlist_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);

        List<WishlistModel> wishlistModelList = new ArrayList<>();
        wishlistModelList.add(new WishlistModel(R.drawable.can,"Coke Can",1,"2",7,"120/-","150/-","Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.can,"Coke Can",0,"3",9,"110/-","130/-","Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.can,"Coke Can",2,"4",10,"130/-","140/-","Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.can,"Coke Can",1,"2",15,"120/-","150/-","Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.can,"Coke Can",3,"2",7,"100/-","150/-","Cash on delivery"));

        WishlistAdapter wishlistAdapter = new WishlistAdapter(wishlistModelList);
        wishlistRecyclerView.setAdapter(wishlistAdapter);
        wishlistAdapter.notifyDataSetChanged();


        return view;
    }
}