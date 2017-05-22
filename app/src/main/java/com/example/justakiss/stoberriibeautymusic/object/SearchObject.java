package com.example.justakiss.stoberriibeautymusic.object;

/**
 * Created by justakiss on 14/10/2016.
 */
public class SearchObject {
    public String Info1, Info2, Info3, Info4 = null;
    // constructor for adding sample data
    public SearchObject(String info1, String info2, String info3){
        this.Info1 = info1;
        this.Info2 = info2;
        this.Info3 = info3;
    }
    public SearchObject(String info1, String info2, String info3, String info4){
        this.Info1 = info1;
        this.Info2 = info2;
        this.Info3 = info3;
        this.Info4 = info4;
    }
}
