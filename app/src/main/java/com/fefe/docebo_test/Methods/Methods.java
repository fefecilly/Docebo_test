package com.fefe.docebo_test.Methods;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.fefe.docebo_test.Parcelable.SortOptions;
import com.fefe.docebo_test.R;

import static com.fefe.docebo_test.MainActivity.so;

/**
 * Created by fefe_ on 10/10/2017.
 */

public class Methods {

    public boolean isOnline(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void dialogFilterAndSort(final Activity main){
        final AlertDialog.Builder dialogBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogBuilder = new AlertDialog.Builder(main, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        } else {
            dialogBuilder = new AlertDialog.Builder(main);
        }
        LayoutInflater inflater = main.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_sort, null);
        dialogBuilder.setView(dialogView);

        TextView clearAll=(TextView)dialogView.findViewById(R.id.dialog_text_clear);
       /* TextView filterType=(TextView)dialogView.findViewById(R.id.dialog_text_filter_type);
        TextView filterPrice=(TextView)dialogView.findViewById(R.id.dialog_text_filter_price);*/
        final Spinner filterType=(Spinner) dialogView.findViewById(R.id.dialog_spinner_filter_type);

        final RadioButton aToZ=(RadioButton) dialogView.findViewById(R.id.dialog_radio_a_z);
        final RadioButton zToA=(RadioButton) dialogView.findViewById(R.id.dialog_radio_z_a);


        final String[] spinnerArray=new String[]{"All","Classroom","Elearning","Mobile","Webinar","Learning plan"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(main.getApplicationContext(), R.layout.spinner_row, spinnerArray);
        filterType.setAdapter(adapter);
        filterType.setSelection(getIndex(so.type));


        if(so.atoz==1) {
            aToZ.setChecked(true);
        }
        if(so.ztoa==1) {
            zToA.setChecked(true);
        }

        aToZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aToZ.setChecked(true);
                zToA.setChecked(false);
                so.atoz=1;
                so.ztoa=0;
            }
        });

        zToA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zToA.setChecked(true);
                aToZ.setChecked(false);
                so.atoz=0;
                so.ztoa=1;
            }
        });

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aToZ.setChecked(false);
                zToA.setChecked(false);
                //so.atoz=0;
                //so.ztoa=0;
                filterType.setSelection(0);
            }
        });


        dialogBuilder.setTitle("Sort and filter items by type")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(aToZ.isChecked()){
                            so.atoz=1;
                        }else{
                            so.atoz=0;
                        }

                        if(zToA.isChecked()){
                            so.ztoa=1;
                        }else{
                            so.ztoa=0;
                        }



                        so.type=getType(filterType.getSelectedItemPosition());
                        Intent i = new Intent("filter_sort_completed");
                        main.getApplicationContext().sendBroadcast(i);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.show();
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

    public int getIndex(String s){
        if(s.equals("ALL")){
            return 0;
        }else if(s.equals("classroom")){
            return 1;
        }else if(s.equals("elearning")){
            return 2;
        }else if(s.equals("mobile")){
            return 3;
        }else if(s.equals("webinar")){
            return 4;
        }else if(s.equals("learning_plan")){
            return 5;
        }else {
            return 0;
        }
    }


}
