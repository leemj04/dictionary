package com.example.dictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    public ArrayList<Item> items = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userid, userbirth;

        public ViewHolder(View view) {
            super(view);
            userid = (TextView) view.findViewById(R.id.user_id);
            userbirth = (TextView) view.findViewById(R.id.user_birth);
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

        public Item(String id, String birth) {
            this.id = id;
            this.birth = birth;
        }

        public String getId() { return id; }
        public String getBirth() { return birth; }
    }
}
