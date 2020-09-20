package com.findyourself.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.findyourself.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseUser current_user;
    FirebaseDatabase db;
    ThisUser user;

    TextView textView;
    MaterialButton logout_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        db = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        current_user = fAuth.getCurrentUser();
        user = ThisUser.getInstance();

        loadData();

        //Log.i("Firebase User Details",current_user.getUid());
        //Log.i("Firebase User Details",Objects.requireNonNull(db.getReference("users").child(current_user.getUid()).toString()));


        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void findViews() {
        logout_btn = findViewById(R.id.logout);
        textView = findViewById(R.id.text);

    }



    private void loadData() {
        DatabaseReference ref = db.getReference("users");
        ref.child(current_user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.i("just checking", snapshot.child("username").getValue().toString());
//                Log.i("just checking 2", snapshot.getValue().toString());
                user.setUsername(Objects.requireNonNull(snapshot.child("username").getValue()).toString());
                user.setFullname(Objects.requireNonNull(snapshot.child("fullname").getValue()).toString());
                user.setGender(Objects.requireNonNull(snapshot.child("gender").getValue()).toString());
                user.setBirthday(Objects.requireNonNull(snapshot.child("birthday").getValue()).toString());

                textView.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("DatabaseError", error.getMessage());

            }
        });
    }
}