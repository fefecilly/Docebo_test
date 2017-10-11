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
import com.fefe.docebo_test.Parcelable.SearchItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Activity main;
    public static FloatingActionButton fab;

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
    public void regisisterReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("search_completed");

        bReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if( intent.getAction().equals("search_completed")) {
                    ArrayList<SearchItem> s=(ArrayList<SearchItem>)intent.getSerializableExtra("result");
                    sr=new SearchResults(main,s);
                    if(s.size()==1){
                        settitle(s.size()+" item found",true);
                    }else{
                        settitle(s.size()+" items found",true);
                    }
                    changeFragment(new Results());
                }else if(intent.getAction().equals("lll")) {

                }
            }
        };
        registerReceiver(bReceiver, filter);
    }


}
