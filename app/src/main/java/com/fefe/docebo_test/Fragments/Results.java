package com.fefe.docebo_test.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.fefe.docebo_test.MainActivity;
import com.fefe.docebo_test.Methods.JsonMethods;
import com.fefe.docebo_test.R;

/**
 * Created by fefe_ on 10/10/2017.
 */

public class Results  extends Fragment {
    private Context ctx;
    ListView list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        ctx=parent.getContext();
        View view=inflater.inflate(R.layout.fragment_result,null);
        list=(ListView) view.findViewById(R.id.list_result);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        list.setAdapter(MainActivity.sr);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }



}