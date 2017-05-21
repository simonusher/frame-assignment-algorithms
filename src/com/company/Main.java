package com.company;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Process> processes = Generator.generateProcesses();
        ArrayList<Request> requests = Generator.generateRequestQueue(processes, 1000);
//        System.out.println(requests.size());


        EqualAllocation e = new EqualAllocation(requests, processes, Generator.numberOfFrames);
//        e.printMainMemory();
        e.handleQueue();
        e.printPageFaults("Equal allocation");

        for (Process process: processes) {
            process.pageFaults = 0;
        }

        ProportionalAllocation proportion = new ProportionalAllocation(requests, processes, Generator.numberOfFrames);
//        proportion.printMainMemory();
        proportion.handleQueue();
        proportion.printPageFaults("Proportional allocation");

        for (Process process: processes) {
            process.pageFaults = 0;
        }
        PageFaultsFrequency pageFaultsFrequency = new PageFaultsFrequency(requests, processes, Generator.numberOfFrames);
        pageFaultsFrequency.handleQueue();
        pageFaultsFrequency.printPageFaults("Page faults frequency");

        for (Process process: processes) {
            process.pageFaults = 0;
        }
        WorkingSetModel wsm = new WorkingSetModel(requests, processes, Generator.numberOfFrames);
        wsm.handleQueue();
        wsm.printPageFaults("Working set model");
    }
}
