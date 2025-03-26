package com.example.mydownloaderapplication.Mainactivity.Fragments.InstagramFragment;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mydownloaderapplication.Mainactivity.MainActivity;
import com.example.mydownloaderapplication.Mainactivity.Prefs;
import com.example.mydownloaderapplication.R;
import com.example.mydownloaderapplication.StartActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yausername.youtubedl_android.DownloadProgressCallback;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLRequest;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InstagramFragment extends Fragment{
    ImageView instagramIcon;
    EditText link;
    Button getDownloadBtn;
    Button downloadBtn;
    String URL = null;
    String videoUrl="1";
    String photoUrl="1";
    private Uri uri2;
    Context thiscontext;
    RelativeLayout progressCircular;
    private InterstitialAd mInterstitialAd;

   TextView cancelLink;
   RelativeLayout prograss;
   boolean isDownloading = false;
   TextView progressText;
   private static boolean removeads;


    private static final int REQUEST = 112;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final DownloadProgressCallback callback = (v, l, s) -> {
    };
    ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
        }
    });

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.instagram_fragment,null);
        thiscontext = container.getContext();

       instagramIcon=v.findViewById(R.id.instagramIcon1);

       link=v.findViewById(R.id.linkTextInsta);
       getDownloadBtn =v.findViewById(R.id.getDownloadBtnInsta);
       downloadBtn =v.findViewById(R.id.downloadBtnInsta);
       progressCircular=v.findViewById(R.id.progress_circular_insta);
       cancelLink = v.findViewById(R.id.cancelLink);
       prograss = v.findViewById(R.id.progress_circular_insta2);
       progressText = v.findViewById(R.id.progressText);




       //**********Check arabic language***************
        if (Locale.getDefault().getLanguage().equals("ar")){
            RelativeLayout instagramLayout = v.findViewById(R.id.instagramLayout);
            instagramLayout.setRotationY(180);
        }
        //*******************************************
        //******** ad *********************
        MobileAds.initialize(getContext(), initializationStatus -> {});
        AdRequest adRequest = new AdRequest.Builder().build();
