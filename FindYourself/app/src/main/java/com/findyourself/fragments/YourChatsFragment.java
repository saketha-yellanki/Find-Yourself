package com.findyourself.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findyourself.R;
import com.findyourself.activities.ChatRoomActivity;
import com.findyourself.activities.ThisUser;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class YourChatsFragment extends Fragment {

    RecyclerView dataList;
    List<String> titles, room_ids;
    RecyclerView.Adapter adapter;
    MaterialTextView hi_user;

    String username;

    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase db = FirebaseDatabase.getInstance();


    public YourChatsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_your_chat_rooms, container, false);
        dataList = view.findViewById(R.id.your_chat_rv);


        hi_user = view.findViewById(R.id.welcome_note);
        hi_user.setText("Hi! " + ThisUser.instance.getUsername());

        titles = new ArrayList<>();
        room_ids = new ArrayList<>();

        final DatabaseReference room_ref = db.getReference("rooms");
        room_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot ds : snapshot.getChildren()) {
                    Log.i("ds", ds.getValue().toString());
                    DatabaseReference m_ref = ds.child("members").getRef();
                    m_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot mds : snapshot.getChildren()) {
                                Log.i("mds", mds.getKey().toString());
                                if (mds.getKey().equals(current_user.getUid())) {
                                    titles.add(ds.child("room_name").getValue().toString());
                                    room_ids.add(ds.getKey());
                                    Log.i("room name", ds.child("room_name").getValue().toString());
                                }
                            }
                            adapter = new YourChatsFragment.Adapter(getContext(), titles);

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            dataList.setLayoutManager(linearLayoutManager);
                            dataList.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        titles.add("First Item");
//        titles.add("Second Item");
//        titles.add("Third Item");
//        titles.add("Fourth Item");
//        titles.add("Fifth Item");
//        titles.add("Sixth Item");


        return view;

    }

    public void putArgs(Bundle b) {
        username = b.getString("username");
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
            if (position % 2 == 1) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

            }
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
                        startActivity(new Intent(getActivity(), ChatRoomActivity.class));
                        Toast.makeText(v.getContext(), "Clicked -> " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

}