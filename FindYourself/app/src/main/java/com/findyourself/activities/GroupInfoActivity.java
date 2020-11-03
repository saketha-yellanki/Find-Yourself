package com.findyourself.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findyourself.R;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupInfoActivity extends AppCompatActivity {
    RecyclerView userslist;
    Adapter adapter;
    List<String> usernames;
    MaterialTextView grp_info_welcome;

    String room_id, room_name;

    FirebaseDatabase db = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        userslist = findViewById(R.id.users_rv);
        grp_info_welcome = findViewById(R.id.group_info_welcome);
        room_id = getIntent().getStringExtra("room_id");
        room_name = getIntent().getStringExtra("room_name");
        grp_info_welcome.setText(room_name);

        usernames = new ArrayList<String>();

        DatabaseReference room_ref = db.getReference("rooms");
        room_ref.child(room_id).child("members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String mem_id = ds.getKey();
                    DatabaseReference user_ref = db.getReference("users");

                    user_ref.child(mem_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String mem_name = snapshot.child("username").getValue().toString();
                            usernames.add(mem_name);

                            adapter = new Adapter(GroupInfoActivity.this, usernames);

                            LinearLayoutManager layoutManager = new LinearLayoutManager(GroupInfoActivity.this, LinearLayoutManager.VERTICAL, false);
                            userslist.setLayoutManager(layoutManager);
                            userslist.setAdapter(adapter);
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


    }

    public class Adapter extends RecyclerView.Adapter<GroupInfoActivity.Adapter.ViewHolder> {
        List<String> usernames;
        LayoutInflater inflater;

        public Adapter(Context ctx, List<String> usernames) {
            this.usernames = usernames;
            this.inflater = LayoutInflater.from(ctx);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.custom_users_list_layout, parent, false);
            return new GroupInfoActivity.Adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GroupInfoActivity.Adapter.ViewHolder holder, int position) {
            holder.title.setText(usernames.get(position));

        }

        @Override
        public int getItemCount() {
            return usernames.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            MaterialTextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.chatroom_user_name_rv);


            }
        }

    }
}