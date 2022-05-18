package com.example.greencity;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartitemModel> cartitemModelList;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;

    public CartAdapter(List<CartitemModel> cartitemModelList,TextView cartTotalAmount,boolean showDeleteBtn) {
        this.cartitemModelList = cartitemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {
       switch (cartitemModelList.get(position).getType()){
           case 0:
              return CartitemModel.CART_ITEM;
           case 1:
               return CartitemModel.TOTAL_AMOUNT;
           default:
               return -1;
       }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case CartitemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout_item,parent,false);
                return new cartItemViewholder(cartItemView);

                case CartitemModel.TOTAL_AMOUNT:
                    View cartTotalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout,parent,false);
                    return new cartTotalAmountViewholder(cartTotalView);

            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (cartitemModelList.get(position).getType()){
            case CartitemModel.CART_ITEM:
                String productID = cartitemModelList.get(position).getProductID();
                String resource = cartitemModelList.get(position).getProductImage();
                String title = cartitemModelList.get(position).getProductTitle();
                long freeCoupens = cartitemModelList.get(position).getFreeCoupens();
                String productPrice = cartitemModelList.get(position).getProductPrice();
                String cuttedPrice = cartitemModelList.get(position).getCuttedPrice();
                long offersApplied = cartitemModelList.get(position).getOffersApplied();
                boolean inStock = cartitemModelList.get(position).isInStock();
                Long productQuantity = cartitemModelList.get(position).getProductQuantity();
                Long maxQuantity = cartitemModelList.get(position).getMaxQuantity();
                boolean qtyError = cartitemModelList.get(position).isQtyError();
                List<String> qtyIds = cartitemModelList.get(position).getQtyIDs();
                long stockQty = cartitemModelList.get(position).getStockQuantity();


                ((cartItemViewholder)holder).setItemDetails(productID,resource,title,freeCoupens,productPrice,cuttedPrice,offersApplied,position,inStock,String.valueOf(productQuantity),maxQuantity,qtyError,qtyIds,stockQty);

                break;
                case CartitemModel.TOTAL_AMOUNT:
                    int totalItems = 0;
                    int totalItemPrice = 0;
                    String deliveryPrice;
                    int totalAmount;
                    int savedAmount = 0;

                    for (int x = 0; x < cartitemModelList.size(); x++){
                        if (cartitemModelList.get(x).getType() == CartitemModel.CART_ITEM && cartitemModelList.get(x).isInStock()){

                            totalItems++;
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartitemModelList.get(x).getProductPrice());

                        }
                    }
                    if (totalItemPrice > 500){
                        deliveryPrice ="Free";
                        totalAmount = totalItemPrice;
                    }else {
                        deliveryPrice = "50";
                        totalAmount = totalItemPrice + 50;
                    }


                    ((cartTotalAmountViewholder)holder).setTotalAmount(totalItems,totalItemPrice,deliveryPrice,totalAmount,savedAmount);

                    break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return cartitemModelList.size();
    }

    class cartItemViewholder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private ImageView freeCoupenIcon;
        private TextView productTitle;
        private TextView freeCoupens;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView offersApplied;
        private TextView coupensApplied;
        private TextView productQuantity;
        private LinearLayout deleteBtn;
        private LinearLayout coupenRedemptionLayout;


        public cartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title_cart);
            freeCoupens = itemView.findViewById(R.id.tv_free_cpupen);
            freeCoupenIcon = itemView.findViewById(R.id.free_coupen_icon);
            productPrice = itemView.findViewById(R.id.product_price_cart);
            cuttedPrice = itemView.findViewById(R.id.cutted_price_cart);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            coupensApplied = itemView.findViewById(R.id.coupens_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            coupenRedemptionLayout = itemView.findViewById(R.id.coupen_redeem_layout);
            deleteBtn = itemView.findViewById(R.id.remove_item_btn);

        }
        private void setItemDetails(String productID,String resource, String title, long freeCoupensNo, String productPriceText, String cuttedPriceText, long offersAppliedNo,int position,boolean inStock,String quantity,Long maxQuantity,boolean qtyError,List<String>qtyIds, long stockQty){

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.place)).into(productImage);
            productTitle.setText(title);
            if (freeCoupensNo > 0){
                freeCoupenIcon.setVisibility(View.VISIBLE);
                freeCoupens.setVisibility(View.VISIBLE);
                if (freeCoupensNo == 1) {
                    freeCoupens.setText("Free " + freeCoupensNo + " Coupen");
                }else{
                    freeCoupens.setText("Free " + freeCoupensNo + " Coupens");
                }
            }else {
                freeCoupenIcon.setVisibility(View.INVISIBLE);
                freeCoupens.setVisibility(View.INVISIBLE);
            }
            if (inStock) {
                productPrice.setText("Rs."+productPriceText+"/-");
               //productPrice.setTextColor(Color.parseColor("000000"));
                cuttedPrice.setText("Rs."+cuttedPriceText+"/-");
                coupenRedemptionLayout.setVisibility(View.VISIBLE);

                productQuantity.setText("Qty: "+ quantity);
                if (!showDeleteBtn) {
                    if (qtyError) {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.red)));

                    } else {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.black)));
                    }
                }

                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog quantityDialog = new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);
                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(false);
                        final EditText quantityNo = quantityDialog.findViewById(R.id.quantity_number);
                        Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
                        Button okBtn = quantityDialog.findViewById(R.id.ok_btn);
                        quantityNo.setHint("Max "+ String.valueOf(maxQuantity));

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                quantityDialog.dismiss();
                            }
                        });

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!TextUtils.isEmpty(quantityNo.getText())) {
                                    if (Long.valueOf(quantityNo.getText().toString()) <= maxQuantity && Long.valueOf(quantityNo.getText().toString()) != 0) {

                                        if (itemView.getContext() instanceof MainActivity){
                                            DBqueries.cartitemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));

                                        }else {

                                            if (DeliveryActivity.fromCart) {
                                                DBqueries.cartitemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                            } else {
                                                DeliveryActivity.cartitemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                            }
                                        }

                                        productQuantity.setText("Qty: " + quantityNo.getText());


                                        if (!showDeleteBtn){
                                            DeliveryActivity.cartitemModelList.get(position).setQtyError(false);
                                            int initialQty = Integer.parseInt(quantity);
                                            int finalQty = Integer.parseInt(quantityNo.getText().toString());
                                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                                            if (finalQty > initialQty) {
                                                for (int y = 0; y < finalQty - initialQty; y++) {
                                                    String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);

                                                    Map<String, Object> timeStamp = new HashMap<>();
                                                    timeStamp.put("time", FieldValue.serverTimestamp());

                                                    int finalY = y;
                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(quantityDocumentName).set(timeStamp)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    qtyIds.add(quantityDocumentName);

                                                                    if (finalY + 1 == finalQty - initialQty) {

                                                                        firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(stockQty).get()
                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {

                                                                                            List<String> serverQuantity = new ArrayList<>();
                                                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                                                                                                serverQuantity.add(queryDocumentSnapshot.getId());
                                                                                            }

                                                                                            long availableQty = 0;

                                                                                            for (String qtyId : qtyIds) {

                                                                                                if (!serverQuantity.contains(qtyId)) {

                                                                                                        DeliveryActivity.cartitemModelList.get(position).setQtyError(true);
                                                                                                        DeliveryActivity.cartitemModelList.get(position).setMaxQuantity(availableQty);
                                                                                                        Toast.makeText(itemView.getContext(), "Sorry! all products may not be available", Toast.LENGTH_SHORT).show();
                                                                                                        DeliveryActivity.allProductsAvailable = false;

                                                                                                } else {
                                                                                                    availableQty++;
                                                                                                }

                                                                                            }
                                                                                            DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                                        } else {
                                                                                            String error = task.getException().getMessage();
                                                                                            Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();

                                                                                        }
                                                                                    }
                                                                                });


                                                                    }
                                                                }
                                                            });
                                                }
                                            }else if (initialQty > finalQty){
                                                for (int x = 0; x < initialQty - finalQty;x++) {

                                                    String qtyId = qtyIds.get(qtyIds.size() - 1 - x);

                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(qtyId).delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    qtyIds.remove(qtyId);
                                                                    DeliveryActivity.cartAdapter.notifyDataSetChanged();

                                                                }
                                                            });

                                                }
                                            }

                                        }

                                    }else {
                                        Toast.makeText(itemView.getContext(),"Max quantity : "+maxQuantity.toString(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                                    quantityDialog.dismiss();

                            }
                        });
                        quantityDialog.show();
                    }
                });

                if (offersAppliedNo > 0) {
                    offersApplied.setVisibility(View.VISIBLE);
                    offersApplied.setText(offersAppliedNo + " Offfers applied");
                }else {
                    offersApplied.setVisibility(View.INVISIBLE);
                }

            }else{
                productPrice.setText("Out of stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                cuttedPrice.setText("");
                coupenRedemptionLayout.setVisibility(View.GONE);

                productQuantity.setText("Qty: "+ 0);
                productQuantity.setTextColor(Color.parseColor("#50000000"));
            }



//            if (showDeleteBtn){
//                deleteBtn.setVisibility(View.VISIBLE);
//            }else{
//                deleteBtn.setVisibility(View.GONE);
//
//            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ProductDetails.running_cart_query){
                        ProductDetails.running_rating_query = true;

                        DBqueries.removeFromCart(position,itemView.getContext(),cartTotalAmount);
                    }
                }
            });

        }
    }

    class cartTotalAmountViewholder extends RecyclerView.ViewHolder{

        private TextView totalItems;
        private TextView totalItemsPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView savedAmount;


        public cartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);
            totalItems = itemView.findViewById(R.id.total_items);
            totalItemsPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);
        }
        private void setTotalAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText, int savedAmountText){
            totalItems.setText("Price("+totalItemText+" items");
            totalItemsPrice.setText("Rs."+totalItemPriceText+"/-");
            if (deliveryPriceText.equals("FREE")) {
                deliveryPrice.setText(deliveryPriceText);
            }else{
                deliveryPrice.setText("Rs."+deliveryPriceText+"/-");

            }
            totalAmount.setText("Rs."+totalAmountText+"/-");
            cartTotalAmount.setText("Rs."+totalAmountText+"/-");
            savedAmount.setText("You saved Rs."+savedAmountText+"/-");

            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalItemPriceText == 0){
                if (DeliveryActivity.fromCart) {
                    DBqueries.cartitemModelList.remove(DBqueries.cartitemModelList.size() - 1);
                    DeliveryActivity.cartitemModelList.remove(DeliveryActivity.cartitemModelList.size() - 1);

                }
                if (showDeleteBtn){
                    DBqueries.cartitemModelList.remove(DBqueries.cartitemModelList.size() - 1);

                }
                parent.setVisibility(View.GONE);
            }else{
                parent.setVisibility(View.VISIBLE);

            }
        }
    }
}
