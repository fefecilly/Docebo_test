package com.fefe.docebo_test.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fefe_ on 10/10/2017.
 */

public class SearchItem implements Parcelable {
    public String thumbnail;
    public String name;
    public String course_type;
    public String description;
    public String price;



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.thumbnail);
        dest.writeString(this.name);
        dest.writeString(this.course_type);
        dest.writeString(this.description);
        dest.writeString(this.price);
    }

    public SearchItem() {
    }

    protected SearchItem(Parcel in) {
        this.thumbnail= in.readString();
        this.name = in.readString();
        this.course_type = in.readString();
        this.description = in.readString();
        this.price = in.readString();
    }

    public static final Parcelable.Creator<SearchItem> CREATOR = new Parcelable.Creator<SearchItem>() {
        @Override
        public SearchItem createFromParcel(Parcel source) {
            return new SearchItem(source);
        }

        @Override
        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };
}


