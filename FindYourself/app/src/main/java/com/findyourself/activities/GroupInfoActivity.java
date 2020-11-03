package com.findyourself.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findyourself.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GroupInfoActivity extends AppCompatActivity {
    RecyclerView userslist;
    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        userslist = findViewById(R.id.users_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        userslist.setLayoutManager(layoutManager);
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
            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.chatroom_user_name_rv);


            }
        }

    }
}