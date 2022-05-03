package com.example.greencity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.example.greencity.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_category);
        setContentView(R.layout.fragment_home2);


        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra("CategoryName");
        //getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        categoryRecyclerView = findViewById(R.id.category_recyclerview);


        //Banner Try
        //bannersliderviewpager = view.findViewById(R.id.banner_slider_view);

        List<SliderModel>sliderModelList = new ArrayList<SliderModel>();

        sliderModelList.add(new SliderModel(R.mipmap.usericon,"ADF9C3"));
        sliderModelList.add(new SliderModel(R.mipmap.carticon,"ADF9C3"));
        sliderModelList.add(new SliderModel(R.mipmap.green_email,"ADF9C3"));

        sliderModelList.add(new SliderModel(R.mipmap.home,"ADF9C3"));
        sliderModelList.add(new SliderModel(R.mipmap.usericon,"ADF9C3"));

        sliderModelList.add(new SliderModel(R.mipmap.carticon,"ADF9C3"));
        sliderModelList.add(new SliderModel(R.mipmap.green_email,"ADF9C3"));
        sliderModelList.add(new SliderModel(R.mipmap.home,"ADF9C3"));


        //SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);

//        bannersliderviewpager.setAdapter(sliderAdapter);
//       bannersliderviewpager.setClipToPadding(false);
//        bannersliderviewpager.setPageMargin(20);
//
//        bannersliderviewpager.setCurrentItem(currentPage);
//
//        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                currentPage = position;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                if (state == ViewPager.SCROLL_STATE_IDLE){
//                    pageLooper();
//                }
//            }
//        };
//        bannersliderviewpager.addOnPageChangeListener(onPageChangeListener);
//
//        startBannerSlideShow();
//
//        bannersliderviewpager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                pageLooper();
//                startBannerSlideShow();
//                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
//                    startBannerSlideShow();
//                }
//                return false;
//            }
//        });
        /////Try Banner Slider

        //Strip AD

//        stripAdImage = view.findViewById(R.id.strip_ad_image);
//        stripAdContainer = view.findViewById(R.id.strip_ad_container);
//
//        stripAdImage.setImageResource(R.drawable.ktmbanner);
//        stripAdImage.setBackgroundColor(Color.parseColor("#000000"));

        //Strip Ad

        //Horizonatal Product Layout
//        HorizontalLayoutTitle = view.findViewById(R.id.horizontal_scroll_layout);
//        horizontalviewAllBtn = view.findViewById(R.id.horizontal_scroll_viewAllbtn);
//        horizontalRecycle = view.findViewById(R.id.horizontal_scroll_layout_rec);

        List<HorizontalProductModel> horizontalProductModelList = new ArrayList<>();
        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.can,"list2","newDescsc","RS.1234"));
        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.can,"list2","newDescsc","RS.1234"));
        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.can,"list2","newDescsc","RS.1234"));
        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ktm,"list2","newDescsc","RS.1234"));
        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ic_email,"list2","newDescsc","RS.1234"));
        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ktm,"list2","newDescsc","RS.1234"));
        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.can,"list2","newDescsc","RS.1234"));
        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ktm,"list2","newDescsc","RS.1234"));
        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ic_email,"list2","newDescsc","RS.1234"));
        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ktm,"list2","newDescsc","RS.1234"));



        ///Grid View Product End

        //////////////////////////
        categoryRecyclerView = findViewById(R.id.category_recyclerview);

        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLayoutManager);

        List<HomePageModel> homePageModelList = new ArrayList<>();
        //homePageModelList.add(new HomePageModel(0,sliderModelList));
        homePageModelList.add(new HomePageModel(1,R.drawable.ktmbanner,"#000000"));
        homePageModelList.add(new HomePageModel(2,"Deals of The Day",horizontalProductModelList));
        homePageModelList.add(new HomePageModel(3,"Deals of The Day",horizontalProductModelList));
        homePageModelList.add(new HomePageModel(1,R.drawable.ktmbanner,"#ffff00"));
        homePageModelList.add(new HomePageModel(3,"Deals of The Day",horizontalProductModelList));
        homePageModelList.add(new HomePageModel(2,"Deals of The Day",horizontalProductModelList));
        homePageModelList.add(new HomePageModel(1,R.drawable.ktmbanner,"#ffff00"));
        // homePageModelList.add(new HomePageModel(0,sliderModelList));

        HomePageAdapter adapter = new HomePageAdapter(homePageModelList);

        categoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //setSupportActionBar(binding.appBarMain.toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.searchicon){
            //Todo
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}