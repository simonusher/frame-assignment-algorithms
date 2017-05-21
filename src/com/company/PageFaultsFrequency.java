package com.company;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Igor on 21.05.2017.
 */
public class PageFaultsFrequency extends FrameAllocationAlgorithm {
    HashMap<Process, ArrayList<Integer>> frequency;
    double lowerLimit = 0.2;
    double upperLimit = 0.8;

    public PageFaultsFrequency(ArrayList<Request> requestQueue, ArrayList<Process> processes, int mainMemorySize) {
        super(requestQueue, processes, mainMemorySize);
        frequency = new HashMap<>();
        for (Process p : processes){
            frequency.put(p, new ArrayList<>());
        }
    }

    @Override
    protected void allocateRest() {
    }

    @Override
    public void handleRequest() {
        if(!isInMainMemory(activeRequest.page)){
            pageFaults++;
            activeRequest.process.pageFaults++;
            place = findEmpty(activeRequest.process);
            if(place == -1){
                place = findPageToRemove();
            }
            mainMemory[place].page = activeRequest.page;
            activeRequest.page.placedInMemory = timePassed;
            frequency.get(activeRequest.process).add(1);
        }
        else{
            ArrayList<Integer> temp = frequency.get(activeRequest.process);
            temp.add(0);
        }
        ArrayList<Integer> temp = frequency.get(activeRequest.process);
        if (temp.size() > 10) temp.remove(0);
        handleFrames(activeRequest.process);
        activeRequest.page.lastRequested = timePassed;
        activeRequest.page.wasRequested = true;
        requestQueue.remove(0);
        if(!isDone()) activeRequest = requestQueue.get(0);
    }

    private int countFrames(Process p){
        int x = 0;
        for (int i = 0; i < mainMemory.length; i++) {
            if(mainMemory[i].process != null && mainMemory[i].process.equals(p)){
                x++;
            }
        }
        return x;
    }

    private void handleFrames(Process process){
//        Iterator it = frequency.entrySet().iterator();
//        while (it.hasNext()){
        double x = 0;
//        Map.Entry pair = (Map.Entry)it.next();
        ArrayList <Integer> temp = frequency.get(process); /*(ArrayList<Integer>)pair.getValue();*/
        for (Integer i : temp) {
            x += i;
        }
        x /= 10;
        if(temp.size() >= 10){
            if (countFrames(/*(Process)pair.getKey()*/ process) > 1 && x <= lowerLimit) {
                removeFrames(/*(Process) pair.getKey()*/ process);
            }
            else if (x  >= upperLimit){
                addFrames(/*(Process)pair.getKey()*/ process);
            }
        }
    }


    protected void removeFrames(Process process){
        for (int i = 0; i < mainMemory.length; i++) {
            if(mainMemory[i].process != null && mainMemory[i].process.equals(process)){
                mainMemory[i].process = null;
//                mainMemory[i].page = null;
                break;
            }
        }
    }

    protected void addFrames(Process process){
        for (int i = 0; i < mainMemory.length; i++) {
            if(mainMemory[i].process == null){
                mainMemory[i].process = process;
            }
        }
    }
}
