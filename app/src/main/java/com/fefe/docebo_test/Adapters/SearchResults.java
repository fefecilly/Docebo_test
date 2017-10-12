package com.fefe.docebo_test.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fefe.docebo_test.Parcelable.SearchItem;
import com.fefe.docebo_test.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by fefe_ on 10/10/2017.
 */

public class SearchResults extends BaseAdapter {

    private Activity activity;
    private ArrayList<SearchItem> results;
    private LayoutInflater inflate;




    public SearchResults(Activity a, ArrayList<SearchItem> ap ) //, MainActivity.OnPostItemSelected pis)
    {
        //Pis=pis;
        results=ap;
        activity = a;
        inflate = (LayoutInflater)a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public ViewHolder holder = null;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //MainActivity.pos=position;

        if(convertView==null){
            holder = new ViewHolder();
            convertView = inflate.inflate(R.layout.result_row, null);

            holder.name = (TextView)convertView.findViewById(R.id.row_name);
            holder.type_price = (TextView)convertView.findViewById(R.id.row_type_price);
            holder.description = (TextView)convertView.findViewById(R.id.row_description);

            holder.thumbnail = (ImageView) convertView.findViewById(R.id.row_thumbnail);

            holder.name.setText(results.get(position).name);
            holder.type_price.setText(results.get(position).course_type+" | "+results.get(position).price);
            holder.description.setText(Html.fromHtml(results.get(position).description));

            if(!results.get(position).thumbnail.equals("")&&!results.get(position).thumbnail.equals("null")) {
                String uri = "https://" + results.get(position).thumbnail;
                Picasso.with(activity.getApplicationContext())
                        .load(uri)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_error)
                        .into(holder.thumbnail);
            }

            notifyDataSetChanged();
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }
    public static class ViewHolder {
        public TextView name,type_price,description;
        public ImageView thumbnail;
    }






}