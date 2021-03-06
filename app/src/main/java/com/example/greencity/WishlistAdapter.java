package com.example.greencity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private List<WishlistModel> wishlistModelList;
    private Boolean wishlist;
    private int lastPosition = -1;

    public WishlistAdapter(List<WishlistModel> wishlistModelList,Boolean wishlist) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist = wishlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String productId = wishlistModelList.get(position).getProductId();
        String resource = wishlistModelList.get(position).getProductImage();
        String title = wishlistModelList.get(position).getProductTitle();
        long freeCoupens = wishlistModelList.get(position).getFreeCoupens();
        String rating = wishlistModelList.get(position).getRating();
        long totalRating = wishlistModelList.get(position).getTotalRatings();
        String productPrice = wishlistModelList.get(position).getProductPrice();
        String cuttedPrice = wishlistModelList.get(position).getCuttedPrice();
        boolean paymentMethod = wishlistModelList.get(position).isCOD();
        boolean inStock = wishlistModelList.get(position).isInStock();
        holder.setData(productId,resource,title,freeCoupens,rating,totalRating,productPrice,cuttedPrice,paymentMethod,position,inStock);

        //Comment this to remove above position error
//        if (lastPosition < position) {
//            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
//            holder.itemView.setAnimation(animation);
//            lastPosition =position;
//        }

    }

    @Override
    public int getItemCount() {
        return wishlistModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private TextView productTitle;
        private TextView freeCoupens;
        private ImageView coupenIcon;
        private TextView rating;
        private TextView totalRating;
        private View priceCut;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView paymentMethod;
        private ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image_wish);
            productTitle = itemView.findViewById(R.id.product_title_wish);
            freeCoupens = itemView.findViewById(R.id.free_coupen_wish);
            coupenIcon = itemView.findViewById(R.id.coupen_icon_wish);
            rating = itemView.findViewById(R.id.tv_product_rating_miniview);
            totalRating = itemView.findViewById(R.id.total_ratings_wish);
            priceCut = itemView.findViewById(R.id.price_cut_wish);
            productPrice = itemView.findViewById(R.id.product_price_wish);
            cuttedPrice = itemView.findViewById(R.id.cutted_price_wish);
            paymentMethod = itemView.findViewById(R.id.payment_method_wish);
           deleteBtn = itemView.findViewById(R.id.delete_btn_wish);

        }
        private void setData(String productId,String resource,String title,long freeCoupensNo,String averageRate, long totalRatingsNo,String price,String cuttedPriceValue, boolean COD,int index,boolean inStock){

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.place)).into(productImage);
            productTitle.setText(title);
            if (freeCoupensNo != 0 && inStock){
                coupenIcon.setVisibility(View.VISIBLE);
                if (freeCoupensNo == 1) {
                    freeCoupens.setText("Free " + freeCoupensNo + " coupen");
                }else{
                    freeCoupens.setText("Free " + freeCoupensNo + " coupens");

                }
            }else{
                coupenIcon.setVisibility(View.INVISIBLE);
                freeCoupens.setVisibility(View.INVISIBLE);

            }
            if (inStock) {

                rating.setVisibility(View.VISIBLE);
                totalRating.setVisibility(View.VISIBLE);
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                cuttedPrice.setVisibility(View.VISIBLE);

                rating.setText(averageRate);
                totalRating.setText(totalRatingsNo+"(ratings)");
                productPrice.setText(price);
                cuttedPrice.setText(cuttedPriceValue);

                if (COD){
                    paymentMethod.setVisibility(View.VISIBLE);

                }else{
                    paymentMethod.setVisibility(View.INVISIBLE);

                }
            }else {
                rating.setVisibility(View.INVISIBLE);
                totalRating.setVisibility(View.INVISIBLE);
                productPrice.setText("Out of stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                cuttedPrice.setVisibility(View.INVISIBLE);
            }

            if (wishlist){
                deleteBtn.setVisibility(View.VISIBLE);
            }else{
                deleteBtn.setVisibility(View.GONE);
            }

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ProductDetails.running_wishlist_query) {
                        ProductDetails.running_wishlist_query = true;
                        DBqueries.removeFromWishlist(index, itemView.getContext());
                    }
                }
            });
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                   // Intent productDetailsIntent = Intent(itemView.getContext(),ProductDetails.class);
//                    productDetailsIntent.putExtra("PRODUCT_ID",productId);
//                    itemView.getContext().startActivity(productDetailsIntent);
//                }
//            });
        }
    }
}
