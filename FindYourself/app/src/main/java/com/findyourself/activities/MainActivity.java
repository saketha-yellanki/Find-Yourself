package com.findyourself.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.findyourself.R;
import com.findyourself.fragments.AllChatsFragment;
import com.findyourself.fragments.ProfileFragment;
import com.findyourself.fragments.YourChatsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    AllChatsFragment allChatsFragment;
    ProfileFragment profileFragment;
    YourChatsFragment yourChatsFragment;

    BottomNavigationView bottomNav;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragent = null;
            switch (item.getItemId()) {
                case R.id.page_1:
                    selectedFragent = new YourChatsFragment();
                    break;
                case R.id.page_2:
                    selectedFragent = new AllChatsFragment();
                    break;
                case R.id.page_3:
                    selectedFragent = new ProfileFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragent).commit();
            return true;
        }
    };

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


        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new YourChatsFragment()).commit();


        //Log.i("Firebase User Details",current_user.getUid());
        //Log.i("Firebase User Details",Objects.requireNonNull(db.getReference("users").child(current_user.getUid()).toString()));


//        logout_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                finish();
//            }
//        });

    }

    private void sendDetailsToFragments() {

        Bundle bundle = new Bundle();
        bundle.putString("username", user.getUsername());
        bundle.putString("fullname", user.getFullname());
        bundle.putString("dob", user.getBirthday());
        bundle.putString("gender", user.getGender());

        Log.i("User details", bundle.getString("username"));

        YourChatsFragment frag1 = new YourChatsFragment();
        frag1.setArguments(bundle);

        ProfileFragment frag2 = new ProfileFragment();
        frag2.setArguments(bundle);
    }

    private void findViews() {
        bottomNav = findViewById(R.id.bottom_navigation);
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

                sendDetailsToFragments();

                //textView.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("DatabaseError", error.getMessage());

            }
        });
    }


}