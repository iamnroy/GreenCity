package com.example.greencity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toolbar;
//import android.support.v7.widget.Toolbar;



import com.example.greencity.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ProductDetails extends AppCompatActivity {

    private ViewPager productImagesViewPager;
    private TableLayout viewpagerIndicator;

    private ViewPager productDetailsViewpager;
    private TabLayout productDetailsTablayout;

    //Rating Layout
    private LinearLayout rateNowContainer;
    //Rating Layout

    private static boolean ALREADY_ADDED_TO_WISHLIST = false;
    private FloatingActionButton addTowishlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager = findViewById(R.id.product_images_viewpager);
        viewpagerIndicator = findViewById(R.id.viewpager_indicator);
        addTowishlist = findViewById(R.id.add_to_wishlist);
        productDetailsViewpager = findViewById(R.id.product_details_view_pager);
        productDetailsTablayout = findViewById(R.id.product_dtails_tablayout);

        List<Integer> productImage = new ArrayList<>();
        productImage.add(R.drawable.can);
        productImage.add(R.drawable.banner);
        productImage.add(R.drawable.can);

        ProductimagesAdapter productimagesAdapter = new ProductimagesAdapter(productImage);
        productImagesViewPager.setAdapter(productimagesAdapter);


       // viewpagerIndicator.setupWithViewPager(productImagesViewPager,true);
        addTowishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ALREADY_ADDED_TO_WISHLIST){
                    ALREADY_ADDED_TO_WISHLIST = false;
                    addTowishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#EA5858")));
                }else {
                    ALREADY_ADDED_TO_WISHLIST = true;
                    addTowishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#A1A9A3")));
                }
            }
        });

        //shows error
         //productDetailsViewpager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),productDetailsTablayout.getTabCount()));
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
        for (int x = 0; x < rateNowContainer.getChildCount(); x++){
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setRating(starPosition);
                }
            });
        }
        //Rating Layout
    }

    private void setRating(int starPosition) {
        for (int x = 0; x < rateNowContainer.getChildCount();x++){
            ImageView starBtn = (ImageView)rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition){
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            //Todo
            finish();
            return true;
        }else if (id == R.id.searchicon)
        {
            //Todo
            return true;

        }else if (id == R.id.maincart){
            //Todo
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}