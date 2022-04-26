package com.example.greencity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {

    List<HorizontalProductModel> horizontalProductModelList;

    public GridProductLayoutAdapter(List<HorizontalProductModel> horizontalProductModelList) {
        this.horizontalProductModelList = horizontalProductModelList;
    }

    @Override
    public int getCount() {
        return 4;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_item_layout, null);
            view.setElevation(0);

            ImageView productImage = view.findViewById(R.id.horizontal_sc_pro_img);
            TextView productTitle = view.findViewById(R.id.hori_scr_pro_title);
            TextView productDesc = view.findViewById(R.id.hori_scr_pro_desc);
            TextView productPrice = view.findViewById(R.id.hori_sc_pro_price);

            productImage.setImageResource(horizontalProductModelList.get(i).getProductImage());
            productTitle.setText(horizontalProductModelList.get(i).getProductTitle());
            productDesc.setText(horizontalProductModelList.get(i).getProductDesc());
            productPrice.setText(horizontalProductModelList.get(i).getProductPrice());

        } else {
            view = convertView;
        }
        return view;
    }

}
