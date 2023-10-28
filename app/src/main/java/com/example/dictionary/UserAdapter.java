package com.example.dictionary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    public ArrayList<Item> items = new ArrayList<>();
    public static View view;
    public static Context context;

    public interface OnItemClickListener {
        void onItemClicked(Item item);
        void onStarClicked(Item item, int position);
    }
    public static OnItemClickListener itemClickListener;
    public void setOnItemClickListener (OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userid, userbirth;
        public ImageView imageView, star;

        public ViewHolder(View view) {
            super(view);
            userid = view.findViewById(R.id.user_id);
            userbirth = view.findViewById(R.id.user_birth);
            imageView = view.findViewById(R.id.profile);
            star = view.findViewById(R.id.star);
        }

        public void setItem(Item item) {
            userid.setText(item.getId());
            userbirth.setText(item.getBirth());
            if (item.getStar()) {
                star.setImageResource(R.drawable.baseline_star_24);
            } else {
                star.setImageResource(R.drawable.baseline_star_border_24);
            }

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference profileRef = storageRef.child(item.getUid()+"/profile.jpg");

            File localFile = null;
            try {
                localFile = File.createTempFile("images", "jpg");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            profileRef.getBytes(1000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray( bytes, 0, bytes.length ) ;

                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    imageView.setImageResource(R.drawable.baseline_perm_identity_24);
                }
            });

            UserAdapter.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { itemClickListener.onItemClicked(item); }
            });

            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { itemClickListener.onStarClicked(item, getAdapterPosition()); }
            });
        }

        public TextView getUserId() { return userid; }
        public TextView getUserBirth() { return userbirth; }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recyclerview_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Item item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public static class Item {
        String id, birth, uid;
        Boolean star;

        public Item(String id, String birth, String uid, Boolean star) {
            this.id = id;
            this.birth = birth;
            this.uid = uid;
            this.star = star;
        }

        public String getId() { return id; }
        public String getBirth() { return birth; }
        public String getUid() { return uid; }
        public Boolean getStar() { return star; }
    }
}
