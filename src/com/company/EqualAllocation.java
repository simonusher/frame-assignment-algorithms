package com.company;

import java.util.ArrayList;

/**
 * Created by Igor on 21.05.2017.
 */
public class EqualAllocation extends FrameAllocationAlgorithm {
    public EqualAllocation(ArrayList<Request> requestQueue, ArrayList<Process> processes, int mainMemorySize) {
        super(requestQueue, processes, mainMemorySize);
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
            int x = size / processes.size();
            for (Process process : processes) {
                for(int i = 0; i < x && unallocatedFramesIndex < mainMemory.length; unallocatedFramesIndex++, i++){
                    mainMemory[unallocatedFramesIndex].process = process;
                }
            }
        }
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
        }
        activeRequest.page.lastRequested = timePassed;
        activeRequest.page.wasRequested = true;
        requestQueue.remove(0);
        if(!isDone()) activeRequest = requestQueue.get(0);
    }
}
