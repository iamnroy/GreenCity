package com.example.greencity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greencity.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int HOME_FRAGEMENT = 0;
    private static final int CART_FRAGEMENT = 1;
    private static final int ORDERS_FRAFEMENT = 2;


    private FrameLayout framelayout;
    private ImageView actionBarLogo;

    private static int currentFragement = -1;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            //setContentView(R.layout.activity_main);
        setContentView(R.layout.fragment_home2);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        actionBarLogo = findViewById(R.id.actionbar_logo);
       //toolbar = findViewById(R.id.toolbar);
         binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
          //setSupportActionBar(toolbar);
         //getSupportActionBar().setDisplayShowTitleEnabled(false);


       setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.getMenu().getItem(0).setChecked(true); //index might be changed to 3


        ///COMMENT THESE 3 to run Navigation_bar
        framelayout =findViewById(R.id.main_framelayout);
        //framelayout =findViewById(R.id.app_bar_main);

        setFragment(new HomeFragment(),HOME_FRAGEMENT);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragement == HOME_FRAGEMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);

        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.searchicon){
            //Todo
            return true;
        }else if (id == R.id.notificationicon)
        {
            //Todo
            return true;

        }else if (id == R.id.maincart){
          //  gotoFragment("My Cart",new MyCartFragment(),CART_FRAGEMENT);
            gotoFragment("My Oders", new MyOrdersFragment(),ORDERS_FRAFEMENT);

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title, Fragment fragment, int fragmentNo) {
      //  actionBarLogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment,fragmentNo);
        if (fragmentNo == CART_FRAGEMENT) {

            navigationView.getMenu().getItem(3).setChecked(true);
        }
    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//        if (id == R.id.nav_cart) {
//
//        }else if (id == R.id.nav_gallery){
//
//        }else if (id == R.id.nav_slideshow){
//
//        }else if (id == R.id.nav_myaccount){
//
//        }
//    }

//private void setFragment(Fragment fragment,int fragementNo ){
//        currentFragement = fragementNo;
//    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//    fragmentTransaction.replace(framelayout.getId(),fragment);
//    fragmentTransaction.commit();
//}

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_mycity){
            //getSupportActionBar().setDisplayShowTitleEnabled(false);
            actionBarLogo.setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
            setFragment(new HomeFragment(),HOME_FRAGEMENT);
        }else if (id == R.id.nav_my_orders){
            gotoFragment("My Oders", new MyOrdersFragment(),ORDERS_FRAFEMENT);

        }else if (id == R.id.nav_rewards){

        }else if (id == R.id.nav_cart){
           // gotoFragment("My Cart",new MyCartFragment(),CART_FRAGEMENT);
            gotoFragment("My Oders", new MyOrdersFragment(),ORDERS_FRAFEMENT);


        }else if (id == R.id.nav_wishlist){

        }else if (id == R.id.nav_myaccount){

        }else if (id == R.id.nav_signout){

        }
//        DrawerLayout drawer = binding.drawerLayout;
//        navigationView = binding.navView;
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setOpenableLayout(drawer)
//                .build();
        return true;
    }

    private void setFragment(Fragment fragment,int fragementNo ){
       // if (fragementNo != currentFragement) {
            currentFragement = fragementNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(framelayout.getId(), fragment);
            fragmentTransaction.commit();
       // }
    }
}