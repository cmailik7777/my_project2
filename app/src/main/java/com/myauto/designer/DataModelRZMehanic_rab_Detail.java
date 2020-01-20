package com.myauto.designer;

/**
 * Created by Designer2 on 16.10.2018.
 */

public class DataModelRZMehanic_rab_Detail {
    String brig;
    String time;
    String otvet;
    String categ;
    String rab;
    String cod;
    String ispoln;
    String data;

    public DataModelRZMehanic_rab_Detail(String brig, String time, String otvet, String categ, String rab, String cod, String ispoln, String data) {
        this.brig = brig;
        this.time = time;
        this.otvet = otvet;
        this.categ = categ;
        this.rab = rab;
        this.cod = cod;
        this.ispoln = ispoln;
        this.data = data;
    }

    public String getCod() {
        return cod;
    }

    public String getIspoln() {
        return ispoln;
    }

    public String getData() {
        return data;
    }

    public String getBrig() {
        return brig;
    }

    public String getTime() {
        return time;
    }

    public String getOtvet() {
        return otvet;
    }

    public String getCateg() {
        return categ;
    }

    public String getRab() {
        return rab;
    }
}
