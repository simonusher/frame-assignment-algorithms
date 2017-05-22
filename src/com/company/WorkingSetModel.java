package com.company;

import java.util.*;

/**
 * Created by Igor on 22.05.2017.
 */
public class WorkingSetModel extends FrameAllocationAlgorithm{
    HashMap<Process, ArrayList<Page>> workingSets;
    int deltaTime = 10;


    public WorkingSetModel(ArrayList<Request> requestQueue, ArrayList<Process> processes, int mainMemorySize) {
        super(requestQueue, processes, mainMemorySize);
        workingSets = new HashMap<>();
        for (Process process : processes) {
            workingSets.put(process, new ArrayList<>());
        }
    }

    @Override
    protected void allocateRest() {

    }

    @Override
    public void handleRequest() {
        if(!isInMainMemory(activeRequest.page)) {
            pageFaults++;
            activeRequest.process.pageFaults++;
            place = findEmpty(activeRequest.process);
            if (place == -1) {
                try{
                    place = findPageToRemove();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            mainMemory[place].page = activeRequest.page;
            activeRequest.page.placedInMemory = timePassed;
        }
        activeRequest.page.lastRequested = timePassed;
        activeRequest.page.wasRequested = true;
        ArrayList<Page> temp = workingSets.get(activeRequest.process);
        temp.add(activeRequest.page);
        if(temp.size() > deltaTime) temp.remove(0);
        handleWorkingSets();
        requestQueue.remove(0);
        if(!isDone()) activeRequest = requestQueue.get(0);
    }

    private void handleWorkingSets(){
        Iterator it = workingSets.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Set<Page> hs = new HashSet<>();
            ArrayList <Page> temp = (ArrayList<Page>)pair.getValue();
            if(temp.size() < 10) continue;
            hs.addAll(temp);
            int numberOfFrames = countFrames((Process)pair.getKey());
            if(hs.size() < numberOfFrames){
                if(numberOfFrames - hs.size() > 1){
                    removeFrames((Process)pair.getKey(), numberOfFrames - hs.size());
                }
                else{
                    removeFrames((Process)pair.getKey(), numberOfFrames - hs.size() + 1);
                }
            }
            else if(hs.size() > numberOfFrames) addFrames((Process)pair.getKey(), hs.size() - numberOfFrames);
        }
    }

    private void removeFrames(Process p, int numberOfFrames){
        for (int i = mainMemory.length - 1, x = 0; i >= 0 && x < numberOfFrames; i--) {
            if(mainMemory[i].process != null && mainMemory[i].process.equals(p)){
                mainMemory[i].process = null;
                x++;
            }
        }
    }

    private void addFrames(Process p, int numberOfFrames){
        for (int i = 0, x = 0; i < mainMemory.length && x < numberOfFrames; i++) {
            if(mainMemory[i].process == null) mainMemory[i].process = p;
        }
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
}
