package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 21.05.2017.
 */
public abstract class FrameAllocationAlgorithm {
    int pageFaults = 0;
    int timePassed = 0;
    ArrayList<Request> requestQueue;
    ArrayList<Process> processes;
    ArrayList<Page> virtualMemory;
    Frame[] mainMemory;
    Request activeRequest;
    int place;
    LastRequestedComparator comparator;

    public FrameAllocationAlgorithm() {
    }

    public FrameAllocationAlgorithm(ArrayList<Request> requestQueue, ArrayList<Process> processes, int mainMemorySize) {
        this.requestQueue = (ArrayList)requestQueue.clone();
        this.processes = processes;
        this.virtualMemory = new ArrayList<>();
        for (Process p : processes) {
            virtualMemory.addAll(p.listOfPages);
        }
        this.mainMemory = new Frame[mainMemorySize];
        for (int i = 0; i < mainMemorySize; i++) {
            mainMemory[i] = new Frame();
        }
        comparator = new LastRequestedComparator();
        allocateNecessaryFrames();
        allocateRest();
    }

    protected void allocateNecessaryFrames(){
        int index = 0;
        List<Page> listOfPages;
        for (Process p : processes) {
            if(index < mainMemory.length){
                listOfPages = p.listOfPages;
                if(listOfPages.size() < 5) {
                    mainMemory[index++].process = p;
                }
                else {
                    int size = (int)(Math.round(0.2 * listOfPages.size()));
                    if(mainMemory.length - index > size){
                        for (int i = 0; i < size; i++) {
                            mainMemory[index++].process = p;
                        }
                    }
                    else throw new NotEnoughMemoryException();
                }
            }
            else throw new NotEnoughMemoryException();
        }
    }

    protected abstract void allocateRest();

    public abstract void handleRequest();

    public void handleQueue(){
        activeRequest = requestQueue.get(0);
        while (!isDone()){
            if(activeRequest.timeOfRequest <= timePassed){
                handleRequest();
            }
            timePassed++;
        }
    }

    public int findPageToRemove() {
        int index = 0;
        while(!mainMemory[index].process.equals(activeRequest.process)) index ++;
        Frame f = mainMemory[index];
        for (int i = index + 1; i < mainMemory.length; i++) {
            if(!mainMemory[i].process.equals(activeRequest.process)) index ++;
            else if (comparator.compare(f.page, mainMemory[i].page) == -1){
                f = mainMemory[i];
                index = i;
            }
        }
        return index;
    }

    public int findEmpty(Process p){
        for (int i = 0; i < mainMemory.length; i++) {
            if(mainMemory[i].page == null && (mainMemory[i].process.equals(p)) || mainMemory[i].process == null){
                return i;
            }
        }
        return -1;
    }

    public boolean isInMainMemory(Page p){
        for (int i = 0; i < mainMemory.length; i++) {
            if(mainMemory[i].page != null && mainMemory[i].page.equals(p)) return true;
        }
        return false;
    }

    public boolean isDone() {
        return requestQueue.size() == 0;
    }
}
