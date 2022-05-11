package com.example.greencity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {

    private List<HorizontalProductModel> horizontalProductModel;

    public HorizontalProductScrollAdapter(List<HorizontalProductModel> horizontalProductModel) {
        this.horizontalProductModel = horizontalProductModel;
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.ViewHolder holder, int position) {

        String resource = horizontalProductModel.get(position).getProductImage();
        String title = horizontalProductModel.get(position).getProductTitle();
        String desc = horizontalProductModel.get(position).getProductDesc();
        String price = horizontalProductModel.get(position).getProductPrice();

        holder.setProductImage(resource);
        holder.setProductTitle(title);
        holder.setProductDesc(desc);
        holder.setProductPrice(price);
    }

    @Override
    public int getItemCount() {

        if (horizontalProductModel.size() > 8) {
            return 8;
        }else {
            return horizontalProductModel.size();

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView productDesc;
        private TextView productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.horizontal_sc_pro_img);
            productTitle = itemView.findViewById(R.id.hori_scr_pro_title);
            productDesc = itemView.findViewById(R.id.hori_scr_pro_desc);
            productPrice = itemView.findViewById(R.id.hori_sc_pro_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent productDetailsIntent = new Intent(itemView.getContext(),ProductDetails.class);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });


        }

        private void setProductImage(String resource){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.cathome)).into(productImage);
        }
        private void setProductTitle(String title){
            productTitle.setText(title);
        }
        private void setProductDesc(String Desc){
            productDesc.setText(Desc);

        }
        private void setProductPrice(String price){
            productPrice.setText("Rs. "+price+" only");
        }
    }
}
