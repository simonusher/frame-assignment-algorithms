package com.company;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Process> processes = Generator.generateProcesses();
        ArrayList<Request> requests = Generator.generateRequestQueue(processes, 1000);
        System.out.println(requests.size());
    }
}
