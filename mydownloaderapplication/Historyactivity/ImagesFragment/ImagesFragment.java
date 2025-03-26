package com.example.mydownloaderapplication.Historyactivity.ImagesFragment;

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

import com.example.mydownloaderapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ImagesFragment extends Fragment {
    @Nullable  private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result){
            getImages();
        }
    });
    ArrayList<Image> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    TextView emptyImages;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.images_fragment,null);

        emptyImages = v.findViewById(R.id.emptyImages);
        recyclerView= v.findViewById(R.id.recyclerImages);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            assert activityResultLauncher != null;
            activityResultLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else  if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            assert activityResultLauncher != null;
            activityResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }else{
            getImages();
        }
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        } else  if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        }else{
            getImages();
        }
    }
    private void getImages() {
        arrayList.clear();
        String path = "/storage/emulated/0/"+getContext().getResources().getString(R.string.app_name);
        File file = new File(path);
        File[] files = file.listFiles();
        //*** to sort images by date
        Arrays.sort(files, (Comparator) (o1, o2) -> Long.compare(((File) o2).lastModified(), ((File) o1).lastModified()));
       //**************************
        for (File file1 : files) {
            if (file1.getPath().endsWith(".jpeg")) {
                arrayList.add(new Image(file1.getPath()));
            }
        }
        ImageAdapter adapter = new ImageAdapter(getContext(), arrayList);
        if (adapter.getItemCount() == 0){
            emptyImages.setVisibility(View.VISIBLE);
            emptyImages.setText(getContext().getResources().getString(R.string.PicturesUnavailable));
        }else{
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, path1) -> startActivity(new Intent(getContext(), ImageViewActivity.class).putExtra("image", path1)));
        }
    }
}
