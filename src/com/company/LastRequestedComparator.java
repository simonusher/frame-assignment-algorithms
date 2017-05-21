package com.company;

/**
 * Created by Igor on 03.05.2017.
 */
public class LastRequestedComparator implements PageComparatorInterface {
    @Override
    public int compare(Page p1, Page p2) {
        int x = 0;
        if(p1.lastRequested < p2.lastRequested) x = 1;
        else if (p1.lastRequested > p2.lastRequested) x = -1;
        return x;
    }
}
