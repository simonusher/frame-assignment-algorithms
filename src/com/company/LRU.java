package com.company;

import java.util.ArrayList;

/**
 * Created by Igor on 03.05.2017.
 */
public class LRU extends PageReplacementAlgorithm {
    LastRequestedComparator comparator = new LastRequestedComparator();

    public LRU(ArrayList<Request> requestQueue, ArrayList<Page> virtualMemory, int virtualMemorySize) {
        super(requestQueue, virtualMemory, virtualMemorySize);
    }

    @Override
    public int findPageToRemove() {
        Page p = mainMemory[0];
        int index = 0;
        for (int i = 1; i < mainMemory.length; i++) {
            if (comparator.compare(p, mainMemory[i]) == -1){
                p = mainMemory[i];
                index = i;
            }
        }
        return index;

    }
}
