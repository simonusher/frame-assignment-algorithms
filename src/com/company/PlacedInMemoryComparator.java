package com.company;

/**
 * Created by Szymon on 24.04.2017.
 */
public class PlacedInMemoryComparator implements PageComparatorInterface {
    public int compare(Page p1, Page p2){
        int x = 0;
        if(p1.placedInMemory < p2.placedInMemory) x = 1;
        else if(p1.placedInMemory > p2.placedInMemory) x = -1;
        return x;
    }
}
