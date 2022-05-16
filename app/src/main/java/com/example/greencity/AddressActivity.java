package com.example.greencity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.HashMap;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {

    private EditText city;
    private EditText area;
    private EditText houseNo;
    private EditText pincode;
    private EditText landmark;
    private EditText name;
    private EditText mobileNo;
    private EditText alternameMobileNo;
    private Spinner districtSpinner;

    private Button saveBtn;
    private String [] districtList;
    private String selectedDistrict;
    private Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add a new Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Loading Dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //Loading Dialog

         districtList = getResources().getStringArray(R.array.valley_distric);
        city = findViewById(R.id.city_name);
        area = findViewById(R.id.area);
        houseNo = findViewById(R.id.house_no);
        pincode = findViewById(R.id.pincode);
        landmark = findViewById(R.id.landmark);
        name = findViewById(R.id.name);
        mobileNo = findViewById(R.id.mobile_no);
        alternameMobileNo = findViewById(R.id.alternate_no);
        districtSpinner = findViewById(R.id.districSpinner);


        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,districtList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        districtSpinner.setAdapter(spinnerAdapter);
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selectedDistrict = districtList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveBtn= findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(city.getText())){
                    if (!TextUtils.isEmpty(area.getText())){
                        if (!TextUtils.isEmpty(houseNo.getText())){
                            if (!TextUtils.isEmpty(pincode.getText()) && pincode.getText().length() == 6){
                                if (!TextUtils.isEmpty(name.getText())) {
                                    if (!TextUtils.isEmpty(mobileNo.getText()) && mobileNo.getText().length() == 10){

                                        loadingDialog.show();

                                        String fullAddress = houseNo.getText().toString()+" "+area.getText().toString()+" "+landmark.getText().toString()+" "+city.getText().toString()+" "+selectedDistrict;


                                        Map<String,Object> addAdress = new HashMap();
                                        addAdress.put("list_size",(long)DBqueries.addressesModelList.size()+1);
                                        if (TextUtils.isEmpty(alternameMobileNo.getText())) {
                                            addAdress.put("fullname_" + String.valueOf((long) DBqueries.addressesModelList.size() + 1), name.getText().toString() + " - " + mobileNo.getText().toString());
                                        }else{
                                            addAdress.put("fullname_" + String.valueOf((long) DBqueries.addressesModelList.size() + 1), name.getText().toString() + " - " + mobileNo.getText().toString()+" or "+alternameMobileNo.getText().toString());

                                        }

                                        addAdress.put("address_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),fullAddress);
                                        addAdress.put("pincode_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),pincode.getText().toString());
                                        addAdress.put("selected_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),true);
                                        if (DBqueries.addressesModelList.size() > 0) {
                                            addAdress.put("selected_" + (DBqueries.selectedAddress + 1), false);
                                        }


                                        FirebaseFirestore.getInstance().collection("USERS")
                                                .document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                                .document("MY_ADDRESSES")
                                                .update(addAdress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    if (DBqueries.addressesModelList.size() > 0) {
                                                        DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                                                    }
                                                    if (TextUtils.isEmpty(alternameMobileNo.getText())) {
                                                        DBqueries.addressesModelList.add(new AddressesModel(name.getText().toString() + " - " + mobileNo.getText().toString(), fullAddress, pincode.getText().toString(), true));
                                                    }else{
                                                        DBqueries.addressesModelList.add(new AddressesModel(name.getText().toString() + " - " + mobileNo.getText().toString()+" or "+alternameMobileNo.getText().toString(), fullAddress, pincode.getText().toString(), true));
                                                    }


                                                    if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                        Intent deliveryIntent = new Intent(AddressActivity.this, DeliveryActivity.class);
                                                        startActivity(deliveryIntent);
                                                    }else {
                                                        MyAddressActivity.refreshItem(DBqueries.selectedAddress,DBqueries.addressesModelList.size() -1);
                                                    }
                                                    DBqueries.selectedAddress = DBqueries.addressesModelList.size() - 1;
                                                    finish();

                                                }else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(AddressActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                loadingDialog.dismiss();
                                            }
                                        });

                                    }else{
                                        mobileNo.requestFocus();
                                        Toast.makeText(AddressActivity.this,"Provide a valid Number",Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    name.requestFocus();
                                }
                            }else {
                                pincode.requestFocus();
                                Toast.makeText(AddressActivity.this,"Provide valid pincode",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            houseNo.requestFocus();
                        }
                    }else{
                        area.requestFocus();

                    }
                }else {
                    city.requestFocus();
                }

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