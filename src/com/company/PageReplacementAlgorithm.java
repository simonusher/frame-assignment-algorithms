package com.company;

import java.util.ArrayList;

/**
 * Created by Szymon on 23.04.2017.
 */
public abstract class PageReplacementAlgorithm {
    int pageFaults = 0;
    int timePassed = 0;
    ArrayList<Request> requestQueue;
    ArrayList<Page> virtualMemory;
    Page[] mainMemory;
    Request activeRequest;
    int place;

    public PageReplacementAlgorithm() {
    }

    public PageReplacementAlgorithm(ArrayList<Request> requestQueue, ArrayList<Page> virtualMemory, int virtualMemorySize) {
        this.requestQueue = (ArrayList)requestQueue.clone();
        this.virtualMemory = virtualMemory;
        this.mainMemory = new Page[virtualMemorySize];
    }

    public void handleQueue(){
        activeRequest = requestQueue.get(0);
        while (!isDone()){
//            System.out.println(Arrays.toString(mainMemory));
            if(activeRequest.timeOfRequest <= timePassed){
                handleRequest();
            }
            timePassed++;
        }
//        System.out.println(Arrays.toString(mainMemory));
    }

    public abstract int findPageToRemove();
    public void handleRequest() {
        if(!isInMainMemory(activeRequest.page)){
            pageFaults++;
            place = findEmpty();
            if(place == -1){
                place = findPageToRemove();
            }
            mainMemory[place] = activeRequest.page;
            activeRequest.page.placedInMemory = timePassed;
        }
        activeRequest.page.lastRequested = timePassed;
        activeRequest.page.wasRequested = true;
        requestQueue.remove(0);
        if(!isDone()) activeRequest = requestQueue.get(0);
    }

    public int findEmpty(){
        for (int i = 0; i < mainMemory.length; i++) {
            if(mainMemory[i] == null) return i;
        }
        return -1;
    }

    public boolean isInMainMemory(Page p){
        for (int i = 0; i < mainMemory.length; i++) {
            if(p.equals(mainMemory[i])) return true;
        }
        return false;
    }

    public boolean isDone() {
        return requestQueue.size() == 0;
    }
}
