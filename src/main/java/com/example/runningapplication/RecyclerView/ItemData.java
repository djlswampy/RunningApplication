package com.example.runningapplication.RecyclerView;


public class ItemData {  //리사이클러뷰의 한 항목에 추가랄 데이터를 표현하기 위한 DTO(데이터 전송 객체)


    private String tv_distance;
    private String tv_time;
    private String tv_date;
    private String dialogText;


    public ItemData(String tv_distance, String tv_time, String tv_date, String dialogText) {
        this.tv_distance = tv_distance;
        this.tv_time = tv_time;
        this.tv_date = tv_date;
        this.dialogText = dialogText;
    }


    public String getTv_distance() {
        return tv_distance;
    }

    public void setTv_distance(String tv_distance) {
        this.tv_distance = tv_distance;
    }

    public String getTv_time() {
        return tv_time;
    }

    public void setTv_time(String tv_time) {
        this.tv_time = tv_time;
    }

    public String getTv_date() {
        return tv_date;
    }

    public void setTv_date(String tv_date) {
        this.tv_date = tv_date;
    }

    public String getDialogText() {
        return dialogText;
    }

    public void setDialogText(String dialogText) {
        this.dialogText = dialogText;
    }
}