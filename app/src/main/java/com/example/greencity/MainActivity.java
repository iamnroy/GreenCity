package com.example.greencity;


import static com.example.greencity.Register.setSignUpFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
//import android.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greencity.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int HOME_FRAGEMENT = 0;
    private static final int CART_FRAGEMENT = 1;
    private static final int ORDERS_FRAFEMENT = 2;
    private static final int WISHLIST_FRAGMENT= 3;
    private static final int REWARDS_FRAGMENT= 4;
    private static final int ACCOUNT_FRAGMENT= 5;
    public static Boolean showCart = false;


    private FrameLayout framelayout;
    private ImageView actionBarLogo;

    private int currentFragement = -1;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private Window window;
    private Dialog signInDialog;
    private FirebaseUser currentUser;
    public static DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           // setContentView(R.layout.activity_main);
        setContentView(R.layout.fragment_home2);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        actionBarLogo = findViewById(R.id.actionbar_logo);
       //toolbar = findViewById(R.id.toolbar);
         binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
          setSupportActionBar(toolbar);
         //getSupportActionBar().setDisplayShowTitleEnabled(false);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);



       setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        drawer = binding.drawerLayout;
        //DrawerLayout drawer = findViewById(R.id.drawer_layout);

        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //NavController navController = Navigation.findNavController(this, R.id.main_framelayout);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.getMenu().getItem(0).setChecked(true); //index might be changed to 3


        ///COMMENT THESE 3 to run Navigation_bar
        framelayout =findViewById(R.id.main_framelayout);
       // framelayout =findViewById(R.id.app_bar_main);
        //setFragment(new HomeFragment(), HOME_FRAGEMENT);

        if (showCart) {
            //drawer.setDrawerLockMode(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotoFragment("My Cart", new MyCartFragment(), -2);
        } else {
           // ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,"Open navigationdrawer","Close navigation drawer");
            setFragment(new HomeFragment(), HOME_FRAGEMENT);
        }


        signInDialog = new Dialog(MainActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.signin_btn);
        Button dialogSignUnBtn = signInDialog.findViewById(R.id.signup_btn);
        Intent registerIntent = new Intent(MainActivity.this,Register.class);

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

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null){
            navigationView.getMenu().getItem(navigationView.getMenu().size() -1).setEnabled(false);
        }else{
            navigationView.getMenu().getItem(navigationView.getMenu().size() -1).setEnabled(true);

        }
    }

    //BACK BTN
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            if (currentFragement == HOME_FRAGEMENT) {
                currentFragement = -1;
                super.onBackPressed();
            }else{

                if(showCart){
                    showCart = false;
                    finish();
                }else {
                    actionBarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGEMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }
    //BACK BTN

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

//            Dialog signInDialog = new Dialog(MainActivity.this);
//            signInDialog.setContentView(R.layout.sign_in_dialog);
//            signInDialog.setCancelable(true);
//            signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//
//            Button dialogSignInBtn = signInDialog.findViewById(R.id.signin_btn);
//            Button dialogSignUnBtn = signInDialog.findViewById(R.id.signup_btn);
//            Intent registerIntent = new Intent(MainActivity.this,Register.class);
//
//            //Un Comment This to show Dialog Box
//
//            dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    signInDialog.dismiss();
//                    setSignUpFragment = false;
//                    startActivity(registerIntent);
//                }
//            });
//
//            dialogSignUnBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    signInDialog.dismiss();
//                    setSignUpFragment = true;
//                    startActivity(registerIntent);
//                }
//            });
            if (currentUser == null) {
                signInDialog.show();
            }else {
                gotoFragment("My Cart", new MyCartFragment(), CART_FRAGEMENT);
            }
           // gotoFragment("My Oders", new MyOrdersFragment(),ORDERS_FRAFEMENT);
            //gotoFragment("My Wishlist",new MyWishlistFragment(),WISHLIST_FRAGMENT);
           // gotoFragment("My Rewards",new MyRewardsFragment(),REWARDS_FRAGMENT);
            //gotoFragment("My Account",new MyAccountFragment(),ACCOUNT_FRAGMENT);

            return true;

        }else if (id == android.R.id.home){
            if (showCart){
                showCart = false;
                finish();
                return true;
            }
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

        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;

        if (currentUser != null) {

            int id = item.getItemId();
            if (id == R.id.nav_mycity) {
                //getSupportActionBar().setDisplayShowTitleEnabled(false);
                actionBarLogo.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
                setFragment(new HomeFragment(), HOME_FRAGEMENT);
            } else if (id == R.id.nav_my_orders) {
                gotoFragment("My Oders", new MyOrdersFragment(), ORDERS_FRAFEMENT);

            } else if (id == R.id.nav_rewards) {
                gotoFragment("My Rewards", new MyRewardsFragment(), REWARDS_FRAGMENT);


            } else if (id == R.id.nav_cart) {
                gotoFragment("My Cart", new MyCartFragment(), CART_FRAGEMENT);
                //  gotoFragment("My Oders", new MyOrdersFragment(),ORDERS_FRAFEMENT);
                // gotoFragment("My Wishlist",new MyWishlistFragment(),WISHLIST_FRAGMENT);
                // gotoFragment("My Rewards",new MyRewardsFragment(),REWARDS_FRAGMENT);
                //gotoFragment("My Account",new MyAccountFragment(),ACCOUNT_FRAGMENT);


            } else if (id == R.id.nav_wishlist) {
                gotoFragment("My Wishlist", new MyWishlistFragment(), WISHLIST_FRAGMENT);

            } else if (id == R.id.nav_myaccount) {
                gotoFragment("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);


            } else if (id == R.id.nav_signout) {
                FirebaseAuth.getInstance().signOut();
                Intent registerIntent = new Intent(MainActivity.this,Register.class);
                startActivity(registerIntent);
                finish();
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }else{
            drawer.closeDrawer(GravityCompat.START);
            signInDialog.show();
            return false;
        }
//        DrawerLayout drawer = binding.drawerLayout;
//        navigationView = binding.navView;
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setOpenableLayout(drawer)
//                .build();

    }

    private void setFragment(Fragment fragment,int fragementNo ){
       // if (fragementNo != currentFragement) {

        if (fragementNo == REWARDS_FRAGMENT){
           window.setStatusBarColor(Color.parseColor("#3794C9"));
           //toolbar.setBackgroundColor(Color.parseColor("000000"));
        }else{
            window.setStatusBarColor(getResources().getColor(R.color.primary));
           // toolbar.setBackgroundColor(getResources().getColor(R.color.primary));

        }
            currentFragement = fragementNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(framelayout.getId(), fragment);
            fragmentTransaction.commit();
       // }
    }
}