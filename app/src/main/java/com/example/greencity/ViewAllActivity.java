package com.example.greencity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;
//import android.widget.Toolbar;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Deals of the day");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        gridView.findViewById(R.id.grid_view);

        int layout_code = getIntent().getIntExtra("layout_code",-1);

        if (layout_code == 0) {

            recyclerView.setVisibility(View.VISIBLE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            List<WishlistModel> wishlistModelList = new ArrayList<>();
            wishlistModelList.add(new WishlistModel(R.drawable.can, "Coke Can", 1, "2", 7, "120/-", "150/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.can, "Coke Can", 0, "3", 9, "110/-", "130/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.can, "Coke Can", 2, "4", 10, "130/-", "140/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.can, "Coke Can", 1, "2", 15, "120/-", "150/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.can, "Coke Can", 3, "2", 7, "100/-", "150/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.can, "Coke Can", 1, "2", 7, "120/-", "150/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.can, "Coke Can", 0, "3", 9, "110/-", "130/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.can, "Coke Can", 2, "4", 10, "130/-", "140/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.can, "Coke Can", 1, "2", 15, "120/-", "150/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.can, "Coke Can", 3, "2", 7, "100/-", "150/-", "Cash on delivery"));

            WishlistAdapter adapter = new WishlistAdapter(wishlistModelList, false);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else if (layout_code == 1) {


            gridView.setVisibility(View.VISIBLE);
            List<HorizontalProductModel> horizontalProductModelList = new ArrayList<>();
            horizontalProductModelList.add(new HorizontalProductModel(R.drawable.can, "list2", "newDescsc", "RS.1234"));
            horizontalProductModelList.add(new HorizontalProductModel(R.drawable.can, "list2", "newDescsc", "RS.1234"));
            horizontalProductModelList.add(new HorizontalProductModel(R.drawable.can, "list2", "newDescsc", "RS.1234"));
            horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ktm, "list2", "newDescsc", "RS.1234"));
            horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ic_email, "list2", "newDescsc", "RS.1234"));
            horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ktm, "list2", "newDescsc", "RS.1234"));
            horizontalProductModelList.add(new HorizontalProductModel(R.drawable.can, "list2", "newDescsc", "RS.1234"));
            horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ktm, "list2", "newDescsc", "RS.1234"));
            horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ic_email, "list2", "newDescsc", "RS.1234"));
            horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ktm, "list2", "newDescsc", "RS.1234"));

            GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductModelList);
            gridView.setAdapter(gridProductLayoutAdapter);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}