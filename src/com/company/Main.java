package com.company;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Process> processes = Generator.generateProcesses();
        ArrayList<Request> requests = Generator.generateRequestQueue(processes, 1000);
        System.out.println(requests.size());
        EqualAllocation e = new EqualAllocation(requests, processes, 40);
        for (int i = 0; i < 40; i++) {
            System.out.println(e.mainMemory[i].process);
        }
        e.handleQueue();
        System.out.println(e.pageFaults);
    }
}
