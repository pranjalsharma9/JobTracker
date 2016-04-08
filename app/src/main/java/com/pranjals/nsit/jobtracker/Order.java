package com.pranjals.nsit.jobtracker;

/**
 * Created by Pranjal on 12-03-2016.
 */
public class Order {

    private String name, doo, doc;
    private long cid, eid;

    Order(String name, long cid, long eid, String doo, String doc){
        this.name = name;
        this.cid = cid;
        this.eid = eid;
        this.doo = doo;
        this.doc = doc;
    }

    public long getCid() {
        return cid;
    }

    public String getDoc() {
        return doc;
    }

    public String getDoo() {
        return doo;
    }

    public long getEid() { return eid; }

    public String getName() { return name; }
}
