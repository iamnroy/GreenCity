package com.example.greencity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.greencity.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity {

    public static List<CartitemModel> cartitemModelList;
    public static CartAdapter cartAdapter;
    private RecyclerView deliveryRecyclerView;
    private Button changedOrAddNewAddBtn;
    public static final int SELECT_ADDRESS = 0;
    private TextView totalAmount;
    private TextView fullname;
    private String name,mobileNo;
    private TextView fullAddress;
    private TextView pincode;
    private Button continueBtn;
    private Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private ImageButton wallet;
    private ImageButton codBtn;
    private ConstraintLayout orderConfirmationLayout;
    private ImageButton continueBuyingBtn;
    private TextView orderId;
    private boolean successResponse = false;
    public static boolean fromCart;

    private FirebaseFirestore firebaseFirestore;
    public static boolean allProductsAvailable;
    public static boolean getQtyIDs = true;

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
        totalAmount = findViewById(R.id.total_cart_amount);
        fullname = findViewById(R.id.full_name);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pin_code);
        continueBtn = findViewById(R.id.cart_continue_btn);
        orderConfirmationLayout = findViewById(R.id.order_confirmation_layout);
        continueBuyingBtn = findViewById(R.id.continue_buyingBtn);
        orderId = findViewById(R.id.order_id);

        //Loading Dialog
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //Loading Dialog

        //Payment Dialog
        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        wallet =paymentMethodDialog.findViewById(R.id.digitalwallet);
        codBtn = paymentMethodDialog.findViewById(R.id.cod_btn);
        //Payment Dialog
        firebaseFirestore = FirebaseFirestore.getInstance();
        getQtyIDs = true;
        allProductsAvailable = true;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);


        cartAdapter = new CartAdapter(cartitemModelList,totalAmount,false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changedOrAddNewAddBtn.setVisibility(View.VISIBLE);

        changedOrAddNewAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getQtyIDs = false;
                Intent myAddressesIntent = new Intent(DeliveryActivity.this,MyAddressActivity.class);
                myAddressesIntent.putExtra("MODE",SELECT_ADDRESS);
                startActivity(myAddressesIntent);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allProductsAvailable) {
                    paymentMethodDialog.show();
                }else {
                    /////////
                }

            }
        });


        codBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getQtyIDs = false;
                Intent otpIntent = new Intent(DeliveryActivity.this,OTPverificationActivity.class);
                otpIntent.putExtra("mobileNo",mobileNo.substring(0,10));
                startActivity(otpIntent);
            }
        });

//        wallet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                paymentMethodDialog.dismiss();
//                loadingDialog.show();
//
//            }
//        });



    }

//    @Override
//    public void onBackPressed() {
//
//        if (successResponse){
//            finish();
//            return;
//        }
//        super.onBackPressed();
//    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getQtyIDs) {
            //accessing quantity
            for (int x = 0; x < cartitemModelList.size() - 1; x++) {

               for (int y = 0;y < cartitemModelList.get(x).getProductQuantity();y++){
                   String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);

                   Map<String,Object> timeStamp = new HashMap<>();
                   timeStamp.put("time", FieldValue.serverTimestamp());
                   int finalX = x;
                   int finalY = y;
                   firebaseFirestore.collection("PRODUCTS").document(cartitemModelList.get(x).getProductID()).collection("QUANTITY").document(quantityDocumentName).set(timeStamp)
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {
                                  cartitemModelList.get(finalX).getQtyIDs().add(quantityDocumentName);

                                  if (finalY + 1 == cartitemModelList.get(finalY).getProductQuantity()){

                                      firebaseFirestore.collection("PRODUCTS").document(cartitemModelList.get(finalX).getProductID()).collection("QUANTITY").orderBy("time",Query.Direction.ASCENDING).limit(cartitemModelList.get(finalX).getStockQuantity()).get()
                                              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                      if (task.isSuccessful()){

                                                          List<String> serverQuantity = new ArrayList<>();
                                                          for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){

                                                              serverQuantity.add(queryDocumentSnapshot.getId());
                                                          }

                                                          long availableQty = 0;
                                                          boolean noLongerAvailable = true;
                                                          for (String qtyId : cartitemModelList.get(finalX).getQtyIDs()){

                                                              if (!serverQuantity.contains(qtyId)){

                                                                  if (noLongerAvailable){
                                                                      cartitemModelList.get(finalX).setInStock(false);
                                                                  }else{
                                                                      cartitemModelList.get(finalX).setQtyError(true);
                                                                      cartitemModelList.get(finalX).setMaxQuantity(availableQty);
                                                                      Toast.makeText(DeliveryActivity.this,"Sorry! all products may not be available",Toast.LENGTH_SHORT).show();
                                                                  }
                                                                  allProductsAvailable = false;
                                                              }else{
                                                                  availableQty++;
                                                                  noLongerAvailable = false;
                                                              }

                                                          }
                                                          cartAdapter.notifyDataSetChanged();
                                                      }else{
                                                          String error = task.getException().getMessage();
                                                          Toast.makeText(DeliveryActivity.this,error,Toast.LENGTH_SHORT).show();

                                                      }
                                                  }
                                              });


                                  }
                               }
                           });
               }

            }
        }else {
            getQtyIDs = true;
        }

        name = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFullname();
        mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();
        fullname.setText(name+" - "+mobileNo);
        fullAddress.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAddress());
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());

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

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();


         if (getQtyIDs) {
            for (int x = 0; x < cartitemModelList.size() - 1; x++) {

                if(!successResponse) {
                    for (String qtyID : cartitemModelList.get(x).getQtyIDs()) {
                        int finalX = x;
                        firebaseFirestore.collection("PRODUCTS").document(cartitemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        if (qtyID.equals(cartitemModelList.get(finalX).getQtyIDs().get(cartitemModelList.get(finalX).getQtyIDs().size() -1))){
                                            cartitemModelList.get(finalX).getQtyIDs().clear();
                                        }
                                    }
                                });

                    }
                }else {
                    cartitemModelList.get(x).getQtyIDs().clear();
                }
            }
        }
    }

    private void showConfirmation(){
        //successResponse = true;
    }
}