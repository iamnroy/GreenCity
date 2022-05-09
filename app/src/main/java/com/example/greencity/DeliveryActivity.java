package com.example.greencity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.example.greencity.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {

    private RecyclerView deliveryRecyclerView;
    private Button changedOrAddNewAddBtn;
    public static final int SELECT_ADDRESS =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

       androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        //toolbar = findViewById(R.id.toolbar);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
       // setContentView(binding.getRoot());
        //Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        deliveryRecyclerView =findViewById(R.id.delivery_recycler_view);
        changedOrAddNewAddBtn = findViewById(R.id.change_or_add_address_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);

        List<CartitemModel> cartitemModelList = new ArrayList<>();
        cartitemModelList.add(new CartitemModel(0,R.drawable.can,"Can",2,"Rs.3999/-","Rs.4999/-",1,0,0));
        cartitemModelList.add(new CartitemModel(0,R.drawable.can,"Can",0,"Rs.3999/-","Rs.4999/-",1,1,0));
        cartitemModelList.add(new CartitemModel(0,R.drawable.can,"Can",2,"Rs.3999/-","Rs.4999/-",1,2,0));
        cartitemModelList.add(new CartitemModel(1,"Price (2 items)","Rs.5999/-","Free","Rs.5999/-","Rs.2999/-"));


        CartAdapter cartAdapter = new CartAdapter(cartitemModelList);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changedOrAddNewAddBtn.setVisibility(View.VISIBLE);

        changedOrAddNewAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myAddressesIntent = new Intent(DeliveryActivity.this,MyAddressActivity.class);
                myAddressesIntent.putExtra("MODE",SELECT_ADDRESS);
                startActivity(myAddressesIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}