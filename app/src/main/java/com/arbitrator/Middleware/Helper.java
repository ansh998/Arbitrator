package com.arbitrator.Middleware;

import android.content.Context;

public class Helper {


    public String url;
    public int c;
    public String arr[][];
    public Context con;


    public Helper(String url, int c, String arr[][], Context con) {
        this.url = url;
        this.arr = arr;
        this.c = c;
        this.con = con;
    }

}
