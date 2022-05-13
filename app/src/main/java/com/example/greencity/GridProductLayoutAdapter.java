package com.example.greencity;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {

    List<HorizontalProductModel> horizontalProductModelList;

    public GridProductLayoutAdapter(List<HorizontalProductModel> horizontalProductModelList) {
        this.horizontalProductModelList = horizontalProductModelList;
    }

    @Override
    public int getCount() {
        return horizontalProductModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView,final ViewGroup viewGroup) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_item_layout, null);
            view.setElevation(0);
            view.setBackgroundColor(Color.parseColor("#ffffff"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent productDetailsIntent = new Intent(viewGroup.getContext(),ProductDetails.class);
                    productDetailsIntent.putExtra("PRODUCT_ID",horizontalProductModelList.get(i).getProductID());
                    viewGroup.getContext().startActivity(productDetailsIntent);
                }
            });

            ImageView productImage = view.findViewById(R.id.horizontal_sc_pro_img);
            TextView productTitle = view.findViewById(R.id.hori_scr_pro_title);
            TextView productDesc = view.findViewById(R.id.hori_scr_pro_desc);
            TextView productPrice = view.findViewById(R.id.hori_sc_pro_price);

            Glide.with(viewGroup.getContext()).load(horizontalProductModelList.get(i).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.place)).into(productImage);
            productTitle.setText(horizontalProductModelList.get(i).getProductTitle());
            productDesc.setText(horizontalProductModelList.get(i).getProductDesc());
            productPrice.setText("Rs."+horizontalProductModelList.get(i).getProductPrice());

        } else {
            view = convertView;
        }
        return view;
    }

}
