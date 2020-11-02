package com.findyourself.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllChatsFragment extends Fragment {


    RecyclerView dataList;
    List<String> titles;
    //List<Integer> images;
    List<String> room_ids;
    Adapter adapter;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference room_ref = db.getReference("rooms");
    DatabaseReference user_ref = db.getReference("users");

    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

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
        //images = new ArrayList<>();
        room_ids = new ArrayList<>();

        final String uid = current_user.getUid();

        room_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String rid = ds.getKey();
                    room_ids.add(rid);
                    titles.add(ds.child("room_name").getValue().toString());
                }

                //images.add(R.drawable.gradient_1);
                adapter = new Adapter(getContext(), titles);


                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                dataList.setLayoutManager(gridLayoutManager);
                dataList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }


    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        List<String> titles;
        //List<Integer> images;
        LayoutInflater inflater;

        public Adapter(Context ctx, List<String> titles) {
            this.titles = titles;
            //this.images = images;
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
            //holder.gridIcon.setImageResource(images.get(position));
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
                //gridIcon = itemView.findViewById(R.id.imageView2);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String room_id = room_ids.get(getAdapterPosition());
                        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(view.getContext());
                        dialog.setTitle("Do you want to join this room?");
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                room_ref.child(room_id).child("members").child(current_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Toast.makeText(getContext(), "Already a member", Toast.LENGTH_SHORT).show();
                                        } else {
                                            room_ref.child(room_id).child("members").child(current_user.getUid()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        room_ref.child(room_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                int mc = Integer.parseInt(snapshot.child("members_count").getValue().toString());
                                                                room_ref.child(room_id).child("members_count").setValue(mc + 1);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                            }
                                                        });

                                                        Toast.makeText(getContext(), "Successfully Joined the Room", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getContext(), "Error Joining the Room", Toast.LENGTH_SHORT).show();
                                                        Log.i("Joining room failed", task.getException().toString());
                                                    }
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dialog.create().show();
                        //Toast.makeText(view.getContext(), "Clicked -> " + getAdapterPosition(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }
}