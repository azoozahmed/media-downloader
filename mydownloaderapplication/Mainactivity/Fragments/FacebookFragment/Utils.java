package com.example.mydownloaderapplication.Mainactivity.Fragments.FacebookFragment;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

import com.example.mydownloaderapplication.R;

import java.io.File;

public class Utils {
    private static Context context;


    public static String RootDirectoryFacebook = "/PiBrowser/Facebook/";

    public static File RootDirectoryFacebookShow = new File(Environment.getExternalStorageDirectory() + "/Download/PiBrowser/Facebook");

    public Utils(Context _mContext) {
        context = _mContext;
    }
    public static void setToast(Context _mContext, String str) {
        Toast toast = Toast.makeText(_mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void createFileFolder() {
        if (!RootDirectoryFacebookShow.exists()) {
            RootDirectoryFacebookShow.mkdirs();
        }

    }



    public static void startDownload(String downloadPath, String destinationPath, Context context, String FileName) {
        setToast(context, context.getResources().getString(R.string.Downloading));
        Uri uri = Uri.parse(downloadPath); // Path where you want to download file.
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle(FileName+""); // Title for notification.
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,destinationPath+FileName);  // Storage directory path
        ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading

        try {
            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(context, new String[]{new File(Environment.DIRECTORY_DOWNLOADS + "/" + destinationPath + FileName).getAbsolutePath()},
                        null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            } else {
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(Environment.DIRECTORY_DOWNLOADS + "/" + destinationPath + FileName))));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
