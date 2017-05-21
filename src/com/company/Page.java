package com.company;

/**
 * Created by Szymon on 23.04.2017.
 */
public class Page {
    int ID;
    Process process;
    int placedInMemory = 0;
    int lastRequested = 0;
    boolean wasRequested = false;

    public Page(int ID) {
        this.ID = ID;
    }

    public Page(int ID, Process process){
        this.ID = ID;
        this.process = process;
    }

    public String toString(){
        return "" + ID;
    }
}
