package com.myauto.designer;

/**
 * Created by Designer2 on 15.10.2018.
 */

public class DataModelRZMehanic {
    String name;
    String manag;
    String num;
    String data;
    String gos;

    public DataModelRZMehanic(String name, String manag, String num, String data, String gos) {
        this.name = name;
        this.manag = manag;
        this.num = num;
        this.data = data;
        this.gos = gos;
    }

    public String getName() {
        return name;
    }

    public String getManag() {
        return manag;
    }

    public String getNum() {
        return num;
    }

    public String getData() {
        return data;
    }

    public String getGos() {
        return gos;
    }
}
