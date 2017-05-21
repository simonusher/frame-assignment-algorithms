package com.company;

/**
 * Created by Szymon on 23.04.2017.
 */
public class Request {
    int timeOfRequest;
    Process process;
    Page page;

    public Request(int timeOfRequest, Page page) {
        this.timeOfRequest = timeOfRequest;
        this.page = page;
        this.process = page.process;
    }

    @Override
    public String toString() {
        return "" + page.toString();
    }
}
