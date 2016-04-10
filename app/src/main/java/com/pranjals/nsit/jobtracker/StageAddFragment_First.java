package com.pranjals.nsit.jobtracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Pranjal Verma on 4/10/2016.
 */
public class StageAddFragment_First extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stage_add_fragment_first, container, false);
        ((StageAddActivity)getActivity()).setFirstFragmentViewGroup(view);
        return view;
    }
}
