package com.company;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Process> processes = Generator.generateProcesses();
        ArrayList<Request> requests = Generator.generateRequestQueue(processes, 1000);

        ProportionalAllocation proportion = new ProportionalAllocation(requests, processes, Generator.numberOfFrames);
//        proportion.printMainMemory();
        proportion.handleQueue();
//        System.out.println(requests);
        proportion.printPageFaults("Proportional allocation");

        for (Process process: processes) {
            process.pageFaults = 0;
            for (Page p :
                    process.listOfPages) {
                p.reset();
            }
        }

        EqualAllocation e = new EqualAllocation(requests, processes, Generator.numberOfFrames);
//        e.printMainMemory();
        e.handleQueue();
        e.printPageFaults("Equal allocation");



        for (Process process: processes) {
            process.pageFaults = 0;
            for (Page p :
                    process.listOfPages) {
                p.reset();
            }
        }
        PageFaultsFrequency pageFaultsFrequency = new PageFaultsFrequency(requests, processes, Generator.numberOfFrames);
        pageFaultsFrequency.handleQueue();
        pageFaultsFrequency.printPageFaults("Page faults frequency");

        for (Process process: processes) {
            process.pageFaults = 0;
            for (Page p :
                    process.listOfPages) {
                p.reset();
            }
        }
        WorkingSetModel wsm = new WorkingSetModel(requests, processes, Generator.numberOfFrames);
        wsm.handleQueue();
        wsm.printPageFaults("Working set model");
    }
}
