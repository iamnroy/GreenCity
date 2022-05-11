package com.example.greencity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView catRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private HomePageAdapter adapter;
    private List<CategoryModel> categoryModelList;
    private FirebaseFirestore firebaseFirestore;

    //Try BANNER SLIDER--------
//    private List<SliderModel> sliderModelList;
//    private ViewPager bannersliderviewpager;
//    private int currentPage = 2;
//    private Timer timer;
//    final  private long DelayTime = 3000;
//    final private long PeriodTime = 3000;

    //Try--------
//    ///STRIP Layout
//    private ImageView stripAdImage;
//    private ConstraintLayout stripAdContainer;

    ///STRIP Layout

    ////HORIZONTAL Product

//    private TextView HorizontalLayoutTitle;
//    private Button horizontalviewAllBtn;
//    private RecyclerView horizontalRecycle;
    ///Horizontal product Layout


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        catRecyclerView = view.findViewById(R.id.category_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        catRecyclerView.setLayoutManager(layoutManager);

        categoryModelList = new ArrayList<CategoryModel>();

        categoryAdapter = new CategoryAdapter(categoryModelList);
        catRecyclerView.setAdapter(categoryAdapter);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                            }
                            categoryAdapter.notifyDataSetChanged();

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//        categoryAdapter = new CategoryAdapter(categoryModelList);
//        catRecyclerView.setAdapter(categoryAdapter);
//        categoryAdapter.notifyDataSetChanged();

        //Banner Try
        //bannersliderviewpager = view.findViewById(R.id.banner_slider_view);




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

        ////UNCOMMENT////
//
//        List<HorizontalProductModel> horizontalProductModelList = new ArrayList<>();
//        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.can, "list2", "newDescsc", "RS.1234"));
//        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.can, "list2", "newDescsc", "RS.1234"));
//        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.can, "list2", "newDescsc", "RS.1234"));
//        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ktm, "list2", "newDescsc", "RS.1234"));
//        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ic_email, "list2", "newDescsc", "RS.1234"));
//        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ktm, "list2", "newDescsc", "RS.1234"));
//        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.can, "list2", "newDescsc", "RS.1234"));
//        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ktm, "list2", "newDescsc", "RS.1234"));
//        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ic_email, "list2", "newDescsc", "RS.1234"));
//        horizontalProductModelList.add(new HorizontalProductModel(R.drawable.ktm, "list2", "newDescsc", "RS.1234"));


        //UNCOMMENT///


//        HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductModelList);
//
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//       // LinearLayoutManage.setOrientation(LinearLayoutManager.HORIZONTAL);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        horizontalRecycle.setLayoutManager(linearLayoutManager);
//
//        horizontalRecycle.setAdapter(horizontalProductScrollAdapter);
//        horizontalProductScrollAdapter.notifyDataSetChanged();

        //Horizontal Product Layout End

        ////Grid View Product Layout Start

//        TextView griLayoutTitle = view.findViewById(R.id.grid_pro_layout_title);
//        Button gridLayoutViewAllBtn = view.findViewById(R.id.grid_pro_layout_view_btn);
//        GridView gridView = view.findViewById(R.id.grid_pro_layout_gridView);
//
//        gridView.setAdapter(new GridProductLayoutAdapter(horizontalProductModelList));

        ///Grid View Product End

        //////////////////////////
        homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);
        List<HomePageModel> homePageModelList = new ArrayList<>();
        adapter = new HomePageAdapter(homePageModelList);
        homePageRecyclerView.setAdapter(adapter);

        firebaseFirestore.collection("CATEGORIES")
                .document("HOME")
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

//                                if ((long)documentSnapshot.get("view_type") == 0){
//                                    List<SliderModel> sliderModelList = new ArrayList<>();
//                                    long no_of_banners = (long)documentSnapshot.get("no_of_banners");
//                                    for (long x = 1;x < no_of_banners + 1;x++){
//                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_"+x).toString()
//                                                ,documentSnapshot.get("banner_"+x+"_background").toString()));
//                                    }
//                                    homePageModelList.add(new HomePageModel(0,sliderModelList));

                               // }else
                                    if ((long)documentSnapshot.get("view_type") == 1){
                                    homePageModelList.add(new HomePageModel(1,documentSnapshot.get("strip_ad_banner").toString()
                                            ,documentSnapshot.get("background").toString()));

                                }else if ((long)documentSnapshot.get("view_type") == 2){
                                        List<HorizontalProductModel> horizontalProductModelList = new ArrayList<>();
                                        long no_of_products = (long)documentSnapshot.get("no_of_products");
                                    for (long x = 1;x < no_of_products + 1;x++){
                                       horizontalProductModelList.add(new HorizontalProductModel(documentSnapshot.get("product_ID_"+x).toString()
                                       ,documentSnapshot.get("product_image_"+x).toString()
                                       ,documentSnapshot.get("product_title_"+x).toString()
                                       ,documentSnapshot.get("product_subtitle_"+x).toString()
                                       ,documentSnapshot.get("product_price_"+x).toString()));

                                    }
                                    homePageModelList.add(new HomePageModel(2,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),horizontalProductModelList));

                                }else if ((long)documentSnapshot.get("view_type") == 3){

                                }

                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        ////////////////////////////
        return view;
    }

    ///Banner TRYYYY
//    private void pageLooper(){
//        if (currentPage == sliderModelList.size() -2){
//            currentPage = 2;
//            bannersliderviewpager.setCurrentItem(currentPage,false);
//        }
//
//        if (currentPage == 1){
//            currentPage = sliderModelList.size() -3;
//            bannersliderviewpager.setCurrentItem(currentPage,false);
//        }
//    }
//
//    private void startBannerSlideShow(){
//        Handler handler = new Handler();
//        Runnable update = new Runnable() {
//            @Override
//            public void run() {
//                if (currentPage >= sliderModelList.size()){
//                    currentPage = 1;
//                }
//              bannersliderviewpager.setCurrentItem(currentPage++,true);
//            }
//        };
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(update);
//            }
//        },DelayTime,PeriodTime);
//
//    }
//
//    private void stopBannerSlidShow(){
//        timer.cancel();
//    }
//    //BANNER TRYyyy
}