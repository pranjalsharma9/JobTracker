package com.pranjals.nsit.jobtracker;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Pranjal on 30-03-2016.
 */
public class BuildDBFragmentOrder extends Fragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) layoutInflater.inflate(R.layout.fragment_builddb_order, container, false);
        rootView = ((BuildDBActivity) getActivity()).loadCustomFields(1, rootView);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        ((BuildDBActivity) getActivity()).saveCustomFields(1);
        super.onSaveInstanceState(outState);
    }
}
