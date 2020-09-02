package com.findyourself;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout username, fullname, password, conf_password, dob, email;
    TextInputEditText un_et, fn_et, pass_et, conf_pass_et, dob_et, email_et;
    RadioGroup gender_grp;
    MaterialButton register_btn;
    int dob_year, dob_month, dob_day;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViews();
        implementDatePicker();

        mAuth = FirebaseAuth.getInstance();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isError()) {
                    errorImplementation();
                } else {
                    String un = Objects.requireNonNull(un_et.getText()).toString();
                    String fn = Objects.requireNonNull(fn_et.getText()).toString();
                    String em = Objects.requireNonNull(email_et.getText()).toString();
                    String pass = Objects.requireNonNull(pass_et.getText()).toString();
                    String conf_pass = Objects.requireNonNull(conf_pass_et.getText()).toString();
                    String birthday = Objects.requireNonNull(dob_et.getText()).toString();

                    if (passwordValid(pass, conf_pass)) {
                        if (getAge(dob_year, dob_month, dob_day) >= 15) {
                            //register user
                            createUser(em, pass);

                        } else {
                            Toast.makeText(RegisterActivity.this, "You are under age to use this application", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private int getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    private boolean passwordValid(String p, String cp) {
        return p.equals(cp);
    }

    private boolean isError() {
        return un_et.length() == 0 || fn_et.length() == 0 || pass_et.length() == 0 || conf_pass_et.length() == 0 || dob_et.length() == 0 || email_et.length() == 0;
    }

    private void errorImplementation() {

        if (un_et.length() == 0) {
            un_et.setError("Username cannot be empty");
        }
        if (fn_et.length() == 0) {
            fn_et.setError("Full Name cannot be empty");
        }
        if (pass_et.length() == 0) {
            pass_et.setError("Password cannot be empty");
        }
        if (conf_pass_et.length() == 0) {
            conf_pass_et.setError("Confirm Password cannot be empty");
        }
        if (dob_et.length() == 0) {
            dob_et.setError("Date of Birth cannot be empty");
        }
        if (email_et.length() == 0) {
            email_et.setError("Email cannot be empty");
        }

    }

    private void implementDatePicker() {

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dob_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        dob_year = year;
                        dob_month = month;
                        dob_day = day;

                        dob_et.setText(date);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

    }

    private void findViews() {
        username = findViewById(R.id.reg_username);
        fullname = findViewById(R.id.reg_fullname);
        password = findViewById(R.id.reg_password);
        conf_password = findViewById(R.id.reg_conf_password);
        dob = findViewById(R.id.date_picker);
        gender_grp = findViewById(R.id.gen_rad_grp);
        un_et = findViewById(R.id.reg_un_et);
        fn_et = findViewById(R.id.reg_fn_et);
        pass_et = findViewById(R.id.reg_pass_et);
        conf_pass_et = findViewById(R.id.reg_conf_pass_et);
        dob_et = findViewById(R.id.date_et);
        register_btn = findViewById(R.id.reg_btn);
        email = findViewById(R.id.reg_email);
        email_et = findViewById(R.id.reg_email_et);


    }
}