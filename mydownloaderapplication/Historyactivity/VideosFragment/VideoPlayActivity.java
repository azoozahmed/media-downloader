package com.example.mydownloaderapplication.Historyactivity.VideosFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mydownloaderapplication.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;

public class VideoPlayActivity extends AppCompatActivity {

    String path;
    VideoView videoView;
    ImageButton share;
    ImageButton delete;
    SimpleExoPlayer simpleExoPlayer;
    ImageView lockscreenBtn;
    ImageView fullscreenBtn;
    Uri videoSource;
    boolean isfullscreen = false;
    boolean islockedscreen = false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        videoView = findViewById(R.id.videoView);

        share = findViewById(R.id.shareVideo);
        delete = findViewById(R.id.deleteVideo);

        PlayerView playerView = findViewById(R.id.player);
        ProgressBar progressBar= findViewById(R.id.progress_bar);
         fullscreenBtn = findViewById(R.id.fullscreenBtn);
         lockscreenBtn = findViewById(R.id.exo_lock);


         simpleExoPlayer = new SimpleExoPlayer.Builder(this).setSeekBackIncrementMs(5000).setSeekForwardIncrementMs(5000).build();
        playerView.setPlayer(simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        simpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if(playbackState == Player.STATE_BUFFERING){
                    progressBar.setVisibility(View.VISIBLE);
                } else if (playbackState == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                }
            }


            @Override
            public void onPlayerErrorChanged(@Nullable PlaybackException error) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(videoSource, "video/*");
                startActivity(intent);
            }

        });

        Intent intent = getIntent();
        if (intent != null){
            path=  intent.getStringExtra("video");
            videoView.setVideoPath(path);
            videoSource = Uri.parse(path);
            MediaItem mediaItem = MediaItem.fromUri(videoSource);
            simpleExoPlayer.setMediaItem(mediaItem);
            simpleExoPlayer.play();
        }
        fullscreenBtn.setOnClickListener(v -> {
            if(!isfullscreen){
                fullscreenBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_fullscreen_exit_24));
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }else{
                fullscreenBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_fullscreen));
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            isfullscreen = !isfullscreen;
        });
        lockscreenBtn.setOnClickListener(v -> {
            if(!islockedscreen){
                lockscreenBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_lock));
            }else{
                lockscreenBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_lock_open));

            }
            islockedscreen = !islockedscreen;
            lockScreen(islockedscreen);
        });
        share.setOnClickListener(v -> new ShareCompat.IntentBuilder(VideoPlayActivity.this).setType("video/*").setStream(Uri.parse(path)).setChooserTitle("Share video").startChooser());
        delete.setOnClickListener(v -> {
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(VideoPlayActivity.this);
            alertDialogBuilder.setMessage(R.string.DeleteConfirm);
            alertDialogBuilder.setPositiveButton(R.string.Ok, (dialog, which) -> {
                String[] projection = new String[] {MediaStore.Video.Media._ID};
                String selection = MediaStore.Video.Media.DATA +" = ?";
                String[] selectionArgs = new String[]{new File(path).getAbsolutePath()};
                Uri queryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(queryUri, projection,selection,selectionArgs,null);
                if(cursor.moveToFirst()){
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    Uri deletUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                  try {
                      contentResolver.delete(deletUri, null, null);
                      Toast.makeText(VideoPlayActivity.this, R.string.DeletedSuccessfully, Toast.LENGTH_SHORT).show();
                      finish();
                  }catch (Exception e){

                      Toast.makeText(VideoPlayActivity.this, R.string.ErrorOccurred, Toast.LENGTH_SHORT).show();
                  }
                }else{
                    File file = new File(path);
                    if(file.exists()){
                        file.delete();
                    }
                    VideoPlayActivity.this.finish();
                    Toast.makeText(VideoPlayActivity.this, R.string.DeletedSuccessfully, Toast.LENGTH_SHORT).show();
                    //     Toast.makeText(VideoPlayActivity.this, R.string.ErrorOccurred, Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            });
            alertDialogBuilder.setNegativeButton(R.string.Cancel, (dialog, which) -> dialog.dismiss());
            alertDialogBuilder.show();

        });
    }
    private void lockScreen(boolean islockedscreen) {
        RelativeLayout relativeLayout = findViewById(R.id.sec_controlvid1);
        LinearLayout linearLayout = findViewById(R.id.sec_controlvid0);
        LinearLayout linearLayout2 = findViewById(R.id.sec_controlvid2);
        if (islockedscreen){
            relativeLayout.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.INVISIBLE);
            linearLayout2.setVisibility(View.INVISIBLE);
        }else{
            relativeLayout.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if(islockedscreen)return;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            fullscreenBtn.performClick();
        }else super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        simpleExoPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        simpleExoPlayer.pause();
    }

}