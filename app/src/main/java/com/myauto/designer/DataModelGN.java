package com.myauto.designer;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataModelGN {

    String service;
    String remz;
    String manager;
    String tel;
    String dato;
    String datz;
    String summa;
    String statys;

    String feature;


    public DataModelGN (String service, String remz, String manager, String tel, String dato, String datz, String summa, String statys, String feature) {
        this.service=service;
        this.remz=remz;
        this.manager=manager;
        this.tel=tel;
        this.dato=dato;
        this.datz=datz;
        this.summa=summa;
        this.statys=statys;
        this.feature=feature;

    }


    public String getService() {
        return service;
    }


    public String getRemz() {
        return remz;
    }


    public String getManager() {
        return manager;
    }


    public String getTel() {
        return tel;
    }

    public String getDato() {
        return dato;
    }


    public String getDatz() {
        return datz;
    }


    public String getSumma() {
        return summa;
    }


    public String getStatys() {
        return statys;
    }


    public String getFeature() {
        return feature;
    }

}