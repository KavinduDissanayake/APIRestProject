package com.example.coivd_app.Screens.User;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coivd_app.R;


public class HomeFragment extends Fragment {


    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView=inflater.inflate(R.layout.home_fragment, container, false);

        init();
        return rootView;
    }

    private void init() {



    }
}