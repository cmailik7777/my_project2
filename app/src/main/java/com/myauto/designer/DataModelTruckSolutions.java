package com.myauto.designer;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataModelTruckSolutions {

    String servicetruck;
    String nzaktruck;
    String ves;
    String obem;
    String mest;
    String summatruck;

    String feature;


    public DataModelTruckSolutions(String servicetruck, String nzaktruck, String ves, String obem, String mest, String summatruck, String feature) {
        this.servicetruck=servicetruck;
        this.nzaktruck=nzaktruck;
        this.ves=ves;
        this.obem=obem;
        this.mest=mest;
        this.summatruck=summatruck;

        this.feature=feature;

    }


    public String getServicetruck() {
        return servicetruck;
    }


    public String getNzaktruck() {
        return nzaktruck;
    }


    public String getVes() {
        return ves;
    }


    public String getObem() {
        return obem;
    }

    public String getMest() {
        return mest;
    }


    public String getSummatruck() {
        return summatruck;
    }


    public String getFeature() {
        return feature;
    }

}