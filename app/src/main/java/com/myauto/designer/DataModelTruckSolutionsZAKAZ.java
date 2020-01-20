package com.myauto.designer;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataModelTruckSolutionsZAKAZ {

    String truckOO;
    String truckOG;
    String truckOA;
    String truckOL;
    String truckPO;
    String truckPG;
    String truckPA;
    String truckPL;

    String feature;


    public DataModelTruckSolutionsZAKAZ(String truckOO, String truckOG, String truckOA, String truckOL, String truckPO, String truckPG, String truckPA, String truckPL, String feature) {
        this.truckOO=truckOO;
        this.truckOG=truckOG;
        this.truckOA=truckOA;
        this.truckOL=truckOL;
        this.truckPO=truckPO;
        this.truckPG=truckPG;
        this.truckPA=truckPA;
        this.truckPL=truckPL;

        this.feature=feature;

    }


    public String getTruckOO() {
        return truckOO;
    }


    public String getTruckOG() {
        return truckOG;
    }


    public String getTruckOA() {
        return truckOA;
    }


    public String getTruckOL() {
        return truckOL;
    }

    public String getTruckPO() {
        return truckPO;
    }


    public String getTruckPG() {
        return truckPG;
    }

    public String getTruckPA() {
        return truckPA;
    }


    public String getTruckPL() {
        return truckPL;
    }


    public String getFeature() {
        return feature;
    }

}