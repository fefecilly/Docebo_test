package com.fefe.docebo_test.Methods;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.fefe.docebo_test.Fragments.Search;
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


    public void getSearchresults(String query,String type,Context ctx){

        ArrayList<SearchItem> search=new ArrayList<>();
        if(!type.equals("ALL")){
            type="type[]="+type+"&";
        }else{
            type="$select=*&";
        }
        StringBuilder data=getJsonString("https://demomobile.docebosaas.com/learn/v1/catalog?"+type+"search_text="+query,ctx);

        search=(ArrayList<SearchItem>)result(data,search,ctx).clone();


        if(search.size()==0){
            Snackbar.make(MainActivity.fab, "No items found", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }else{
            Intent i = new Intent("search_completed");
            i.putExtra("result",search);
            ctx.sendBroadcast(i);
        }

    }


    public ArrayList<SearchItem> result_old(StringBuilder data,ArrayList<SearchItem> search,Context ctx){
        String nextPage="1";
        String selfPage="0";
        String lastPage="2";
        int page=0;
        try {
            while (!selfPage.equals(lastPage)&&!nextPage.equals("end")) {

                JSONObject o = new JSONObject(data.toString());

                JSONObject dat = o.getJSONObject("data");
                JSONArray a = dat.getJSONArray("items");

                JSONObject links = o.getJSONObject("_links");

                try {
                    JSONObject next = links.getJSONObject("next");
                    nextPage = next.getString("href");

                    JSONObject self = links.getJSONObject("self");
                    selfPage = self.getString("href");

                    JSONObject last = links.getJSONObject("last");
                    lastPage = last.getString("href");
                }catch (Exception e){
                    nextPage="end";
                    e.printStackTrace();
                }

                if(selfPage.equals(lastPage)){
                    return search;
                }else {
                    for (int i = 0; i < a.length(); i++) {
                        SearchItem sr = new SearchItem();
                        JSONObject item = a.getJSONObject(i);
                        sr.course_type = item.getString("item_type");
                        sr.name = item.getString("item_name");
                        sr.description = item.getString("item_description");
                        sr.price = item.getString("item_price");
                        sr.thumbnail = item.getString("item_thumbnail");
                        search.add(sr);
                        //Log.i("Search result",sr.name+"\n"+sr.price+"\n"+sr.course_type+"\n"+sr.description+"\n"+sr.thumbnail);
                        Log.i("Search result", "page: " + page + "  - item: " + i);
                    }
                    page++;
                    data = getJsonString(nextPage, ctx);

                }

            }
        }catch (Exception e){
            Log.i("JsonERR",e.toString());

        }

        return search;
    }

    public ArrayList<SearchItem> result(StringBuilder data,ArrayList<SearchItem> search,Context ctx){
        boolean anotherPage=true;
        while (anotherPage) {
            setLoadingPercentage(data.toString());
            JSONArray a = getItemArray(data.toString());
            String[] pages = getPages(data.toString());
            search = scanJson(a, search);
            anotherPage=isThereAnotherPage(pages[1]);
            if(anotherPage){
                data=getJsonString(pages[1],ctx);
            }
        }
        return search;
    }

    public JSONArray getItemArray(String data){
        JSONArray a=null;
        try{
            JSONObject o = new JSONObject(data);
            JSONObject dat = o.getJSONObject("data");
            a = dat.getJSONArray("items");
        }catch (Exception e){
            e.printStackTrace();
        }
        return a;
    }

    public String[] getPages(String data){
        String[] pages=new String[]{"null","null","null"};
        try {
            JSONObject o = new JSONObject(data);
            JSONObject links = o.getJSONObject("_links");

            JSONObject next = links.getJSONObject("next");
            pages[1] = next.getString("href");

            JSONObject self = links.getJSONObject("self");
            pages[0] = self.getString("href");

            JSONObject last = links.getJSONObject("last");
            pages[2] = last.getString("href");
        }catch (Exception e){
            e.printStackTrace();
        }
        return pages;
    }

    public ArrayList<SearchItem> scanJson(JSONArray a,ArrayList<SearchItem> search){
        try {
            for (int i = 0; i < a.length(); i++) {
                SearchItem sr = new SearchItem();
                JSONObject item = a.getJSONObject(i);
                sr.course_type = item.getString("course_type");
                sr.name = item.getString("item_name");
                sr.description = item.getString("item_description");
                sr.price = item.getString("item_price");
                sr.thumbnail = item.getString("item_thumbnail");
                search.add(sr);
                //Log.i("Search result",sr.name+"\n"+sr.price+"\n"+sr.course_type+"\n"+sr.description+"\n"+sr.thumbnail);
                //Log.i("Search result", "page: " + page + "  - item: " + i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return search;
    }

    public boolean isThereAnotherPage(String page){
        if(page.equals("null")){
            return false;
        }else{
            return true;
        }
    }

    public int getNumberOfPages(String data){
        int i=0;
        try{
            JSONObject o = new JSONObject(data);
            JSONObject dat = o.getJSONObject("data");
            i = dat.getInt("total_page_count");
        }catch (Exception e){
            e.printStackTrace();
        }
        return i;
    }

    public int getNumberOfCurrentPage(String data){
        int i=0;
        try{
            JSONObject o = new JSONObject(data);
            JSONObject dat = o.getJSONObject("data");
            i = dat.getInt("current_page");
        }catch (Exception e){
            e.printStackTrace();
        }
        return i;
    }

    public int getLoadingPercentage(String data){
        return (100*getNumberOfCurrentPage(data))/getNumberOfPages(data);
    }

    public void setLoadingPercentage(final String data){
        MainActivity.main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Search.search.setText("Searching  "+getLoadingPercentage(data)+"%");
            }
        });
    }
}
