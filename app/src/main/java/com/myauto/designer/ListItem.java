package com.myauto.designer;

/**
 * Created by Designer2 on 22.10.2018.
 */

public class ListItem {

    private String head;
    private String desc;
    private String imageUrl;
    private String img;
    private String mob;
    private String otvet;
    private String insta;
    private String data;

    String http = "http://gps-monitor.kz/oleg/mobile/news/";

    public ListItem(String head, String desc, String imageUrl, String img, String mob, String otvet, String insta, String data) {
        this.head = head;
        this.desc = desc;
        this.imageUrl = http+imageUrl;
        this.img = http+img;
        this.mob = mob;
        this.otvet = otvet;
        this.insta = insta;
        this.data = data;
    }

    public String getImg() {
        return img;
    }

    public String getMob() {
        return mob;
    }

    public String getOtvet() {
        return otvet;
    }

    public String getInsta() {
        return insta;
    }

    public String getData() {
        return data;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
