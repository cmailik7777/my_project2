package com.myauto.designer;

/**
 * Created by Designer2 on 03.07.2018.
 */

public class DataModelNews {

    String head;
    String desc;
    String text;
    String icon;
    String img;
    String mob;
    String otvet;
    String insta;
    String data;

    public DataModelNews (String head, String desc, String icon, String text, String img,String mob, String otvet, String insta, String data) {
        this.head=head;
        this.desc=desc;
        this.icon=icon;
        this.text = text;
        this.img = img;
        this.mob = mob;
        this.otvet = otvet;
        this.insta = insta;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String getText() {
        return text;
    }

    public String getImg() {
        return img;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getIcon() {
        return icon;
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
}
