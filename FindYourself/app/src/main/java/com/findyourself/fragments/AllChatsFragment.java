package com.findyourself.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.findyourself.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllChatsFragment extends Fragment {


    public AllChatsFragment() {
        // Required empty public constructor
    }


    public static AllChatsFragment newInstance(String param1, String param2) {
        AllChatsFragment fragment = new AllChatsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_chat_rooms, container, false);
    }
}