package com.example.greencity;

import static com.example.greencity.DBqueries.categoryModelList;
import static com.example.greencity.DBqueries.firebaseFirestore;
import static com.example.greencity.DBqueries.homePageModelList;
import static com.example.greencity.DBqueries.loadCategories;
import static com.example.greencity.DBqueries.loadFragmentData;

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

    private RecyclerView catRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private HomePageAdapter adapter;
    private ImageView noInternetConnection;
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
        noInternetConnection = view.findViewById(R.id.no_internet_connection);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected() == (true)) {
            noInternetConnection.setVisibility(View.GONE);
            catRecyclerView = view.findViewById(R.id.category_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            catRecyclerView.setLayoutManager(layoutManager);

            //categoryModelList = new ArrayList<CategoryModel>();

            categoryAdapter = new CategoryAdapter(categoryModelList);
            catRecyclerView.setAdapter(categoryAdapter);

            if (categoryModelList.size() == 0){
                loadCategories(categoryAdapter,getContext());
            }else{
                categoryAdapter.notifyDataSetChanged();
            }

            homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
            LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
            testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            homePageRecyclerView.setLayoutManager(testingLayoutManager);
            //List<HomePageModel> homePageModelList = new ArrayList<>();
            adapter = new HomePageAdapter(homePageModelList);
            homePageRecyclerView.setAdapter(adapter);

            if (homePageModelList.size() == 0){
                loadFragmentData(adapter,getContext());
            }else{
                categoryAdapter.notifyDataSetChanged();
            }


        }else{
            Glide.with(this).load(R.drawable.no_internet_connection).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
        }


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