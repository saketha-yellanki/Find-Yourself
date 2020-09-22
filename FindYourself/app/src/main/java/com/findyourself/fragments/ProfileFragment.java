package com.findyourself.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.findyourself.R;
import com.findyourself.activities.AboutActivity;
import com.findyourself.activities.LoginActivity;
import com.findyourself.activities.TipsActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {


    MaterialButton logout;
    AppCompatImageButton menu_btn;

    String un, fn, dob, gen;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


    }

    public void showPopup(View v) {
        PopupMenu pop = new PopupMenu(getActivity(), v);
        pop.setOnMenuItemClickListener(this);
        pop.inflate(R.menu.menu_items);
        pop.show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        menu_btn = view.findViewById(R.id.menu_btn);
        logout = view.findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        return view;

    }

    public void putArgs(Bundle bundle) {
        un = bundle.getString("username");
        fn = bundle.getString("fullname");
        gen = bundle.getString("gender");
        dob = bundle.getString("dob");
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.item_tips:
                startActivity(new Intent(getActivity(), TipsActivity.class));
                break;
        }
        return true;
    }
}