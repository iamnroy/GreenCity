package com.example.greencity;


import static com.example.greencity.MainActivity.showCart;
import static com.example.greencity.Register.setSignUpFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.support.v7.widget.Toolbar;


import com.example.greencity.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetails extends AppCompatActivity {

    public static boolean running_wishlist_query = false;
    public static boolean running_rating_query = false;
    public static boolean running_cart_query = false;
    public static Activity productDetailsActivity;


    private ViewPager productImagesViewPager;
    private Button coupenRedeemBtn;

    //Coupen Dialog
    public static TextView coupenTitle;
    public static TextView coupenExpiryDate;
    public static TextView coupenBody;
    private static RecyclerView coupensRecyclerView;
    private static LinearLayout selectedCoupen;
    //Coupen Dialog

    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView productPrice;
    private TextView cuttedPrice;
    private ImageView codIndicator;
    private TextView tvCodIndicator;
    private TextView rewardTitle;
    private TextView rewardBody;
    private TextView totalRatingMiniView;

    //Product Description
    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    private ViewPager productDetailsViewpager;
    private TabLayout productDetailsTablayout;
    private TextView productOnlyDescriptionBody;

    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    private String productDescription;
    private String productOtherDetails;
    private TabLayout viewpagerIndicator;
    //Product Description


    //Rating Layout
    public static int initialRating;
    public static LinearLayout rateNowContainer;
    private TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private TextView averageRating;
    //Rating Layout

    private Button buyNowBtn;
    private LinearLayout addToCartBtn;
    public static MenuItem cartItem;

    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;

    public static FloatingActionButton addTowishlist;
    FirebaseFirestore firebaseFirestore;

    private Dialog signInDialog;
    private Dialog loadingDialog;
    private FirebaseUser currentUser;
    public static String productID;
    private TextView badgeCount;
    private boolean inStock;
    private DocumentSnapshot documentSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        //androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager = findViewById(R.id.product_images_viewpager);
        viewpagerIndicator = findViewById(R.id.viewpager_indicator);
        addTowishlist = findViewById(R.id.add_to_wishlist);
        productDetailsViewpager = findViewById(R.id.product_details_view_pager);
        productDetailsTablayout = findViewById(R.id.product_dtails_tablayout);
        buyNowBtn = findViewById(R.id.buy_now_btn);
        coupenRedeemBtn = findViewById(R.id.coupen_redeem_btn);
        productTitle = findViewById(R.id.product_title);
        averageRatingMiniView = findViewById(R.id.tv_product_rating_miniview);
        totalRatingMiniView = findViewById(R.id.total_ratings_miniview);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        tvCodIndicator = findViewById(R.id.tv_cod_indicator);
        codIndicator = findViewById(R.id.cod_indicator_image);
        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);
        productDetailsTabsContainer = findViewById(R.id.product_details_tabs_container);
        productDetailsOnlyContainer = findViewById(R.id.product_details_container);
        productOnlyDescriptionBody = findViewById(R.id.product_details_body);
        totalRatings = findViewById(R.id.total_ratings);
        ratingsNoContainer = findViewById(R.id.ratings_number_container);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        ratingsProgressBarContainer = findViewById(R.id.ratings_progressbar_container);
        averageRating = findViewById(R.id.average_rating);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);

        initialRating = -1;
        //Loading Dialog
        loadingDialog = new Dialog(ProductDetails.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        //Loading Dialog

        firebaseFirestore = FirebaseFirestore.getInstance();
        List<String> productImages = new ArrayList<>();
        productID = getIntent().getStringExtra("PRODUCT_ID");

        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();

                    //New added code
                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){

                                        for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
                                            productImages.add(documentSnapshot.get("product_image_" + x).toString());
                                        }
                                        ProductimagesAdapter productimagesAdapter = new ProductimagesAdapter(productImages);
                                        productImagesViewPager.setAdapter(productimagesAdapter);

                                        productTitle.setText(documentSnapshot.get("product_title").toString());
                                        averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                                        totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings") + ")ratings");
                                        productPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "-/");
                                        cuttedPrice.setText("Rs." + documentSnapshot.get("cutted_price").toString() + "-/");
                                        if ((boolean) documentSnapshot.get("COD")) {
                                            codIndicator.setVisibility(View.VISIBLE);
                                            tvCodIndicator.setVisibility(View.VISIBLE);
                                        } else {
                                            codIndicator.setVisibility(View.INVISIBLE);
                                            tvCodIndicator.setVisibility(View.INVISIBLE);
                                        }
                                        rewardTitle.setText((long) documentSnapshot.get("free_coupens") + documentSnapshot.get("free_coupen_title").toString());
                                        rewardBody.setText(documentSnapshot.get("free_coupen_body").toString());

                                        if ((boolean) documentSnapshot.get("use_tab_layout")) {
                                            productDetailsTabsContainer.setVisibility(View.VISIBLE);
                                            productDetailsOnlyContainer.setVisibility(View.GONE);
                                            productDescription = documentSnapshot.get("product_description").toString();
                                            productOtherDetails = documentSnapshot.get("product_other_details").toString();

                                            for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {

                                                productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_" + x).toString()));
                                                for (long y = 1; y < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; y++) {
                                                    productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(), documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));

                                                }
                                            }

                                        } else {
                                            productDetailsTabsContainer.setVisibility(View.GONE);
                                            productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                                            productOnlyDescriptionBody.setText(documentSnapshot.get("product_other_details").toString());

                                        }

                                        totalRatings.setText((long) documentSnapshot.get("total_ratings") + " ratings");
                                        for (int x = 0; x < 5; x++) {
                                            TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                                            rating.setText(String.valueOf((long) documentSnapshot.get(5 - x + "_star")));

                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                            int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                            progressBar.setMax(maxProgress);
                                            progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));

                                        }
                                        totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                        averageRating.setText(documentSnapshot.get("average_rating").toString());
                                        //productDetailsViewpager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),productDetailsTablayout.getTabCount(),productDescription,productOtherDetails,productSpecificationModelList));

                                        if (currentUser != null) {
                                            if (DBqueries.myRating.size() == 0) {
                                                DBqueries.loadRatingList(ProductDetails.this);

                                            }
                                            if (DBqueries.cartList.size() == 0) {
                                                DBqueries.loadCartList(ProductDetails.this, loadingDialog, false,badgeCount,new TextView(ProductDetails.this));

                                            }
                                            if (DBqueries.wishList.size() == 0) {
                                                DBqueries.loadWishlist(ProductDetails.this, loadingDialog, false);

                                            } else {
                                                loadingDialog.dismiss();
                                            }

                                        } else {
                                            loadingDialog.dismiss();
                                        }
                                        if (DBqueries.myRateedIds.contains(productID)) {
                                            int index = DBqueries.myRateedIds.indexOf(productID);
                                            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
                                            setRating(initialRating);
                                            //setRating(Integer.parseInt(String.valueOf((long)));
                                        }
                                        if (DBqueries.cartList.contains(productID)) {
                                            ALREADY_ADDED_TO_CART = true;


                                        } else {

                                            ALREADY_ADDED_TO_CART = false;
                                        }

                                        if (DBqueries.wishList.contains(productID)) {
                                            ALREADY_ADDED_TO_WISHLIST = true;
                                            addTowishlist.setSupportImageTintList(getResources().getColorStateList(R.color.red));

                                        } else {
                                            addTowishlist.setSupportImageTintList(getResources().getColorStateList(R.color.white));
                                            ALREADY_ADDED_TO_WISHLIST = false;
                                        }


                                        if (task.getResult().getDocuments().size() < (long)documentSnapshot.get("stock_quantity")){
                                            inStock = true;
                                            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (currentUser == null) {
                                                        signInDialog.show();
                                                    } else {
                                                        if (!running_cart_query) {
                                                            running_cart_query = true;
                                                            if (ALREADY_ADDED_TO_CART) {
                                                                running_cart_query = false;
                                                                Toast.makeText(ProductDetails.this, "Alerady added to cart!", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Map<String, Object> addProduct = new HashMap<>();
                                                                addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
                                                                addProduct.put("list_size", (long) DBqueries.cartList.size() + 1);

                                                                firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_CART")
                                                                        .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {


                                                                            if (DBqueries.cartitemModelList.size() != 0) {

                                                                                DBqueries.cartitemModelList.add(0,new CartitemModel(CartitemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                                                                                        , documentSnapshot.get("product_title").toString()
                                                                                        , (long) documentSnapshot.get("free_coupens")
                                                                                        , documentSnapshot.get("product_price").toString()
                                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                                        , (long) 1
                                                                                        , (long) 0
                                                                                        , (long) 0
                                                                                        //,(boolean)documentSnapshot.get("in_stock")
                                                                                        ,inStock
                                                                                        ,(long)documentSnapshot.get("max-quantity")
                                                                                        ,(long)documentSnapshot.get("stock_quantity")));
                                                                            }

                                                                            ALREADY_ADDED_TO_CART = true;
                                                                            DBqueries.cartList.add(productID);
                                                                            Toast.makeText(ProductDetails.this, "Added to cartlist successfully", Toast.LENGTH_SHORT).show();
                                                                            invalidateOptionsMenu();
                                                                            running_cart_query = false;

                                                                        } else {
                                                                            //addTowishlist.setEnabled(true);
                                                                            running_cart_query = false;
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(ProductDetails.this, error, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });

                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                        }else{
                                            inStock = false;
                                            buyNowBtn.setVisibility(View.GONE);
                                            TextView outOfStock = (TextView) addToCartBtn.getChildAt(0);
                                            outOfStock.setText("Out of stock");
                                            outOfStock.setTextColor(getResources().getColor(R.color.red));
                                            outOfStock.setCompoundDrawables(null,null,null,null);

                                        }

                                    }else{
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetails.this,error,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    //New Added code

//                    for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
//                        productImages.add(documentSnapshot.get("product_image_" + x).toString());
//                    }
//                    ProductimagesAdapter productimagesAdapter = new ProductimagesAdapter(productImages);
//                    productImagesViewPager.setAdapter(productimagesAdapter);
//
//                    productTitle.setText(documentSnapshot.get("product_title").toString());
//                    averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
//                    totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings") + ")ratings");
//                    productPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "-/");
//                    cuttedPrice.setText("Rs." + documentSnapshot.get("cutted_price").toString() + "-/");
//                    if ((boolean) documentSnapshot.get("COD")) {
//                        codIndicator.setVisibility(View.VISIBLE);
//                        tvCodIndicator.setVisibility(View.VISIBLE);
//                    } else {
//                        codIndicator.setVisibility(View.INVISIBLE);
//                        tvCodIndicator.setVisibility(View.INVISIBLE);
//                    }
//                    rewardTitle.setText((long) documentSnapshot.get("free_coupens") + documentSnapshot.get("free_coupen_title").toString());
//                    rewardBody.setText(documentSnapshot.get("free_coupen_body").toString());
//
//                    if ((boolean) documentSnapshot.get("use_tab_layout")) {
//                        productDetailsTabsContainer.setVisibility(View.VISIBLE);
//                        productDetailsOnlyContainer.setVisibility(View.GONE);
//                        productDescription = documentSnapshot.get("product_description").toString();
//                        productOtherDetails = documentSnapshot.get("product_other_details").toString();
//
//                        for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
//
//                            productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_" + x).toString()));
//                            for (long y = 1; y < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; y++) {
//                                productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(), documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));
//
//                            }
//                        }
//
//                    } else {
//                        productDetailsTabsContainer.setVisibility(View.GONE);
//                        productDetailsOnlyContainer.setVisibility(View.VISIBLE);
//                        productOnlyDescriptionBody.setText(documentSnapshot.get("product_other_details").toString());
//
//                    }
//
//                    totalRatings.setText((long) documentSnapshot.get("total_ratings") + " ratings");
//                    for (int x = 0; x < 5; x++) {
//                        TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
//                        rating.setText(String.valueOf((long) documentSnapshot.get(5 - x + "_star")));
//
//                        ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
//                        int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
//                        progressBar.setMax(maxProgress);
//                        progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));
//
//                    }
//                    totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));
//                    averageRating.setText(documentSnapshot.get("average_rating").toString());
//                    //productDetailsViewpager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),productDetailsTablayout.getTabCount(),productDescription,productOtherDetails,productSpecificationModelList));
//
//                    if (currentUser != null) {
//                        if (DBqueries.myRating.size() == 0) {
//                            DBqueries.loadRatingList(ProductDetails.this);
//
//                        }
//                        if (DBqueries.cartList.size() == 0) {
//                            DBqueries.loadCartList(ProductDetails.this, loadingDialog, false,badgeCount,new TextView(ProductDetails.this));
//
//                        }
//                        if (DBqueries.wishList.size() == 0) {
//                            DBqueries.loadWishlist(ProductDetails.this, loadingDialog, false);
//
//                        } else {
//                            loadingDialog.dismiss();
//                        }
//
//                    } else {
//                        loadingDialog.dismiss();
//                    }
//                    if (DBqueries.myRateedIds.contains(productID)) {
//                        int index = DBqueries.myRateedIds.indexOf(productID);
//                        initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
//                        setRating(initialRating);
//                        //setRating(Integer.parseInt(String.valueOf((long)));
//                    }
//                    if (DBqueries.cartList.contains(productID)) {
//                        ALREADY_ADDED_TO_CART = true;
//
//
//                    } else {
//
//                        ALREADY_ADDED_TO_CART = false;
//                    }
//
//                    if (DBqueries.wishList.contains(productID)) {
//                        ALREADY_ADDED_TO_WISHLIST = true;
//                        addTowishlist.setSupportImageTintList(getResources().getColorStateList(R.color.red));
//
//                    } else {
//                        addTowishlist.setSupportImageTintList(getResources().getColorStateList(R.color.white));
//                        ALREADY_ADDED_TO_WISHLIST = false;
//                    }
                    //Added new


                    ///Added new

//
//                    if ((boolean) documentSnapshot.get("in_stock")){
//
////                        addToCartBtn.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View view) {
////                                if (currentUser == null) {
////                                    signInDialog.show();
////                                } else {
////                                    if (!running_cart_query) {
////                                        running_cart_query = true;
////                                        if (ALREADY_ADDED_TO_CART) {
////                                            running_cart_query = false;
////                                            Toast.makeText(ProductDetails.this, "Alerady added to cart!", Toast.LENGTH_SHORT).show();
////                                        } else {
////                                            Map<String, Object> addProduct = new HashMap<>();
////                                            addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
////                                            addProduct.put("list_size", (long) DBqueries.cartList.size() + 1);
////
////                                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_CART")
////                                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
////                                                @Override
////                                                public void onComplete(@NonNull Task<Void> task) {
////                                                    if (task.isSuccessful()) {
////
////
////                                                        if (DBqueries.cartitemModelList.size() != 0) {
////
////                                                            DBqueries.cartitemModelList.add(0,new CartitemModel(CartitemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
////                                                                    , documentSnapshot.get("product_title").toString()
////                                                                    , (long) documentSnapshot.get("free_coupens")
////                                                                    , documentSnapshot.get("product_price").toString()
////                                                                    , documentSnapshot.get("cutted_price").toString()
////                                                                    , (long) 1
////                                                                    , (long) 0
////                                                                    , (long) 0
////                                                                    ,(boolean)documentSnapshot.get("in_stock")
////                                                                    ,(long)documentSnapshot.get("max-quantity")
////                                                                    ,(long)documentSnapshot.get("stock_quantity")));
////                                                        }
////
////                                                        ALREADY_ADDED_TO_CART = true;
////                                                        DBqueries.cartList.add(productID);
////                                                        Toast.makeText(ProductDetails.this, "Added to cartlist successfully", Toast.LENGTH_SHORT).show();
////                                                        invalidateOptionsMenu();
////                                                        running_cart_query = false;
////
////                                                    } else {
////                                                        //addTowishlist.setEnabled(true);
////                                                        running_cart_query = false;
////                                                        String error = task.getException().getMessage();
////                                                        Toast.makeText(ProductDetails.this, error, Toast.LENGTH_SHORT).show();
////                                                    }
////                                                }
////                                            });
////
////                                        }
////                                    }
////                                }
////                            }
////                        });
//
//                    }else{
////                        buyNowBtn.setVisibility(View.GONE);
////                        TextView outOfStock = (TextView) addToCartBtn.getChildAt(0);
////                        outOfStock.setText("Out of stock");
////                        outOfStock.setTextColor(getResources().getColor(R.color.red));
////                        outOfStock.setCompoundDrawables(null,null,null,null);
//
//                    }

                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetails.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewpagerIndicator.setupWithViewPager(productImagesViewPager, true);
        addTowishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    //addTowishlist.setEnabled(false);
                    if (!running_wishlist_query) {
                        running_wishlist_query = true;
                        if (ALREADY_ADDED_TO_WISHLIST) {

                            int index = DBqueries.wishList.indexOf(productID);
                            DBqueries.removeFromWishlist(index, ProductDetails.this);
                            //ALREADY_ADDED_TO_WISHLIST = false;
                            addTowishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#EA5858")));
                        } else {
                            addTowishlist.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBqueries.wishList.size()), productID);
                            addProduct.put("list_size", (long) DBqueries.wishList.size() + 1);

                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {


                                        if (DBqueries.wishlistModelList.size() != 0) {

                                            DBqueries.wishlistModelList.add(new WishlistModel(productID, documentSnapshot.get("product_image_1").toString()
                                                    , documentSnapshot.get("product_title").toString()
                                                    , (long) documentSnapshot.get("free_coupens")
                                                    , documentSnapshot.get("average_rating").toString()
                                                    , (long) documentSnapshot.get("total_ratings")
                                                    , documentSnapshot.get("product_price").toString()
                                                    , documentSnapshot.get("cutted_price").toString()
                                                    , (boolean) documentSnapshot.get("COD")
                                                    //,(boolean)documentSnapshot.get("in_stock")));
                                                    ,inStock));
                                        }

                                        ALREADY_ADDED_TO_WISHLIST = true;
                                        //addTowishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#A1A9A3")));
                                        addTowishlist.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                                        DBqueries.wishList.add(productID);
                                        Toast.makeText(ProductDetails.this, "Added to wishlist successfully", Toast.LENGTH_SHORT).show();


                                    } else {
                                        addTowishlist.setSupportImageTintList(getResources().getColorStateList(R.color.white));
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetails.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_wishlist_query = false;

                                }
                            });

                        }
                    }
                }
            }
        });

        //shows error
        productDetailsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));
        productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Rating Layout

        rateNowContainer = findViewById(R.id.rate_now_container);

        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        if (starPosition != initialRating) {
                            if (!running_rating_query) {
                                running_rating_query = true;
                                setRating(starPosition);
                                Map<String, Object> updateRating = new HashMap<>();

                                if (DBqueries.myRateedIds.contains(productID)) {

                                    TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);

                                    updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                    updateRating.put(starPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition - initialRating, true));

                                } else {

                                    updateRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition + 1, false));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);

                                }
                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Map<String, Object> myRating = new HashMap<>();
                                            if (DBqueries.myRateedIds.contains(productID)) {

                                                myRating.put("rating_" + DBqueries.myRateedIds.indexOf(productID), (long) starPosition + 1);

                                            } else {
                                                myRating.put("list_size", (long) DBqueries.myRateedIds.size() + 1);
                                                myRating.put("product_ID_" + DBqueries.myRateedIds.size(), productID);
                                                myRating.put("rating_" + DBqueries.myRateedIds.size(), (long) starPosition + 1);
                                            }

                                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        if (DBqueries.myRateedIds.contains(productID)) {

                                                            DBqueries.myRating.set(DBqueries.myRateedIds.indexOf(productID), (long) starPosition + 1);

                                                            TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                                            TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                            oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));

                                                        } else {

                                                            DBqueries.myRateedIds.add(productID);
                                                            DBqueries.myRating.add((long) starPosition + 1);

                                                            TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                            totalRatingMiniView.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ")ratings");
                                                            totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                            totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));

                                                            Toast.makeText(ProductDetails.this, "Thank you for rating.", Toast.LENGTH_SHORT).show();
                                                        }


                                                        for (int x = 0; x < 5; x++) {
                                                            TextView ratingfigures = (TextView) ratingsNoContainer.getChildAt(x);

                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                                            int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                            progressBar.setMax(maxProgress);

                                                            progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));

                                                        }
                                                        initialRating = starPosition;
                                                        averageRating.setText(calculateAverageRating(0, true));
                                                        averageRatingMiniView.setText(calculateAverageRating(0, true));

                                                        if (DBqueries.wishList.contains(productID) && DBqueries.wishlistModelList.size() != 0) {
                                                            int index = DBqueries.wishList.indexOf(productID);
                                                            DBqueries.wishlistModelList.get(index).setRating(averageRating.getText().toString());
                                                            DBqueries.wishlistModelList.get(index).setTotalRatings(Long.parseLong(totalRatingsFigure.getText().toString()));

                                                        }

                                                    } else {
                                                        setRating(initialRating);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetails.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    running_rating_query = false;

                                                }
                                            });

                                        } else {
                                            running_rating_query = false;
                                            setRating(initialRating);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ProductDetails.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }

                }
            });
        }
        //Rating Layout

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    DeliveryActivity.fromCart = false;
                    loadingDialog.show();
                    productDetailsActivity = ProductDetails.this;
                    DeliveryActivity.cartitemModelList = new ArrayList<>();
                    DeliveryActivity.cartitemModelList.add(new CartitemModel(CartitemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                            , documentSnapshot.get("product_title").toString()
                            , (long) documentSnapshot.get("free_coupens")
                            , documentSnapshot.get("product_price").toString()
                            , documentSnapshot.get("cutted_price").toString()
                            , (long) 1
                            , (long) 0
                            , (long) 0
                           // ,(boolean)documentSnapshot.get("in_stock")
                            ,inStock
                            ,(long)documentSnapshot.get("max-quantity")
                            ,(long)documentSnapshot.get("stock_quantity")));
                    DeliveryActivity.cartitemModelList.add(new CartitemModel(CartitemModel.TOTAL_AMOUNT));

                    if (DBqueries.addressesModelList.size() == 0) {
                        DBqueries.loadAddresses(ProductDetails.this, loadingDialog);
                    }else {
                        loadingDialog.dismiss();
                        Intent deliveryIntent = new Intent(ProductDetails.this, DeliveryActivity.class);
                        startActivity(deliveryIntent);
                    }
                }
            }
        });


        ///COUPEN REDEEM DIALOG
        Dialog checkCoupenPriceDialog = new Dialog(ProductDetails.this);
        checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
        checkCoupenPriceDialog.setCancelable(true);
        checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toggleRecyclerView = checkCoupenPriceDialog.findViewById(R.id.toggle_recyclerview);
        coupensRecyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerView);
        selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);

        coupenTitle = checkCoupenPriceDialog.findViewById(R.id.coupen_title_reward);
        coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.coupen_validity);
        coupenBody = checkCoupenPriceDialog.findViewById(R.id.coupen_body);


        TextView originalPrice = checkCoupenPriceDialog.findViewById(R.id.original_price);
        TextView discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetails.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        coupensRecyclerView.setLayoutManager(layoutManager);

        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("Cashback","Till 20th June 2022","Get 10% Cashback"));
        rewardModelList.add(new RewardModel("Discount","Till 25th June 2022","Get 15% Cashback"));
        rewardModelList.add(new RewardModel("Cashback","Till 27th June 2022","Get 10% Cashback"));
        rewardModelList.add(new RewardModel("Cashback","Till 20th June 2022","Get 10% Cashback"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList,true);
        coupensRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRecyclerView();
            }
        });
        coupenRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkCoupenPriceDialog.show();

            }
        });
     //////COUPEN REDEEM DIALOG END


        ///SIGNIN DIALOG

        signInDialog = new Dialog(ProductDetails.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.signin_btn);
        Button dialogSignUnBtn = signInDialog.findViewById(R.id.signup_btn);
        Intent registerIntent = new Intent(ProductDetails.this, Register.class);

        //Un Comment This to show Dialog Box

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });

        dialogSignUnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });
        ///SIGNIN DIALOG

    }
    public static void showDialogRecyclerView(){
        if (coupensRecyclerView.getVisibility() == View.GONE){
            coupensRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);
        }else{
            coupensRecyclerView.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {


            if (DBqueries.myRating.size() == 0) {
                DBqueries.loadRatingList(ProductDetails.this);

            }


            if (DBqueries.wishList.size() == 0) {
                DBqueries.loadWishlist(ProductDetails.this, loadingDialog, false);

            } else {
                loadingDialog.dismiss();
            }

        } else {
            loadingDialog.dismiss();
        }
        if (DBqueries.myRateedIds.contains(productID)) {
            int index = DBqueries.myRateedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
            setRating(initialRating);
            //setRating(Integer.parseInt(String.valueOf((long)));
        }

        if (DBqueries.cartList.contains(productID)) {
            ALREADY_ADDED_TO_CART = true;
        } else {
            ALREADY_ADDED_TO_CART = false;
        }
        if (DBqueries.wishList.contains(productID)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addTowishlist.setSupportImageTintList(getResources().getColorStateList(R.color.red));

        } else {
            addTowishlist.setSupportImageTintList(getResources().getColorStateList(R.color.white));
            ALREADY_ADDED_TO_WISHLIST = false;
        }
        invalidateOptionsMenu();
    }

    public static void setRating(int starPosition) {

        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition) {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));

            }
        }

    }

    private String calculateAverageRating(long currentUserRating, boolean update) {
        Double totalStars = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5 - x);
            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString()) * x);
        }
        totalStars = totalStars + currentUserRating;
        if (update) {
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0, 3);

        } else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString()) + 1)).substring(0, 3);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);

         cartItem = menu.findItem(R.id.maincart);


            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.carticon);
            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);

        if (currentUser != null){
            if (DBqueries.cartList.size() == 0) {
                DBqueries.loadCartList(ProductDetails.this,loadingDialog, false,badgeCount,new TextView(ProductDetails.this));

            }else{
                badgeCount.setVisibility(View.VISIBLE);
                if (DBqueries.cartList.size() < 99){
                    badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                }else {
                    badgeCount.setText("99");
                }
            }
        }
            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        Intent cartIntent = new Intent(ProductDetails.this, MainActivity.class);
                        showCart = true;
                        startActivity(cartIntent);
                    }
                }
            });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            productDetailsActivity = null;
            finish();
            return true;
        } else if (id == R.id.searchicon) {
            //Todo
            return true;

        } else if (id == R.id.maincart) {
            if (currentUser == null) {
                signInDialog.show();
            } else {
                Intent cartIntent = new Intent(ProductDetails.this, MainActivity.class);
                showCart = true;
                startActivity(cartIntent);
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        productDetailsActivity = null;
        super.onBackPressed();
    }
}