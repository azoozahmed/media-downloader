package com.example.mydownloaderapplication.Mainactivity.Fragments.FacebookFragment;

import static android.app.Activity.RESULT_OK;


import static com.example.mydownloaderapplication.Mainactivity.Fragments.FacebookFragment.Utils.RootDirectoryFacebook;
import static com.example.mydownloaderapplication.Mainactivity.Fragments.FacebookFragment.Utils.createFileFolder;
import static com.example.mydownloaderapplication.Mainactivity.Fragments.FacebookFragment.Utils.startDownload;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.mydownloaderapplication.R;
import com.example.mydownloaderapplication.databinding.ActivityMainBinding;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

public class FacebookFragment extends  Fragment {
    Button getDownload;
    Button downloadBtn;
    ImageView facebookIcon;
    EditText link;
    String URL = null;
    String videoUrl = "1";
    Uri uri2;
    Context thiscontext;
    ProgressBar progressBar;

    Dialog prograssD;
    File dir;
    TextView cancelLink;
    ActivityMainBinding binding;
    private ClipboardManager clipBoard;
    private String strName = "facebook";
    private String strNameSecond = "fb";

    ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                Toast.makeText(getContext(), "Permission Granted!", Toast.LENGTH_SHORT).show();
            }
        }
    });


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.facebook_fragment, null);
        thiscontext = container.getContext();

        getDownload = v.findViewById(R.id.getDownloadBtnFacebook);
        link = v.findViewById(R.id.linkTextFacebook);
        downloadBtn = v.findViewById(R.id.downloadBtnFacebook);
        downloadBtn.setVisibility(View.GONE);
       facebookIcon = v.findViewById(R.id.facebookIcon1);
        cancelLink = v.findViewById(R.id.cancelLink);

        progressBar = v.findViewById(R.id.prgbarFacebook);
        prograssD = new ProgressDialog(thiscontext);

        createFileFolder();
        //**********Check arabic language***************
        if (Locale.getDefault().getLanguage().equals("ar")){
           RelativeLayout facebookLayout = v.findViewById(R.id.facebookLayout);
            facebookLayout.setRotationY(180);
        }

        //*******************************************
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
        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link.setText(null);
                cancelLink.setVisibility(View.GONE);
            }
        });
        getDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL = link.getText().toString();
                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
                if (connected) {
                    if (ActivityCompat.checkSelfPermission(thiscontext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    } else if (URL.equals("")) {
                        Toast.makeText(thiscontext, R.string.EnterLink, Toast.LENGTH_SHORT).show();
                    } else if (!Patterns.WEB_URL.matcher(URL).matches()) {
                        Toast.makeText(thiscontext, R.string.WrongLink, Toast.LENGTH_SHORT).show();
                    } else if (!URL.contains("fb") && !URL.contains("facebook")) {
                        Toast.makeText(thiscontext, R.string.WrongLink, Toast.LENGTH_SHORT).show();
                    } else {
                   //     downloadBtn.setVisibility(View.VISIBLE);
                     //   facebookIcon.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(thiscontext, R.string.CheckConnection, Toast.LENGTH_SHORT).show();
                }
            }

        });
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        getFacebookData();
            }
        });

        return v;
    }

    private void getFacebookData() {
        try {
            createFileFolder();
            java.net.URL url = new URL(URL);
            String host = url.getHost();
            if (host.contains(strName) || host.contains(strNameSecond)) {
         //       Utils.showProgressDialog(activity);
                new CallGetFacebookData().execute(URL);

//                showInterstitialAds();
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class CallGetFacebookData extends AsyncTask<String, Void, Document> {
        Document facebookDoc;
        @Override
        protected Document doInBackground(String... urls) {
            try {
                facebookDoc = (Document) Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
              System.out.println("FFFFFFFFFF background error");
            }
            return facebookDoc;
        }
        @Override
        protected void onPostExecute(Document result) {
            try {
                videoUrl = result.select("meta[property=\"og:video\"]").last().attr("content");
                String ff = result.toString();
                System.out.println(ff);
                if (!videoUrl.equals("")) {
                    startDownload(videoUrl, RootDirectoryFacebook, getActivity(), "facebook_" + System.currentTimeMillis() + ".mp4");
                    videoUrl = "";
//                    showInterstitialAds();
                 //   binding.etText.setText("");
                }
            } catch (NullPointerException e) {
                System.out.println("dddddddddddddddddddd"+e);
                Toast.makeText(thiscontext, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}