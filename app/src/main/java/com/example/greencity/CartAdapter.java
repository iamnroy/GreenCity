package com.example.greencity;

import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

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

                ((cartItemViewholder)holder).setItemDetails(productID,resource,title,freeCoupens,productPrice,cuttedPrice,offersApplied,position,inStock);

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
        private void setItemDetails(String productID,String resource, String title, long freeCoupensNo, String productPriceText, String cuttedPriceText, long offersAppliedNo,int position,boolean inStock){

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

                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog quantityDialog = new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);
                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(false);
                        final EditText quantityNo = quantityDialog.findViewById(R.id.quantity_number);
                        Button cancelBtn = quantityNo.findViewById(R.id.cancel_btn);
                        Button okBtn = quantityNo.findViewById(R.id.ok_btn);

//                        cancelBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                quantityDialog.dismiss();
//                            }
//                        });
//
//                        okBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                productQuantity.setText("Qty: "+ quantityNo.getText());
//                                quantityDialog.dismiss();
//                            }
//                        });
                        //quantityDialog.show();
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
                DBqueries.cartitemModelList.remove(DBqueries.cartitemModelList.size()-1);
                parent.setVisibility(View.GONE);
            }else{
                parent.setVisibility(View.VISIBLE);

            }
        }
    }
}
