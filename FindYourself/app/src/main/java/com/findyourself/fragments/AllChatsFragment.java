package com.findyourself.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findyourself.R;

import java.util.ArrayList;
import java.util.List;

public class AllChatsFragment extends Fragment {


    RecyclerView dataList;
    List<String> titles;
    List<Integer> images;
    Adapter adapter;

    public AllChatsFragment() {
    }


//    public static AllChatsFragment newInstance(String param1, String param2) {
//        AllChatsFragment fragment = new AllChatsFragment();
//
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_chat_rooms, container, false);
        dataList = view.findViewById(R.id.all_chat_rv);
        titles = new ArrayList<>();
        images = new ArrayList<>();

        titles.add("First Item");
        titles.add("Second Item");
        titles.add("Third Item");
        titles.add("Fourth Item");
        titles.add("Fifth Item");
        titles.add("Sixth Item");


        images.add(R.drawable.title1);
        images.add(R.drawable.title2);
        images.add(R.drawable.title3);
        images.add(R.drawable.title4);
        images.add(R.drawable.title5);
        images.add(R.drawable.title6);


        adapter = new Adapter(getContext(), titles, images);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);


        return view;
    }


    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        List<String> titles;
        List<Integer> images;
        LayoutInflater inflater;

        public Adapter(Context ctx, List<String> titles, List<Integer> images) {
            this.titles = titles;
            this.images = images;
            this.inflater = LayoutInflater.from(ctx);
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.custom_grid_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.title.setText(titles.get(position));
            holder.gridIcon.setImageResource(images.get(position));
        }

        @Override
        public int getItemCount() {
            return titles.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            ImageView gridIcon;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.textView2);
                gridIcon = itemView.findViewById(R.id.imageView2);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "Clicked -> " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}