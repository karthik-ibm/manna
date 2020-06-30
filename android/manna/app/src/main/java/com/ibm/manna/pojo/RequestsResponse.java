package com.ibm.manna.pojo;

import java.util.ArrayList;

public class RequestsResponse {
    ArrayList<Requests> myrequest;
    ArrayList<Requests> nearest_neighbour;

    public ArrayList<Requests> getMyrequest() {
        return myrequest;
    }

    public void setMyrequest(ArrayList<Requests> myrequest) {
        this.myrequest = myrequest;
    }

    public ArrayList<Requests> getNearest_neighbour() {
        return nearest_neighbour;
    }

    public void setNearest_neighbour(ArrayList<Requests> nearest_neighbour) {
        this.nearest_neighbour = nearest_neighbour;
    }
}
