package com.findyourself.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.findyourself.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class YourChatsFragment extends Fragment {

    RecyclerView dataList;
    List<String> titles;
    RecyclerView.Adapter adapter;

    public YourChatsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_chat_rooms, container, false);
        dataList = view.findViewById(R.id.your_chat_rv);
        titles = new ArrayList<>();


        titles.add("First Item");
        titles.add("Second Item");
        titles.add("Third Item");
        titles.add("Fourth Item");
        titles.add("Fifth Item");
        titles.add("Sixth Item");


        adapter = new YourChatsFragment.Adapter(getContext(), titles);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        dataList.setLayoutManager(linearLayoutManager);
        dataList.setAdapter(adapter);


        return view;

    }

    public class Adapter extends RecyclerView.Adapter<YourChatsFragment.Adapter.ViewHolder> {
        List<String> titles;
        LayoutInflater inflater;

        public Adapter(Context ctx, List<String> titles) {
            this.titles = titles;
            this.inflater = LayoutInflater.from(ctx);
        }

        @NonNull
        @Override
        public YourChatsFragment.Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.custom_list_layout, parent, false);
            return new YourChatsFragment.Adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull YourChatsFragment.Adapter.ViewHolder holder, int position) {
            holder.title.setText(titles.get(position));
        }

        @Override
        public int getItemCount() {
            return titles.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.textview3);

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