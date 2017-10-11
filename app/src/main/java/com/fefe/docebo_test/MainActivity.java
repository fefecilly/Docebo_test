package com.fefe.docebo_test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.fefe.docebo_test.Adapters.SearchResults;
import com.fefe.docebo_test.Fragments.Results;
import com.fefe.docebo_test.Fragments.Search;
import com.fefe.docebo_test.Methods.ItemComparator;
import com.fefe.docebo_test.Methods.Methods;
import com.fefe.docebo_test.Parcelable.SearchItem;
import com.fefe.docebo_test.Parcelable.SortOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    public static Activity main;
    public static FloatingActionButton fab;
    Methods m=new Methods();
    public static SortOptions so=new SortOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        main=this;

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();

        settitle("Docebo test",true);
        regisisterReceiver();
        changeFragment(new Search());

        so.atoz=0;
        so.ztoa=0;
        so.price_low_hight="low";
        so.price_trigger="1";
        so.type="null";

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort) {
            if (finishedSearch){
                m.dialogFilterAndSort(main);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {

            //popTitlesBack();
            if (getSupportFragmentManager().getBackStackEntryCount() >1 ) {
                getSupportFragmentManager().popBackStackImmediate();
            } else {
                Log.i("MAIN ACTIVITY", "BACK STACK FINISHED");
                finish();
            }

            if(titles.size()>1) {
                titles.remove(titles.size()-1);
                settitle(titles.get(titles.size() - 1),false);
            }

        if(titles.size()==1) {
            finishedSearch=false;
        }

    }


    public static ArrayList<String> titles=new ArrayList<>();
    public void settitle(final String a, final boolean add){
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                setTitle(a);
                if(add){
                    titles.add(a);
                }
            }
        });
    }

    public void changeFragment(Fragment fr){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, fr);
        ft.addToBackStack(null);
        ft.commit();
    }

    BroadcastReceiver bReceiver;
    public static SearchResults sr;
    ArrayList<SearchItem> myResult;
    ArrayList<SearchItem> myResult_sorted;
    String filterType="ALL";
    boolean finishedSearch=false;
    public void regisisterReceiver(){
        final IntentFilter filter = new IntentFilter();
        filter.addAction("search_completed");
        filter.addAction("filter_sort_completed");

        bReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if( intent.getAction().equals("search_completed")) {

                    myResult=(ArrayList<SearchItem>)intent.getSerializableExtra("result");
                    sr=new SearchResults(main,myResult);
                    if(myResult.size()==1){
                        settitle(myResult.size()+" item found",true);
                    }else{
                        settitle(myResult.size()+" items found",true);
                    }
                    finishedSearch=true;
                    changeFragment(new Results());

                }else if(intent.getAction().equals("filter_sort_completed")) {


                    myResult_sorted=(ArrayList<SearchItem>)myResult.clone();
                    if(!so.type.equals("ALL")){
                        ArrayList<SearchItem> temp=new ArrayList<>();
                        for(int i=0; i<myResult_sorted.size(); i++){
                            if(myResult_sorted.get(i).course_type.equals(so.type)){
                                temp.add(myResult_sorted.get(i));
                            }
                        }
                        myResult_sorted=(ArrayList<SearchItem>)temp.clone();
                        temp.clear();
                    }

                    if(myResult_sorted.size()!=0) {
                        onBackPressed();
                        if (so.atoz == 1) {
                            Collections.sort(myResult_sorted, new Comparator<SearchItem>() {
                                public int compare(SearchItem o1, SearchItem o2) {
                                    return o1.course_type.compareTo(o2.course_type);
                                }
                            });
                        } else if (so.ztoa == 1) {
                            Collections.sort(myResult_sorted, new Comparator<SearchItem>() {
                                public int compare(SearchItem o1, SearchItem o2) {
                                    return o2.course_type.compareTo(o1.course_type);
                                }
                            });
                        }

                        sr = new SearchResults(main, myResult_sorted);
                        if (myResult_sorted.size() == 1) {
                            settitle(myResult_sorted.size() + " item found", true);
                        } else {
                            settitle(myResult_sorted.size() + " items found", true);
                        }
                        filterType=so.type;
                        finishedSearch = true;
                        changeFragment(new Results());
                    }else{
                        so.type=filterType;
                        Snackbar.make(MainActivity.fab, "No result after filtering", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            }
        };
        registerReceiver(bReceiver, filter);
    }


}
