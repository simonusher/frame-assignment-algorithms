package com.company;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Igor on 03.05.2017.
 */
public class Generator {
    private static Random generator = new Random();
    private static int[] sizes = {13, 5, 100, 7, 20, 6, 8, 12, 2, 30/*, 40*/};
    public static int numberOfFrames = 80;
    private static int requestsPerProcess = 10;

    static ArrayList<Page> generateVirtualMemory(int numberOfPages){
        ArrayList<Page> virtualMemory = new ArrayList<>();
        for (int i = 0; i < numberOfPages; i++) {
            virtualMemory.add(new Page(i));
        }
        return virtualMemory;
    }

    static ArrayList<Page> generateVirtualMemory(int numberOfPages, Process process){
        ArrayList<Page> virtualMemory = new ArrayList<>();
        for (int i = 0; i < numberOfPages; i++) {
            virtualMemory.add(new Page(i, process));
        }
        return virtualMemory;
    }

    static ArrayList<Request> generateRequestQueue(Process process, int numberOfRequests){
        ArrayList<Request> requestQueue = new ArrayList<>();
        int size = process.listOfPages.size();
        Request lastGenerated;
        for (int i = 0; requestQueue.size() < numberOfRequests ;) {
            lastGenerated = new Request(i++, process.listOfPages.get(generator.nextInt(size)));
            if(requestQueue.size() < numberOfRequests) requestQueue.add(lastGenerated);
            int x;
            double k = numberOfRequests * 0.5;
            for (int j = 0; (j < k || j < 1) && requestQueue.size() < numberOfRequests; j++) {
                x = generator.nextInt(10);
                if(x < 9){
                    lastGenerated = new Request(i++, requestQueue.get(requestQueue.size() - 1).page);
                    requestQueue.add(lastGenerated);
                }
                else{
                    lastGenerated = new Request(i++, process.listOfPages.get(generator.nextInt(size)));
                    requestQueue.add(lastGenerated);
                    break;
                }
            }
        }
        return requestQueue;
    }

    static ArrayList<Request> generateRequestQueue(ArrayList<Process> processes, int numberOfRequests){
        ArrayList<Request> requestQueue = new ArrayList<>();
        while(requestQueue.size() < numberOfRequests){
            ArrayList<Request> temp;
            for (Process p : processes)  {
                temp = generateRequestQueue(p, /*p.listOfPages.size()*2*//*generator.nextInt(91) + 10*/ requestsPerProcess );
                if(requestQueue.size() + temp.size() <= numberOfRequests){
                    requestQueue.addAll(temp);
                }
                else {
                    int toAdd = numberOfRequests - requestQueue.size();
                    requestQueue.addAll(temp.subList(temp.size() - 1 - toAdd, temp.size() -1));
                    break;
                }
            }
        }
        for (int i = 0; i < requestQueue.size(); i++) {
            requestQueue.get(i).timeOfRequest = i;
        }
        return requestQueue;
    }

    static ArrayList<Process> generateProcesses(){
        ArrayList<Process> processes = new ArrayList<>();
        for (int i = 0; i < sizes.length; i++) {
            processes.add(new Process(sizes[i], i));
        }
        return processes;
    }

    static ArrayList<Process> generateProcesses(int[] tab){
        ArrayList<Process> processes = new ArrayList<>();
        for (int i = 0; i < tab.length; i++) {
            processes.add(new Process(tab[i], i));
        }
        return processes;
    }
}
