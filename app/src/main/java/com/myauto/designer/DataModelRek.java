package com.myauto.designer;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataModelRek {

    String rektitle;
    String rektext;

    String feature;


    public DataModelRek(String rektitle, String rektext, String feature) {
        this.rektitle=rektitle;
        this.rektext=rektext;

        this.feature=feature;

    }


    public String getRektitle() {
        return rektitle;
    }


    public String getRektext() {
        return rektext;
    }

    public String getFeature() {
        return feature;
    }

}