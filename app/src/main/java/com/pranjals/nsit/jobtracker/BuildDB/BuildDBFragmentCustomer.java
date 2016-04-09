package com.pranjals.nsit.jobtracker.BuildDB;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pranjals.nsit.jobtracker.R;

/**
 * Created by Pranjal on 30-03-2016.
 */
public class BuildDBFragmentCustomer extends Fragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) layoutInflater.inflate(R.layout.fragment_builddb_customer, container, false);
        rootView = ((BuildDBActivity) getActivity()).loadCustomFields(2, rootView);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        ((BuildDBActivity) getActivity()).saveCustomFields(2);
        super.onSaveInstanceState(outState);
    }
}
