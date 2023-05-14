package com.example.runningapplication.RecyclerView;

import android.widget.TextView;

public class DetailRecord_ItemData {

    private String tv_sectionDistance;
    private String tv_sectionTime;
    private String tv_sectionPace;


    public DetailRecord_ItemData(String tv_sectionDistance, String tv_sectionTime, String tv_sectionPace) {
        this.tv_sectionDistance = tv_sectionDistance;
        this.tv_sectionTime = tv_sectionTime;
        this.tv_sectionPace = tv_sectionPace;
    }

    public String getTv_sectionDistance() {
        return tv_sectionDistance;
    }

    public void setTv_sectionDistance(String tv_sectionDistance) {
        this.tv_sectionDistance = tv_sectionDistance;
    }

    public String getTv_sectionTime() {
        return tv_sectionTime;
    }

    public void setTv_sectionTime(String tv_sectionTime) {
        this.tv_sectionTime = tv_sectionTime;
    }

    public String getTv_sectionPace() {
        return tv_sectionPace;
    }

    public void setTv_sectionPace(String tv_sectionPace) {
        this.tv_sectionPace = tv_sectionPace;
    }


}
