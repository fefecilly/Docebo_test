package com.fefe.docebo_test.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fefe_ on 10/10/2017.
 */

public class SortOptions implements Parcelable {
    public int atoz;
    public int ztoa;
    public String type;
    public String price_trigger;
    public String price_low_hight;



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.atoz);
        dest.writeInt(this.ztoa);
        dest.writeString(this.type);
        dest.writeString(this.price_trigger);
        dest.writeString(this.price_low_hight);
    }

    public SortOptions() {
    }

    protected SortOptions(Parcel in) {
        this.ztoa= in.readInt();
        this.atoz= in.readInt();
        this.type= in.readString();
        this.price_low_hight = in.readString();
        this.price_trigger = in.readString();
    }

    public static final Parcelable.Creator<SortOptions> CREATOR = new Parcelable.Creator<SortOptions>() {
        @Override
        public SortOptions createFromParcel(Parcel source) {
            return new SortOptions(source);
        }

        @Override
        public SortOptions[] newArray(int size) {
            return new SortOptions[size];
        }
    };
}


