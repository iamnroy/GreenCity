package com.example.greencity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBqueries {

//    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();


    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList = new ArrayList<>();
    //public static List<HomePageModel> homePageModelList = new ArrayList<>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesNames = new ArrayList<>();
    public static List<String> wishList = new ArrayList<>();
    public static List<WishlistModel> wishlistModelList = new ArrayList<>();
    public static List<String> myRateedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();
    public static List<CartitemModel> cartitemModelList = new ArrayList<>();
    public static int selectedAddress = -1;
    public static List<AddressesModel> addressesModelList = new ArrayList<>();



    public static void loadCategories(RecyclerView categoryRecyclerView, Context context){
        categoryModelList.clear();

        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                            }
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                            categoryRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadFragmentData(RecyclerView homePageRecyclerView, Context context, int index, String categoryName){

        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                               // BANNER SLIDER CODE
//                                if ((long)documentSnapshot.get("view_type") == 0){
//                                    List<SliderModel> sliderModelList = new ArrayList<>();
//                                    long no_of_banners = (long)documentSnapshot.get("no_of_banners");
//                                    for (long x = 1;x < no_of_banners + 1;x++){
//                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_"+x).toString()
//                                                ,documentSnapshot.get("banner_"+x+"_background").toString()));
//                                    }
//                                    lists.get(index).add(new HomePageModel(0,sliderModelList));
//
//                                 }else
                                if ((long)documentSnapshot.get("view_type") == 1){
                                    lists.get(index).add(new HomePageModel(1,documentSnapshot.get("strip_ad_banner").toString()
                                            ,documentSnapshot.get("background").toString()));

                                }else if ((long)documentSnapshot.get("view_type") == 2){

                                    List<WishlistModel> viewAllProductList = new ArrayList<>();

                                    List<HorizontalProductModel> horizontalProductModelList = new ArrayList<>();
                                    long no_of_products = (long)documentSnapshot.get("no_of_products");
                                    for (long x = 1;x < no_of_products + 1;x++){
                                        horizontalProductModelList.add(new HorizontalProductModel(documentSnapshot.get("product_ID_"+x).toString()
                                                ,documentSnapshot.get("product_image_"+x).toString()
                                                ,documentSnapshot.get("product_title_"+x).toString()
                                                ,documentSnapshot.get("product_subtitle_"+x).toString()
                                                ,documentSnapshot.get("product_price_"+x).toString()));

                                        viewAllProductList.add(new WishlistModel(documentSnapshot.get("product_ID_"+x).toString(),documentSnapshot.get("product_image_"+x).toString()
                                                ,documentSnapshot.get("product_full_title_"+x).toString()
                                                ,(long)documentSnapshot.get("free_coupens_"+x)
                                                ,documentSnapshot.get("average_rating_"+x).toString()

                                                ,(long)documentSnapshot.get("total_ratings_"+x)
                                                ,documentSnapshot.get("product_price_"+x).toString()
                                                ,documentSnapshot.get("cutted_price_"+x).toString()
                                                ,(boolean)documentSnapshot.get("COD_"+x)
                                        ,(boolean) documentSnapshot.get("in_stock_"+x)));

                                    }
                                    lists.get(index).add(new HomePageModel(2,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),horizontalProductModelList,viewAllProductList));

                                }else if ((long)documentSnapshot.get("view_type") == 3){
                                    List<HorizontalProductModel> GridLayoutModelList = new ArrayList<>();
                                    long no_of_products = (long)documentSnapshot.get("no_of_products");
                                    for (long x = 1;x < no_of_products + 1;x++){
                                        GridLayoutModelList.add(new HorizontalProductModel(documentSnapshot.get("product_ID_"+x).toString()
                                                ,documentSnapshot.get("product_image_"+x).toString()
                                                ,documentSnapshot.get("product_title_"+x).toString()
                                                ,documentSnapshot.get("product_subtitle_"+x).toString()
                                                ,documentSnapshot.get("product_price_"+x).toString()));

                                    }
                                    lists.get(index).add(new HomePageModel(3,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),GridLayoutModelList));

                                }

                            }
                            HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
                            homePageRecyclerView.setAdapter(homePageAdapter);
                           // adapter.notifyDataSetChanged();
                            homePageAdapter.notifyDataSetChanged();
                            HomeFragment.swipeRefreshLayout.setRefreshing(false);

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadWishlist(final Context context, Dialog dialog, boolean loadProductData){
        wishList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if (task.isSuccessful()){
                   for (long x = 0;x < (long)task.getResult().get("list_size");x++){
                       wishList.add(task.getResult().get("product_ID_"+x).toString());

                       if (DBqueries.wishList.contains(ProductDetails.productID)){
                           ProductDetails.ALREADY_ADDED_TO_WISHLIST = true;
                           if (ProductDetails.addTowishlist != null) {
                               ProductDetails.addTowishlist.setSupportImageTintList(context.getResources().getColorStateList(R.color.red));
                           }

                       }else {
                           if (ProductDetails.addTowishlist != null) {

                               //ProductDetails.addTowishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#A1A9A3")));
                               ProductDetails.addTowishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#A1A9A3")));

                           }
                           ProductDetails.ALREADY_ADDED_TO_WISHLIST = false;
                       }

                       if (loadProductData) {
                           wishlistModelList.clear();
                           String productId = task.getResult().get("product_ID_" + x).toString();
                           firebaseFirestore.collection("PRODUCTS").document(productId)
                                   .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                   if (task.isSuccessful()) {

                                       DocumentSnapshot documentSnapshot = task.getResult();
                                       FirebaseFirestore.getInstance().collection("PRODUCTS").document(productId).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                                               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                       if (task.isSuccessful()){

                                                           if (task.getResult().getDocuments().size() < (long)documentSnapshot.get("stock_quantity")){

                                                               wishlistModelList.add(new WishlistModel(productId,documentSnapshot.get("product_image_1").toString()
                                                                       , documentSnapshot.get("product_title").toString()
                                                                       , (long) documentSnapshot.get("free_coupens")
                                                                       , documentSnapshot.get("average_rating").toString()
                                                                       , (long) documentSnapshot.get("total_ratings")
                                                                       , documentSnapshot.get("product_price").toString()
                                                                       , documentSnapshot.get("cutted_price").toString()
                                                                       , (boolean) documentSnapshot.get("COD")
                                                                       ,true));

                                                           }else {

                                                               wishlistModelList.add(new WishlistModel(productId,documentSnapshot.get("product_image_1").toString()
                                                                       , documentSnapshot.get("product_title").toString()
                                                                       , (long) documentSnapshot.get("free_coupens")
                                                                       , documentSnapshot.get("average_rating").toString()
                                                                       , (long) documentSnapshot.get("total_ratings")
                                                                       , documentSnapshot.get("product_price").toString()
                                                                       , documentSnapshot.get("cutted_price").toString()
                                                                       , (boolean) documentSnapshot.get("COD")
                                                                       ,false));

                                                           }
                                                           MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();

                                                       }else{
                                                           String error = task.getException().getMessage();
                                                           Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               });

//                                       wishlistModelList.add(new WishlistModel(productId,task.getResult().get("product_image_1").toString()
//                                               , task.getResult().get("product_title").toString()
//                                               , (long) task.getResult().get("free_coupens")
//                                               , task.getResult().get("average_rating").toString()
//                                               , (long) task.getResult().get("total_ratings")
//                                               , task.getResult().get("product_price").toString()
//                                               , task.getResult().get("cutted_price").toString()
//                                               , (boolean) task.getResult().get("COD")
//                                               ,(boolean)task.getResult().get("in_stock")));

                                      // MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();

                                   } else {
                                       String error = task.getException().getMessage();
                                       Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
                       }
                   }
               }else{
                   String error = task.getException().getMessage();
                   Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
               }
               dialog.dismiss();
            }
        });
    }

    public static void removeFromWishlist(int index,Context context){
        String removedProductId = wishList.get(index);
        wishList.remove(index);
        Map<String,Object> updateWishlist = new HashMap<>();

        for (int x = 0;x < wishList.size();x++){
            updateWishlist.put("product_ID_"+x,wishList.get(x));
        }
        updateWishlist.put("list_size",(long)wishList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .set(updateWishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if (wishlistModelList.size() != 0){
                        wishlistModelList.remove(index);
                        MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                    }
                    ProductDetails.ALREADY_ADDED_TO_WISHLIST = false;
                    Toast.makeText(context, "Removed successfully!", Toast.LENGTH_SHORT).show();

                }else{
                    if (ProductDetails.addTowishlist != null) {
                        ProductDetails.addTowishlist.setSupportImageTintList(context.getResources().getColorStateList(R.color.red));
                    }
                    wishList.add(index,removedProductId);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
//                if (ProductDetails.addTowishlist != null) {
//                    ProductDetails.addTowishlist.setEnabled(true);
//                }
                ProductDetails.running_wishlist_query = false;

            }
        });
    }

    public static void loadRatingList(Context context){
        if (!ProductDetails.running_rating_query) {
            ProductDetails.running_rating_query = true;
            myRateedIds.clear();
            myRating.clear();
            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {

                            myRateedIds.add(task.getResult().get("product_ID_" + x).toString());
                            myRating.add((long) task.getResult().get("rating_" + x));

                            if (task.getResult().get("product_ID_" + x).toString().equals(ProductDetails.productID)) {
                                ProductDetails.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if (ProductDetails.rateNowContainer != null) {
                                    ProductDetails.setRating(ProductDetails.initialRating);
                                }
                            }
                        }

                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                    ProductDetails.running_rating_query = false;
                }
            });
        }
    }

    public static void loadCartList(Context context, Dialog dialog, boolean loadProductData, TextView badgeCount,TextView cartTotalAmount){

        cartList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    for (long x = 0;x < (long)task.getResult().get("list_size");x++){
                        cartList.add(task.getResult().get("product_ID_"+x).toString());

                        if (DBqueries.cartList.contains(ProductDetails.productID)){
                            ProductDetails.ALREADY_ADDED_TO_CART = true;


                        }else {

                            ProductDetails.ALREADY_ADDED_TO_CART = false;
                        }

                        if (loadProductData) {
                            cartitemModelList.clear();
                            String productId = task.getResult().get("product_ID_" + x).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {


                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productId).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()){
                                                            int index = 0;
                                                            if (cartList.size() >= 2){
                                                                index = cartList.size() -2;
                                                            }


                                                            if (task.getResult().getDocuments().size() < (long)documentSnapshot.get("stock_quantity")){

                                                                cartitemModelList.add(index,new CartitemModel(CartitemModel.CART_ITEM,productId,documentSnapshot.get("product_image_1").toString()
                                                                        , documentSnapshot.get("product_title").toString()
                                                                        , (long) documentSnapshot.get("free_coupens")
                                                                        , documentSnapshot.get("product_price").toString()
                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                        , (long) 1
                                                                        , (long) 0
                                                                        , (long) 0
                                                                        ,true
                                                                        ,(long)documentSnapshot.get("max-quantity")
                                                                        ,(long)documentSnapshot.get("stock_quantity")));

                                                            }else {

                                                                cartitemModelList.add(index,new CartitemModel(CartitemModel.CART_ITEM,productId,documentSnapshot.get("product_image_1").toString()
                                                                        , documentSnapshot.get("product_title").toString()
                                                                        , (long) documentSnapshot.get("free_coupens")
                                                                        , documentSnapshot.get("product_price").toString()
                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                        , (long) 1
                                                                        , (long) 0
                                                                        , (long) 0
                                                                        ,false
                                                                        ,(long)documentSnapshot.get("max-quantity")
                                                                        ,(long)documentSnapshot.get("stock_quantity")));
                                                            }
                                                            if (cartList.size() == 1){
                                                                cartitemModelList.add(new CartitemModel(CartitemModel.TOTAL_AMOUNT));
                                                                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                                                parent.setVisibility(View.VISIBLE);
                                                            }
                                                            if (cartList.size() == 0){
                                                                cartitemModelList.clear();
                                                            }
                                                            MyCartFragment.cartAdapter.notifyDataSetChanged();

                                                        }else{
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

//                                        int index = 0;
//                                        if (cartList.size() >= 2){
//                                            index = cartList.size() -2;
//                                        }
//                                        cartitemModelList.add(index,new CartitemModel(CartitemModel.CART_ITEM,productId,task.getResult().get("product_image_1").toString()
//                                                , task.getResult().get("product_title").toString()
//                                                , (long) task.getResult().get("free_coupens")
//                                                , task.getResult().get("product_price").toString()
//                                                , task.getResult().get("cutted_price").toString()
//                                                , (long) 1
//                                                , (long) 0
//                                                , (long) 0
//                                                ,(boolean)task.getResult().get("in_stock")
//                                                ,(long)task.getResult().get("max-quantity")
//                                                 ,(long)task.getResult().get("stock_quantity")));

//                                        if (cartList.size() == 1){
//                                            cartitemModelList.add(new CartitemModel(CartitemModel.TOTAL_AMOUNT));
//                                            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
//                                            parent.setVisibility(View.VISIBLE);
//                                        }
//                                        if (cartList.size() == 0){
//                                            cartitemModelList.clear();
//                                        }
//                                        MyCartFragment.cartAdapter.notifyDataSetChanged();

                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    if (cartList.size() != 0) {
                        badgeCount.setVisibility(View.VISIBLE);
                    }else {
                        badgeCount.setVisibility(View.INVISIBLE);
                    }
                    if (DBqueries.cartList.size() < 99) {
                        badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                    }else {
                        badgeCount.setText("99");

                    }
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public static void removeFromCart(int index,Context context,TextView cartTotalAmount){
        String removedProductId = cartList.get(index);
        cartList.remove(index);
        Map<String,Object> updateCartlist = new HashMap<>();

        for (int x = 0;x < cartList.size();x++){
            updateCartlist.put("product_ID_"+x,cartList.get(x));
        }
        updateCartlist.put("list_size",(long)cartList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .set(updateCartlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if (cartitemModelList.size() != 0){
                        cartitemModelList.remove(index);
                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                    }
                    if (cartList.size() == 0){
                        LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                        parent.setVisibility(View.GONE);
                        cartitemModelList.clear();
                    }
                    Toast.makeText(context, "Removed successfully!", Toast.LENGTH_SHORT).show();

                }else{

                    cartList.add(index,removedProductId);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                ProductDetails.running_cart_query = false;

            }
        });
    }

    public static void loadAddresses(Context context,Dialog loadingDialog){

        addressesModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    Intent deliveryIntent;

                    if((long)task.getResult().get("list_size") == 0){ //replace null with 0
                         deliveryIntent = new Intent(context,AddressActivity.class);
                         deliveryIntent.putExtra("INTENT","deliveryIntent");
                    }else {

                        for (long x = 1;x < (long)task.getResult().get("list_size") +1;x++){
                            addressesModelList.add(new AddressesModel(task.getResult().get("fullname_"+x).toString(),
                                    task.getResult().get("address_"+x).toString(),
                                    task.getResult().get("pincode_"+x).toString(),
                                    (boolean)task.getResult().get("selected_"+x)
                                    ,task.getResult().get("mobile_no_"+x).toString()));

                            if ((boolean)task.getResult().get("selected_"+x)){
                                selectedAddress =  Integer.parseInt(String.valueOf(x - 1));
                            }
                        }
                         deliveryIntent = new Intent(context,DeliveryActivity.class);
                    }
                    context.startActivity(deliveryIntent);
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });
    }

    public static void clearData(){
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        wishList.clear();
        wishlistModelList.clear();
        cartList.clear();
        cartitemModelList.clear();
        myRateedIds.clear();
        myRating.clear();
        addressesModelList.clear();
    }

}
