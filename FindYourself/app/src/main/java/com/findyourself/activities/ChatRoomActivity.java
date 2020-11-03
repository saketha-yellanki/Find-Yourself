package com.findyourself.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findyourself.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener {


    final ThisUser user = ThisUser.getInstance();
    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    List<Message> messages;
    MessageAdapter messageAdapter;
    DatabaseReference messagedb;
    RecyclerView rvMessage;
    TextInputEditText etMessage;
    AppCompatImageButton imgButton;
    String room_id;

    ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Log.i("current method", "onCreate()");
        room_id = getIntent().getStringExtra("room_id");
        init();
    }

    private void init() {
        rvMessage = (RecyclerView) findViewById(R.id.chat_recycler);
        etMessage = findViewById(R.id.et_msg);
        imgButton = findViewById(R.id.btn_send);
        imgButton.setOnClickListener(this);
        messages = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {

        if (!TextUtils.isEmpty(etMessage.getText().toString())) {
            String msg_id = messagedb.child(room_id).push().getKey();
            Message message = new Message(etMessage.getText().toString(), user.getUsername(), msg_id);
            etMessage.setText("");
            messagedb.child(room_id).child(msg_id).setValue(message);

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        messagedb.child(room_id).removeEventListener(childEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("current method", "onStart()");
        messages.clear();
        messagedb = db.getReference("messages");
        childEventListener = messagedb.child(room_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("current method", "onChildAdded()");
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                messages.add(message);
                for (int i = 0; i < messages.size(); i++) {
                    Log.i("all messages above", messages.get(i).toString());
                }
                displayMessages(messages);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("current method", "onChildChanged()");
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                List<Message> newMessages = new ArrayList<Message>();

                for (Message m : messages) {
                    if (m.getKey().equals(message.getKey())) {
                        newMessages.add(message);
                    } else {
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                for (int i = 0; i < messages.size(); i++) {
                    Log.i("all messages", messages.get(i).toString());
                }

                displayMessages(messages);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.i("current method", "onChildRemoved()");
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                List<Message> newmessages = new ArrayList<Message>();

                for (Message m : messages) {
                    if (!m.getKey().equals(message.getKey())) {
                        newmessages.add(m);
                    }
                }
                messages = newmessages;
                displayMessages(messages);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("current method", "onResume()");
        messages = new ArrayList<>();
        messages.clear();
    }

    private void displayMessages(List<Message> messages) {
        Log.i("current method", "DisplayMessages()");
        rvMessage.setLayoutManager(new LinearLayoutManager(ChatRoomActivity.this));
        messageAdapter = new MessageAdapter(ChatRoomActivity.this, messages, messagedb);
        rvMessage.setAdapter(messageAdapter);
    }

    private class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {

        Context context;
        List<Message> messages;
        DatabaseReference messagedb;

        public MessageAdapter(Context context, List<Message> messages, DatabaseReference messagedb) {
            this.context = context;
            this.messagedb = messagedb;
            this.messages = messages;
        }

        @NonNull
        @Override
        public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
            return new MessageAdapterViewHolder(view);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {

            Message message = messages.get(position);
            if (message.getName().equals(user.getUsername())) {
                holder.tvTitle.setText("You: " + message.getMessage());
                holder.tvTitle.setGravity(Gravity.START);
                holder.l1.setBackgroundColor(Color.parseColor("#1ca7ec"));
                holder.msg_parent_layout.setGravity(Gravity.END);


            } else {
                holder.tvTitle.setText(message.getName() + ": " + message.getMessage());
                holder.ibDelete.setVisibility(View.GONE);
                holder.l1.setBackgroundColor(Color.parseColor("#1f2f98"));
                holder.msg_parent_layout.setGravity(Gravity.START);
            }
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {

            MaterialTextView tvTitle;
            ImageFilterButton ibDelete;
            LinearLayout l1;
            CardView msg_card;
            LinearLayout msg_parent_layout;

            public MessageAdapterViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                ibDelete = itemView.findViewById(R.id.ibDelete);
                l1 = itemView.findViewById(R.id.l1message);
                msg_card = itemView.findViewById(R.id.msg_card);
                msg_parent_layout = itemView.findViewById(R.id.msg_parent_layout);

                ibDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("clivked", "delete clicked");
                        Log.i("delete msg id", messages.get(getAdapterPosition()).getKey());
                        messagedb.child(room_id).child(messages.get(getAdapterPosition()).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i("delete message", "successful");
                                } else {
                                    Log.i("delete unsuccessful", task.getException().toString());
                                }
                            }
                        });

                    }
                });
            }
        }
    }
}