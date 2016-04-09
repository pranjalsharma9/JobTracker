package com.pranjals.nsit.jobtracker.BuildDB;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pranjals.nsit.jobtracker.R;

/**
 * Created by Pranjal on 29-03-2016.
 */
public class BuildDBFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) layoutInflater.inflate(R.layout.fragment_builddb, container, false);
        return rootView;
    }
}
