package com.example.dictionary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    public static Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userid, userbirth;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            userid = (TextView) view.findViewById(R.id.user_id);
            userbirth = (TextView) view.findViewById(R.id.user_birth);
            imageView = view.findViewById(R.id.profile);
        }

        public void setItem(Item item) {
            userid.setText(item.getId());
            userbirth.setText(item.getBirth());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recyclerview_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Item item = items.get(position);
        viewHolder.setItem(item);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Create a reference to "mountains.jpg"
        StorageReference profileRef = storageRef.child(item.uid+"/profile.jpg");


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

                viewHolder.imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                viewHolder.imageView.setImageResource(R.drawable.baseline_perm_identity_24);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public static class Item {
        String id, birth, profile, uid;

        public Item(String id, String birth, String uid) {
            this.id = id;
            this.birth = birth;
            this.uid = uid;
        }

        public String getId() { return id; }
        public String getBirth() { return birth; }
    }
}