//ca-app-pub-5745492974183360/1006105424
        //ca-app-pub-3940256099942544/1033173712
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
            downloadBtn.setVisibility(View.VISIBLE);
            instagramIcon.setVisibility(View.GONE);
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
       getDownloadBtn.setOnClickListener(v1 -> {
           //*****check connection ************
           ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

           boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                   connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
           if (connected) {
               if (Build.VERSION.SDK_INT >= 23) {
                   String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                   if (!hasPermissions(getContext(), PERMISSIONS)) {
                       ActivityCompat.requestPermissions((Activity) getContext(), PERMISSIONS, REQUEST );
                   }
               else {
           URL = link.getText().toString().trim();
           if(URL.equals("")){
               Toast.makeText(getContext(), R.string.EnterLink, Toast.LENGTH_SHORT).show();
           }else {
               //  File file = new File(Environment.getExternalStorageDirectory(),"sociall");
               //   if(!file.exists()){
               //       file.mkdirs();
               //   }

                       //********* For videos *************
                       if (URL.contains("/reel/")) {
                           //     String result = StringUtils.substringBefore(URL, "/?");
                           //  URL = result + "/?__a=1&__d=dis";
                           //    progressCircular.setVisibility(View.VISIBLE);
                           //  processVideoData();
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
                               instagramIcon.setVisibility(View.GONE);
                           },2000);


                           //********* For images *************
                       } else if (URL.contains("/p/")) {
                           String result = StringUtils.substringBefore(URL, "/?");
                           URL = result + "/?__a=1&__d=dis";
                           progressCircular.setVisibility(View.VISIBLE);
                           processImageData();
                       } else {
                           Toast.makeText(getContext(), R.string.WrongLink, Toast.LENGTH_SHORT).show();
                       }
                   }

               }
               } else {
                   //do here
               }
           }else {
               Toast.makeText(thiscontext, R.string.CheckConnection, Toast.LENGTH_SHORT).show();
           }

           });
        File dir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(getContext().getResources().getString(R.string.app_name))));
        if(!dir.exists()){
            dir.mkdirs();
        }
       downloadBtn.setOnClickListener(v12 -> {
           //check permission
           if (ActivityCompat.checkSelfPermission(thiscontext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
               activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
           }else  {
               //**************for videos**************
               if (videoUrl.equals("1")) {
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
                   if (prograss.getVisibility() == View.GONE) {
                       prograss.setVisibility(View.VISIBLE);
                       isDownloading = true;
                       YoutubeDLRequest request = new YoutubeDLRequest(URL);
                       request.addOption("--no-mtime");
                       request.addOption("--downloader", "libaria2c.so");
                       request.addOption("--external-downloader-args", "aria2c:\"--summary-interval=1\"");
                       request.addOption("-f", "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best");
                       request.addOption("-o", dir.getAbsolutePath() + "/%(title)s"+System.currentTimeMillis()/100000+".mp4");
                       Disposable disposable = Observable.fromCallable(() -> YoutubeDL.getInstance().execute(request, String.valueOf(callback)))
                               .subscribeOn(Schedulers.newThread())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(youtubeDLResponse -> {
                                   Toast.makeText(getContext(), R.string.DownloadedSuccessfully, Toast.LENGTH_SHORT).show();
                                   prograss.setVisibility(View.GONE);
                                   downloadBtn.setVisibility(View.GONE);
                                   link.setText(null);
                                   instagramIcon.setVisibility(View.VISIBLE);
                                   isDownloading = false;
                               }, e -> {
                                   Toast.makeText(getContext(), R.string.ErrorOccurred, Toast.LENGTH_SHORT).show();
                                   prograss.setVisibility(View.GONE);
                                   downloadBtn.setVisibility(View.GONE);
                                   link.setText(null);
                                   instagramIcon.setVisibility(View.VISIBLE);
                                   isDownloading = false;
                               });
                       compositeDisposable.add(disposable);
                       final Handler handler = new Handler(Looper.getMainLooper());
                       handler.postDelayed(() -> {
                           if(isDownloading){
                                   progressText.setText(getContext().getResources().getString(R.string.Downloading) +""+getContext().getResources().getString(R.string.MoreTime));

                               progressText.setText(getContext().getResources().getString(R.string.Downloading) +""+getContext().getResources().getString(R.string.MoreTime));
                           }
                       }, 60000);
                   }
                   /*
                   DownloadManager.Request request = new DownloadManager.Request(uri2);
                   request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                   request.setTitle("Download");
                   request.setDescription("------");
                   request.allowScanningByMediaScanner();
                   request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

                   request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Instagram_" + System.currentTimeMillis() + ".mp4");
                   DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                   manager.enqueue(request);
                   Toast.makeText(getContext(), R.string.Downloading, Toast.LENGTH_SHORT).show();
                   link.setText(null);
                   instagramIcon.setVisibility(View.VISIBLE);
                   downloadBtn.setVisibility(View.GONE);
                   videoUrl = "1";
                   */
                   //***********for photos ***************
               } else if (!photoUrl.equals("1")) {
                   DownloadManager.Request request = new DownloadManager.Request(uri2);
                   request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                   request.setTitle("Download");
                   request.setDescription("------");
                   request.allowScanningByMediaScanner();
                   request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

                   String name ="Instagram_"+ System.currentTimeMillis() + ".jpeg";
                   request.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS,name);
                   DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                   manager.enqueue(request);
                   Toast.makeText(getContext(), R.string.Downloading, Toast.LENGTH_SHORT).show();
                   link.setText(null);
                   instagramIcon.setVisibility(View.VISIBLE);
                   downloadBtn.setVisibility(View.GONE);
                   photoUrl = "1";
                   //******** save in app folder *******************
                   File file = new File(String.valueOf(Environment.getExternalStoragePublicDirectory("TikTok downloads")),name);
                   if(!dir.exists()){
                       dir.mkdirs();
                   }
                   FileOutputStream stream = null;
                   try {
                       Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri2);
                       stream = new FileOutputStream(file);
                       bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                       stream.flush();
                       stream.flush();
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
                   try {
                       stream = new FileOutputStream(file);
                   } catch (FileNotFoundException e) {
                       throw new RuntimeException(e);
                   }
                   String uriStr = String.valueOf(uri2);
                   try {
                       stream.write(uriStr.getBytes());
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
                   try {
                       stream.close();
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
                   //**************************************

               }
           }
       });

       return v;
    }
    public void setremoveads(boolean remove){
        removeads = remove;
    }

    //***********Get video data  ****************
    /*
    private void processVideoData() {
        StringRequest request = new StringRequest(URL, response -> {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            MainURL mainURL = gson.fromJson(response, MainURL.class);
            videoUrl = mainURL.getGraphql().getShortcode_media().getVideo_url();
            uri2 = Uri.parse(videoUrl);

            instagramIcon.setVisibility(View.GONE);
            downloadBtn.setVisibility(View.VISIBLE);
            progressCircular.setVisibility(View.GONE);
        }, error -> {
            progressCircular.setVisibility(View.GONE);

            Toast.makeText(getContext(), R.string.VideoNotFound, Toast.LENGTH_SHORT).show();


        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
*/
    //***********Get photo data ****************
    private void processImageData() {
        StringRequest request = new StringRequest(URL, response -> {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
           MainURL mainURL = gson.fromJson(response, MainURL.class);
            photoUrl = mainURL.getGraphql().getShortcode_media().getDisplay_url();
            uri2 = Uri.parse(photoUrl);
            instagramIcon.setVisibility(View.GONE);
            downloadBtn.setVisibility(View.VISIBLE);
            progressCircular.setVisibility(View.GONE);
        }, error -> {
            progressCircular.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.ImageNotFound, Toast.LENGTH_SHORT).show();

        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);

    }
    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        if(isDownloading){
            Toast.makeText(getContext(), R.string.DownloadindCanceled, Toast.LENGTH_SHORT).show();
        }
        super.onDestroy();
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
    //***********get shared link
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            if (sharedText.contains("/reel/")) {
                link.setText(sharedText);
                //***************************
                new Handler().postDelayed(() -> {
                    getDownloadBtn.performClick();
                },1000);

            }
        }
    }
}
