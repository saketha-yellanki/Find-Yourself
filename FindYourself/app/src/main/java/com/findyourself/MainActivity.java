package com.findyourself;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
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
    FirebaseAuth.AuthStateListener fAuthListener;
    FirebaseUser current_user;
    FirebaseDatabase db;
    ThisUser user;

    TextView textView;
    MaterialButton logout_btn;
    MaterialButton login_btn;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        fAuth = FirebaseAuth.getInstance();
        current_user = fAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        user = ThisUser.getInstance();
        loadData();

        //Log.i("Firebase User Details",current_user.getUid());
        //Log.i("Firebase User Details",Objects.requireNonNull(db.getReference("users").child(current_user.getUid()).toString()));
        fAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

        };

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    @SuppressLint("WrongViewCast")
    private void findViews() {
        logout_btn = findViewById(R.id.logout);
        textView = findViewById(R.id.text);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(fAuthListener);
    }

    private void startSignIn() {
        String user_string = username.getText().toString();
        String pass_string = password.getText().toString();

        if (TextUtils.isEmpty(user_string) || TextUtils.isEmpty(pass_string)) {
            Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_LONG).show();

        } else {
            fAuth.signInWithEmailAndPassword(user_string, pass_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Successfully Signed In", Toast.LENGTH_LONG).show();
                        // val intent = Intent(this, MainActivity::class.java);
                        //startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Sign-in Failed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


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