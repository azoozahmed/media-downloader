package com.example.mydownloaderapplication.Historyactivity.VideosFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydownloaderapplication.Mainactivity.MainActivity;
import com.example.mydownloaderapplication.R;
import com.example.mydownloaderapplication.StartActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class VideosFragment extends Fragment {
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result){
            getVideos();
        }else{
            startActivity(new Intent(getContext(), MainActivity.class));
        }
    });
    ArrayList<Video> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    TextView emptyVideos;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.videos_fragment,null);

        emptyVideos = v.findViewById(R.id.emptyVideos);
        recyclerView= v.findViewById(R.id.recyclerVideos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else  if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }else{
            getVideos();
        }

        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        } else  if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        }else{
            getVideos();
        }
    }
    private void getVideos() {
        arrayList.clear();
        String path = "/storage/emulated/0/"+getContext().getResources().getString(R.string.app_name);
        File file = new File(path);
        File[] files = file.listFiles();
        if(files.length != 0) {
            Arrays.sort(files, (Comparator) (o1, o2) -> Long.compare(((File) o2).lastModified(), ((File) o1).lastModified()));
        }
        for (File file1 : files) {
            if (file1.getPath().endsWith(".mp4")) {
                arrayList.add(new Video(file1.getPath()));
            }
        }
        VideoAdapter adapter = new VideoAdapter(getContext(), arrayList);
        if (adapter.getItemCount() == 0){
            emptyVideos.setVisibility(View.VISIBLE);
        }else{
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, path1) -> startActivity(new Intent(getContext(), VideoPlayActivity.class).putExtra("video", path1)));
    }
    }
}
