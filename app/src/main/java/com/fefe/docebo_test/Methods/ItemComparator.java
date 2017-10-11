package com.fefe.docebo_test.Methods;

import com.fefe.docebo_test.Parcelable.SearchItem;

import java.util.Comparator;

/**
 * Created by fefe_ on 11/10/2017.
 */

public class ItemComparator implements Comparator<SearchItem>
{
    public int compare(SearchItem left, SearchItem right) {
        return left.name.compareTo(right.name);
    }
}