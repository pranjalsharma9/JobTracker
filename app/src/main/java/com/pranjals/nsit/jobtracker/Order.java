package com.pranjals.nsit.jobtracker;

/**
 * Created by Pranjal on 12-03-2016.
 */
public class Order {

    private String name, doo, doc;
    private long _id, cid, eid;
    private int curStage, totalStages;

    Order(long _id, String name, long cid, long eid, String doo, String doc, int curStage, int totalStages){
        this._id = _id;
        this.name = name;
        this.cid = cid;
        this.eid = eid;
        this.doo = doo;
        this.doc = doc;
        this.curStage = curStage;
        this.totalStages = totalStages;
    }

    public long get_id(){ return _id; }

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

    public int getCurStage() { return curStage; }

    public int getTotalStages() { return totalStages; }
}
