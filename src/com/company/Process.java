package com.company;

import java.util.List;

/**
 * Created by Igor on 21.05.2017.
 */
public class Process {
    List<Page> listOfPages;
    int index;
    public Process(List<Page> listOfPages) {
        this.listOfPages = listOfPages;
    }

    public Process(int numberOfPages, int index){
        this.listOfPages = Generator.generateVirtualMemory(numberOfPages, this);
        this.index = index;
    }

    @Override
    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        for (Page p : listOfPages) {
//            sb.append(p.toString());
//            sb.append("\n");
//        }
//        return sb.toString();
        return Integer.toString(index);
    }
}
