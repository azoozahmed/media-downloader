package com.example.mydownloaderapplication.Mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.example.mydownloaderapplication.Historyactivity.HistoryActivity;
import com.example.mydownloaderapplication.Mainactivity.Fragments.InstagramFragment.InstagramFragment;
import com.example.mydownloaderapplication.Mainactivity.Fragments.TiktokFragment;
import com.example.mydownloaderapplication.Mainactivity.Fragments.TwitchFragment;
import com.example.mydownloaderapplication.Mainactivity.Fragments.TwitterFragment;
import com.example.mydownloaderapplication.Mainactivity.Fragments.YoutubeFragment;
import com.example.mydownloaderapplication.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
   ImageView instagramBtn;
    ImageView youtubeBtn;
    ImageView tiktokBtn;
    ImageView facebookBtn;
    ImageView twitterBtn;
    ImageView twitchBtn;
    androidx.viewpager.widget.PagerAdapter pagerAdapter;
    ConstraintLayout btnsLayout;
     DrawerLayout drawerLayout;
     ActionBarDrawerToggle actionBarDrawerToggle;
     NavigationView sideNavigation;

     BillingClient billingClient;

     List<ProductDetails> productDetailsList;
     Handler handler;
     public Prefs prefs;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instagramBtn = findViewById(R.id.instagramBtn);
        youtubeBtn = findViewById(R.id.youtubeBtn);
        tiktokBtn = findViewById(R.id.tiktokBtn);
        facebookBtn= findViewById(R.id.facebookBtn);
        twitterBtn = findViewById(R.id.twitterBtn);
        twitchBtn = findViewById(R.id.twitchBtn);

        productDetailsList = new ArrayList<>();
        handler = new Handler();
        prefs = new Prefs(MainActivity.this);


        ViewPager viewPager = findViewById(R.id.downloadView);
        btnsLayout = findViewById(R.id.btnsLayout);

        //********* Side navigation**************
        drawerLayout = findViewById(R.id.my_drawer_layout);
       actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
       sideNavigation = findViewById(R.id.sideNavigation);
       sideNavigation.setNavigationItemSelectedListener(this);

       drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3700B3")));
          //***************************************

        //**********Check arabic language***************
        if (Locale.getDefault().getLanguage().equals("ar")){
            viewPager.setRotationY(180);
        }
        //*******************************************

        //*********In-app-subscribtion***************
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(
                        (billingResult, list) -> {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                                for (Purchase purchase : list) {
                                    handlePurchase(purchase);
                                }
                            }
                        }
                ).build();

        //start the connection after initializing the billing client
        establishConnection();
        if (prefs.getPremium() == 0) {
            if (!prefs.isRemoveAd()) {
                MobileAds.initialize(this, initializationStatus -> {
                });
                Log.d("RemoveAds", "Remove ads off");
            } else {
                Log.d("RemoveAds", "Remove ads On");

            }
        }
        if(prefs.isRemoveAd()){
            InstagramFragment insta = new InstagramFragment();
            TiktokFragment tiktok = new TiktokFragment();
            YoutubeFragment youtube = new YoutubeFragment();
            TwitterFragment twitter = new TwitterFragment();
            TwitchFragment twitch = new TwitchFragment();
            tiktok.setremoveads(true);
            youtube.setremoveads(true);
           twitter.setremoveads(true);
            twitch.setremoveads(true);
            insta.setremoveads(true);
        }

        //*******************************************

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),6);
        viewPager.setAdapter(pagerAdapter);

        instagramBtn.setOnClickListener(v -> {
            pagerAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(0);
        });
        youtubeBtn.setOnClickListener(v -> {
            pagerAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(1);
        });
        tiktokBtn.setOnClickListener(v -> {
            pagerAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(2);
        });
        facebookBtn.setOnClickListener(v -> {
            pagerAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(3);
        });
        twitterBtn.setOnClickListener(v -> {
            pagerAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(4);
        });
        twitchBtn.setOnClickListener(v -> {
            pagerAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(5);
        });
        int purple_200=getResources().getColor(R.color.purple_200);
      viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
          @Override
          public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
              instagramBtn.setBackgroundColor( purple_200);
              youtubeBtn.setBackgroundColor( purple_200);
              tiktokBtn.setBackgroundColor( purple_200);
              facebookBtn.setBackgroundColor(purple_200);
              twitterBtn.setBackgroundColor(purple_200);
              twitchBtn.setBackgroundColor(purple_200);
              if(position == 0){
                  instagramBtn.setBackgroundResource(R.drawable.background_border2);
              } else if (position == 1) {
                  youtubeBtn.setBackgroundResource(R.drawable.background_border2);
              }
              else if (position == 2) {
                  tiktokBtn.setBackgroundResource(R.drawable.background_border2);
              }
              else if (position == 3) {
                  facebookBtn.setBackgroundResource(R.drawable.background_border2);
              }
              else if (position == 4) {
                 twitterBtn.setBackgroundResource(R.drawable.background_border2);
              }
              else if (position == 5) {
                  twitchBtn.setBackgroundResource(R.drawable.background_border2);
              }
          }
          @Override
          public void onPageSelected(int position) {
          }
          @Override
          public void onPageScrollStateChanged(int state) {
          }
      });

      //*********** getting link
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
           return true;
            }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_home:
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                break;
            case R.id.item_history:
                Intent ii = new Intent(this, HistoryActivity.class);
                startActivity(ii);
                break;
            case R.id.item_ads:
                if(prefs.isRemoveAd()){
                    Toast.makeText(this, R.string.Subscribed, Toast.LENGTH_SHORT).show();
                }else{
                    restorePurchases();
                }
                break;
            case R.id.item_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.aziz.mediadownloader");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.item_help:
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"cycwebsite1@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "I need help with..");
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
                break;
            case R.id.item_policy:
                Uri uriUrl = Uri.parse("https://www.termsfeed.com/live/b1815090-8e91-4d6a-a336-42e6bfd47ad3");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW,uriUrl);
                startActivity(launchBrowser);
                break;
        }
        return true;
    }
    //*********In-app-subscribtion****************
    void establishConnection() {

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    showProducts();
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                establishConnection();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    void showProducts() {
        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(
                //Product 1
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("ads_remove")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
        );
        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        billingClient.queryProductDetailsAsync(
                params,
                (billingResult, prodDetailsList) -> {
                    // Process the result
                    productDetailsList.clear();
                    handler.postDelayed(() -> {
                    //    loadProducts.setVisibility(View.INVISIBLE);
                        productDetailsList.addAll(prodDetailsList);
                    }, 2000);

                }
        );

    }
    void launchPurchaseFlow(ProductDetails productDetails) {
        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .build()
                );
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();

        billingClient.launchBillingFlow(this, billingFlowParams);
    }

    void handlePurchase(Purchase purchases) {
        if (!purchases.isAcknowledged()) {
            billingClient.acknowledgePurchase(AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchases.getPurchaseToken())
                    .build(), billingResult -> {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //Setting setIsRemoveAd to true
                    // true - No ads
                    // false - showing ads.
                    prefs.setIsRemoveAd(true);
                    reloadScreen();
                }
            });
        }else{
        }
    }
    void restorePurchases() {
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener((billingResult, list) -> {
        }).build();
        final BillingClient finalBillingClient = billingClient;
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                establishConnection();
            }
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    finalBillingClient.queryPurchasesAsync(
                            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(), (billingResult1, list) -> {
                                if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                    if (list.size() > 0) {
                                        prefs.setIsRemoveAd(true); // set true to activate remove ad feature
                                        reloadScreen();
                                        Toast.makeText(MainActivity.this, R.string.RemovedAds, Toast.LENGTH_SHORT).show();
                                    } else {
                                        prefs.setIsRemoveAd(false); // set false to de-activate remove ad feature
                                        if(productDetailsList.size() != 0){
                                            launchPurchaseFlow(productDetailsList.get(0));
                                        }else{
                                            Toast.makeText(MainActivity.this, R.string.ErrorOccurred, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }


    private void reloadScreen() {
        //Reload the screen to activate the removeAd and remove the actual Ad off the screen.
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    protected void onResume() {
        super.onResume();
        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(),
                (billingResult, list) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : list) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                                handlePurchase(purchase);
                            }
                        }
                    }
                }
        );
    }
    //********************************************

    //*********** recive link
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
           if(sharedText.contains("youtu")){
               pagerAdapter.notifyDataSetChanged();
               youtubeBtn.performClick();
           } else if (sharedText.contains("tiktok")){
                pagerAdapter.notifyDataSetChanged();
                tiktokBtn.performClick();
           }else if (sharedText.contains("fb")){
               pagerAdapter.notifyDataSetChanged();
               facebookBtn.performClick();
           }
           else if (sharedText.contains("twitter")){
               pagerAdapter.notifyDataSetChanged();
               twitterBtn.performClick();
           }
           else if (sharedText.contains("twitch")){
               pagerAdapter.notifyDataSetChanged();
               twitchBtn.performClick();
           }
        }
    }
}
