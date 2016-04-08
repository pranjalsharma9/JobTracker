package com.pranjals.nsit.jobtracker;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Pranjal on 30-03-2016.
 */
public class BuildDBFragmentFinish extends Fragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) layoutInflater.inflate(R.layout.fragment_builddb_finish, container, false);
        return rootView;
    }

}