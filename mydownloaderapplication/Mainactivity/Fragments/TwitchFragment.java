package com.example.mydownloaderapplication.Mainactivity.Fragments;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.mydownloaderapplication.Mainactivity.MainActivity;
import com.example.mydownloaderapplication.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.yausername.youtubedl_android.DownloadProgressCallback;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLRequest;

import java.io.File;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TwitchFragment extends Fragment {
    Button getDownload;
    Button downloadBtn;
    ImageView twitchIcon1;
    EditText link;
    String URL = null;
    Context thiscontext;
    RelativeLayout prograss;
    TextView progressText;
    boolean isDownloading = false;
    boolean isLoadingLate = false;
    private static boolean removeads;

    private InterstitialAd mInterstitialAd;
    TextView cancelLink;
    private static final int REQUEST = 112;
    ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
        }
    });
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final DownloadProgressCallback callback = (v, l, s) -> {
    };
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.twitch_fragment,null);
        getDownload =v.findViewById(R.id.getDownloadBtnTwitch);
        link = v.findViewById(R.id.linkTextTwitch);
        downloadBtn = v.findViewById(R.id.downloadBtnTwitch);
        downloadBtn.setVisibility(View.GONE);
        twitchIcon1=v.findViewById(R.id.twitchIcon1);
        prograss=v.findViewById(R.id.progress_circular_twitch);
        cancelLink = v.findViewById(R.id.cancelLink);
        progressText = v.findViewById(R.id.progressText);

        //**********Check arabic language***************
        if (Locale.getDefault().getLanguage().equals("ar")){
            RelativeLayout youtubeLayout = v.findViewById(R.id.twitchLayout);
            youtubeLayout.setRotationY(180);
        }

        //*******************************************
        //******** ad *********************
        MobileAds.initialize(getContext(), initializationStatus -> {});
        AdRequest adRequest = new AdRequest.Builder().build();
//ca-app-pub-5745492974183360/1006105424
        InterstitialAd.load(getContext(),"ca-app-pub-5745492974183360/1006105424", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
     /*
       mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.");
            }
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.");
                mInterstitialAd = null;
            }
            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                mInterstitialAd = null;
            }
            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.");
            }
            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.");
            }
        });
      */
        //*******************************
        //*******show progress bar if downloading*******
        if(isDownloading){
            if (isLoadingLate){
                progressText.setText(getContext().getResources().getString(R.string.Downloading) +""+getContext().getResources().getString(R.string.MoreTime));
            }
            downloadBtn.setVisibility(View.VISIBLE);
            twitchIcon1.setVisibility(View.GONE);
            prograss.setVisibility(View.VISIBLE);
        }
        //**********************************************
        link.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals("")){
                    cancelLink.setVisibility(View.GONE);
                }else{
                    cancelLink.setVisibility(View.VISIBLE);
                }
            }
        });
        cancelLink.setOnClickListener(v13 -> {
            link.setText(null);
            cancelLink.setVisibility(View.GONE);
        });
        //*********** get shared link
        Intent intent = getActivity().getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }
        getDownload.setOnClickListener(v1 -> {
            //*****check connection ************
            ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
            if(!connected) {
                Toast.makeText(getContext(), R.string.CheckConnection, Toast.LENGTH_SHORT).show();
            }else {
                //****check permission
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if (!hasPermissions(getContext(), PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) getContext(), PERMISSIONS, REQUEST );
                    } else {
                    URL = link.getText().toString().trim();
                    if (URL.equals("")) {
                        Toast.makeText(thiscontext, R.string.EnterLink, Toast.LENGTH_SHORT).show();
                    } else {
                        if (!URL.contains("twitch")) {
                            Toast.makeText(getContext(), R.string.WrongLink, Toast.LENGTH_SHORT).show();
                        } else if (prograss.getVisibility() == View.GONE) {
                            //***** play ad *************
                            if(removeads){
                            }else {
                                if (mInterstitialAd != null) {
                                    mInterstitialAd.show(getActivity());
                                } else {
                                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                                }
                            }
                            //***************************
                            new Handler().postDelayed(() -> {
                                downloadBtn.setVisibility(View.VISIBLE);
                                twitchIcon1.setVisibility(View.GONE);
                            },2000);downloadBtn.setVisibility(View.VISIBLE);
                        }


                    }
                }}
            }
        });
        File dir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(getContext().getResources().getString(R.string.app_name))));
        if(!dir.exists()){
            dir.mkdirs();
        }
        downloadBtn.setOnClickListener(v12 -> {
            if(prograss.getVisibility() == View.GONE) {
                prograss.setVisibility(View.VISIBLE);
               isDownloading = true;

                YoutubeDLRequest request = new YoutubeDLRequest(URL);
                request.addOption("--no-mtime");
                request.addOption("--downloader", "libaria2c.so");
                request.addOption("--external-downloader-args", "aria2c:\"--summary-interval=1\"");
                request.addOption("-f", "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best");
                request.addOption("-o", dir.getAbsolutePath() + "/%(title)s"+System.currentTimeMillis()/10000000+".mp4");
                Disposable disposable = Observable.fromCallable(() -> YoutubeDL.getInstance().execute(request, String.valueOf(callback)))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(youtubeDLResponse -> {
                            Toast.makeText(getContext(), R.string.DownloadedSuccessfully, Toast.LENGTH_SHORT).show();
                            prograss.setVisibility(View.GONE);
                            downloadBtn.setVisibility(View.GONE);
                            link.setText(null);
                            twitchIcon1.setVisibility(View.VISIBLE);
                            isDownloading = false;
                            isLoadingLate=false;
                        }, e -> {
                            Toast.makeText(getContext(), R.string.ErrorOccurred, Toast.LENGTH_SHORT).show();
                            prograss.setVisibility(View.GONE);
                            downloadBtn.setVisibility(View.GONE);
                            link.setText(null);
                            twitchIcon1.setVisibility(View.VISIBLE);
                            isDownloading = false;
                            isLoadingLate = false;
                        });
                compositeDisposable.add(disposable);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                    if(isDownloading){
                        isLoadingLate = true;
                        progressText.setText(getContext().getResources().getString(R.string.Downloading) +""+getContext().getResources().getString(R.string.MoreTime));
                    }
                }, 60000);
            }
        });
        return v;
    }
    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        if(isDownloading){
            Toast.makeText(getContext(), R.string.DownloadindCanceled, Toast.LENGTH_SHORT).show();
        }
        super.onDestroy();
    }
    public void setremoveads(boolean remove){
        removeads = remove;
    }

    //************** permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                } else {
                    Toast.makeText(getContext(), "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            if (sharedText.contains("twitch")) {
                link.setText(sharedText);
                //***************************
                new Handler().postDelayed(() -> {
                    getDownload.performClick();
                },1000);
            }
        }
    }
}
