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
//        printMainMemory();
        allocateRest();
//        printMainMemory();
    }

    protected void allocateNecessaryFrames(){
        int index = 0;
        List<Page> listOfPages;
        for (Process p : processes) {
            if(index < mainMemory.length){
                listOfPages = p.listOfPages;
                if(listOfPages.size() < 5) {
//                    for (int i = 0; i < 2; i++) {
                        mainMemory[index++].process = p;
//                    }
                }
                else {
                    int size = (int)(Math.round(0.3 * listOfPages.size()));
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
        while(mainMemory[index].process != null){
            if(mainMemory[index].process.equals(activeRequest.process)) break;
            else index ++;
        }
        Frame f = mainMemory[index];
        for (int i = index + 1; i < mainMemory.length; i++) {
            if(mainMemory[i].process != null && mainMemory[i].process.equals(activeRequest.process)
                    && comparator.compare(f.page, mainMemory[i].page) == -1){
                f = mainMemory[i];
                index = i;
            }
        }
        return index;
    }

    public int findEmpty(Process p){
        for (int i = 0; i < mainMemory.length; i++) {
            if(mainMemory[i].page == null && (mainMemory[i].process == null || mainMemory[i].process.equals(p))){
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

    public void printPageFaults (String name){
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" page faults: ");
        sb.append(pageFaults);
        sb.append("\n");
        sb.append("[");
        for (Process process : processes) {
            sb.append(process.pageFaults);
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        System.out.println(sb.toString());
    }

    public void printMainMemory(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < mainMemory.length; i++) {
            sb.append(mainMemory[i].process);
            sb.append(" ");
            sb.append(mainMemory[i].page);
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        System.out.println(sb.toString());
    }

}
