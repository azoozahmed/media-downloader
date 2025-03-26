package com.example.mydownloaderapplication.Historyactivity.ImagesFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mydownloaderapplication.R;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    Context context;
    ArrayList<Image> arrayList;
    ImageAdapter.OnItemClickListener onItemClickListener;

    public ImageAdapter(Context context, ArrayList<Image> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(arrayList.get(position).getPath()).into(holder.imageView);
        holder.title.setText(new File(arrayList.get(position).getPath()).getName());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onClick(v, arrayList.get(position).getPath()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_item_image);
            title = itemView.findViewById(R.id.list_item_title);
        }
    }
    public void setOnItemClickListener(ImageAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(View view, String path);
    }

}
