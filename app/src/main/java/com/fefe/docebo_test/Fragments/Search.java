package com.fefe.docebo_test.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.fefe.docebo_test.MainActivity;
import com.fefe.docebo_test.Methods.JsonMethods;
import com.fefe.docebo_test.R;

/**
 * Created by fefe_ on 10/10/2017.
 */

public class Search  extends Fragment {
    private Context ctx;
    EditText name;
    Spinner type;
    public static Button search;
    Boolean canSearch=true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        ctx=parent.getContext();
        View view=inflater.inflate(R.layout.fragment_search,null);
        name=(EditText)view.findViewById(R.id.search_edit_name);
        type=(Spinner)view.findViewById(R.id.dialog_spinner_filter_type);
        search=(Button)view.findViewById(R.id.search_button);

        String[] spinnerArray=new String[]{"All","Classroom","Elearning","Mobile","Webinar","Learning plan"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, R.layout.spinner_row, spinnerArray);
        type.setAdapter(adapter);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canSearch) {
                    InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            canSearch=false;
                            JsonMethods jm = new JsonMethods();
                            jm.getSearchresults(name.getText().toString(), getType(type.getSelectedItemPosition()), ctx);
                            setButtonText();
                            canSearch=true;
                        }
                    });
                    t.start();
                    Snackbar.make(MainActivity.fab, "Searching", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else{
                    Snackbar.make(MainActivity.fab, "Wait previous result before searching again", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            }
        });


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }


    public String getType(int i){
        if(i==0){
            return "ALL";
        }else if(i==1){
            return "classroom";
        }else if(i==2){
            return "elearning";
        }else if(i==3){
            return "mobile";
        }else if(i==4){
            return "webinar";
        }else if(i==5){
            return "learning_plan";
        }else {
            return "ALL";
        }
    }

    public void setButtonText(){
        MainActivity.main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                search.setText("Search");
            }
        });
    }







}