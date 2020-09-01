package com.findyourself;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout username, fullname, password, conf_password, dob;
    TextInputEditText un_et, fn_et, pass_et, conf_pass_et, dob_et;
    RadioGroup gender_grp;
    MaterialButton register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViews();
        implementDatePicker();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorImplementation();

            }
        });


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


    }
}