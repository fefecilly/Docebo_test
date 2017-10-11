package com.fefe.docebo_test.Methods;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.fefe.docebo_test.MainActivity;
import com.fefe.docebo_test.Parcelable.SearchItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by fefe_ on 10/10/2017.
 */

public class JsonMethods {
    Methods m=new Methods();

    public StringBuilder getJsonString(String link,Context ctx){
        StringBuilder cat_result = new StringBuilder();
        if(m.isOnline(ctx)){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(link);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    cat_result.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();

            }
        }
        return cat_result;
    }


    public ArrayList<SearchItem> getSearchresults(String query,String type,Context ctx){

        ArrayList<SearchItem> search=new ArrayList<>();
        if(!type.equals("ALL")){
            type="type[]="+type+"&";
        }else{
            type="$select=*&";
        }
        StringBuilder data=getJsonString("https://demomobile.docebosaas.com/learn/v1/catalog?"+type+"search_text="+query,ctx);
        try {
            JSONObject o=new JSONObject(data.toString());
            //o=o.getJSONObject("array");
            o=o.getJSONObject("data");
            JSONArray a=o.getJSONArray("items");

            for (int i = 0; i <a.length(); i++) {
                SearchItem sr=new SearchItem();
                JSONObject item= a.getJSONObject(i);
                sr.course_type=item.getString("item_type");
                sr.name=item.getString("item_name");
                sr.description=item.getString("item_description");
                sr.price=item.getString("item_price");
                sr.thumbnail=item.getString("item_thumbnail");
                search.add(sr);
                Log.i("Search result",sr.name+"\n"+sr.price+"\n"+sr.course_type+"\n"+sr.description+"\n"+sr.thumbnail);

            }

            if(search.size()==0){
                Snackbar.make(MainActivity.fab, "No items found", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }else{
                Intent i = new Intent("search_completed");
                i.putExtra("result",search);
                ctx.sendBroadcast(i);
            }


        }catch (Exception e){
            Log.i("JsonERR",e.toString());
        }
        return search;
    }



}
