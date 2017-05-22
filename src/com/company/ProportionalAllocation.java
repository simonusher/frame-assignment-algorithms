package com.company;

import java.util.ArrayList;

/**
 * Created by Igor on 21.05.2017.
 */
public class ProportionalAllocation extends FrameAllocationAlgorithm {

    public ProportionalAllocation(ArrayList<Request> requestQueue, ArrayList<Process> processes, int mainMemorySize) {
        super(requestQueue, processes, mainMemorySize);
    }

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
        }
        activeRequest.page.lastRequested = timePassed;
        activeRequest.page.wasRequested = true;
        requestQueue.remove(0);
//        printMainMemory();
//        System.out.println(pageFaults);
//        System.out.println();
//        System.out.println();
        if(!isDone()) activeRequest = requestQueue.get(0);
    }

    @Override
    protected void allocateRest() {
        int unallocatedFramesIndex = -1;
        for (int i = 0; i < mainMemory.length; i++) {
            if(mainMemory[i].process == null){
                unallocatedFramesIndex = i;
                break;
            }
        }
        if(unallocatedFramesIndex != -1){
            int size = mainMemory.length - unallocatedFramesIndex;
            int x = 0;
            for (Process p : processes) {
                x += p.listOfPages.size();
            }
            for (Process process : processes) {
                double k = (process.listOfPages.size() / (double)x) * size;
                for(int i = 0; i < (int)k && unallocatedFramesIndex < mainMemory.length; unallocatedFramesIndex++, i++){
                    mainMemory[unallocatedFramesIndex].process = process;
                }
            }
            Process biggestProcess = findBiggestProcess();
            while(unallocatedFramesIndex < mainMemory.length){
                mainMemory[unallocatedFramesIndex].process = biggestProcess;
                unallocatedFramesIndex++;
            }
        }
    }

    private Process findBiggestProcess(){
        Process p = processes.get(0);
        for (Process process : processes) {
            if(process.listOfPages.size() > p.listOfPages.size()) p = process;
        }
        return p;
    }
}
