package com.example.greencity;

import static com.example.greencity.DBqueries.categoryModelList;
import static com.example.greencity.DBqueries.firebaseFirestore;
import static com.example.greencity.DBqueries.lists;
import static com.example.greencity.DBqueries.loadCategories;
import static com.example.greencity.DBqueries.loadFragmentData;
import static com.example.greencity.DBqueries.loadedCategoriesNames;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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

import com.bumptech.glide.Glide;
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

    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView categoryRecyclerView;
    private List<CategoryModel> categoryModeFakelList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private HomePageAdapter adapter;
    private ImageView noInternetConnection;
    private Button retryBtn;
    //private List<CategoryModel> categoryModelList;
   // private FirebaseFirestore firebaseFirestore;

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
        //change fragment name here to check errors
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);
        categoryRecyclerView = view.findViewById(R.id.category_view);
        homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
        retryBtn = view.findViewById(R.id.retry_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);

        //Categories Fake List
        categoryModeFakelList.add(new CategoryModel("null",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        //Categories Fake List

        //Home page fake liste
        List<SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#D5E5F3"));
        sliderModelFakeList.add(new SliderModel("null","#D5E5F3"));
        sliderModelFakeList.add(new SliderModel("null","#D5E5F3"));
        sliderModelFakeList.add(new SliderModel("null","#D5E5F3"));
        sliderModelFakeList.add(new SliderModel("null","#D5E5F3"));

        List<HorizontalProductModel> horizontalProductModelFakeList = new ArrayList<>();
        horizontalProductModelFakeList.add(new HorizontalProductModel("","","","",""));
        horizontalProductModelFakeList.add(new HorizontalProductModel("","","","",""));
        horizontalProductModelFakeList.add(new HorizontalProductModel("","","","",""));
        horizontalProductModelFakeList.add(new HorizontalProductModel("","","","",""));
        horizontalProductModelFakeList.add(new HorizontalProductModel("","","","",""));
        horizontalProductModelFakeList.add(new HorizontalProductModel("","","","",""));
        horizontalProductModelFakeList.add(new HorizontalProductModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#D5E5F3"));
        homePageModelFakeList.add(new HomePageModel(2,"","#D5E5F3",horizontalProductModelFakeList,new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#D5E5F3",horizontalProductModelFakeList));

        //Home Page fake List

        categoryAdapter = new CategoryAdapter(categoryModeFakelList);

        adapter = new HomePageAdapter(homePageModelFakeList);

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected() == (true)) {
            //MainActivity.drawer.setDrawerLockMode(0);
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);
//            catRecyclerView = view.findViewById(R.id.category_view);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//            catRecyclerView.setLayoutManager(layoutManager);
//            categoryAdapter = new CategoryAdapter(categoryModelList);
//            catRecyclerView.setAdapter(categoryAdapter);

            if (categoryModelList.size() == 0){
                loadCategories(categoryRecyclerView,getContext());
            }else{
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }
            categoryRecyclerView.setAdapter(categoryAdapter);


//            homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
//            LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
//            testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//            homePageRecyclerView.setLayoutManager(testingLayoutManager);
            //List<HomePageModel> homePageModelList = new ArrayList<>();

            if (lists.size() == 0){
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());
                //adapter = new HomePageAdapter(lists.get(0));
                loadFragmentData(homePageRecyclerView,getContext(),0,"Home");
            }else{
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }
            homePageRecyclerView.setAdapter(adapter);

            //homePageRecyclerView.setAdapter(adapter);
        }else{
            //MainActivity.drawer.setDrawerLockMode(1);
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.no_internet_connection).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);


        }

        //Refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.success),getContext().getResources().getColor(R.color.success),getContext().getResources().getColor(R.color.success));
                swipeRefreshLayout.setRefreshing(true);
                reloadPage();
//                networkInfo = connectivityManager.getActiveNetworkInfo();
//
//                categoryModelList.clear();
//                lists.clear();
//                loadedCategoriesNames.clear();
//                if(networkInfo != null && networkInfo.isConnected() == (true)) {
//                    noInternetConnection.setVisibility(View.GONE);
//                    retryBtn.setVisibility(View.GONE);
//                    categoryRecyclerView.setVisibility(View.VISIBLE);
//                    homePageRecyclerView.setVisibility(View.VISIBLE);
//                    categoryRecyclerView.setAdapter(categoryAdapter);
//                    homePageRecyclerView.setAdapter(adapter);
//
//                    loadCategories(categoryRecyclerView,getContext());
//                    loadedCategoriesNames.add("HOME");
//                    lists.add(new ArrayList<HomePageModel>());
//                    loadFragmentData(homePageRecyclerView,getContext(),0,"Home");
//
//                }else{
//                    categoryRecyclerView.setVisibility(View.GONE);
//                    homePageRecyclerView.setVisibility(View.GONE);
//                    Glide.with(getContext()).load(R.drawable.no_internet_connection).into(noInternetConnection);
//                    noInternetConnection.setVisibility(View.VISIBLE);
//                    retryBtn.setVisibility(View.VISIBLE);
//                    swipeRefreshLayout.setRefreshing(false);
//                }
            }
        });
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadPage();
            }
        });

        return view;
    }

    private void reloadPage(){
        networkInfo = connectivityManager.getActiveNetworkInfo();

        categoryModelList.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        if(networkInfo != null && networkInfo.isConnected() == (true)) {
           // MainActivity.drawer.setDrawerLockMode(0);

            noInternetConnection.setVisibility(View.GONE);
            categoryAdapter = new CategoryAdapter(categoryModeFakelList);
            adapter = new HomePageAdapter(homePageModelFakeList);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);
            categoryRecyclerView.setAdapter(categoryAdapter);
            homePageRecyclerView.setAdapter(adapter);

            loadCategories(categoryRecyclerView,getContext());

            loadedCategoriesNames.add("HOME");
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(homePageRecyclerView,getContext(),0,"Home");

        }else{
            //MainActivity.drawer.setDrawerLockMode(1);

            Toast.makeText(getContext(),"No Internet Connection!!!",Toast.LENGTH_SHORT).show();
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(getContext()).load(R.drawable.no_internet_connection).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
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