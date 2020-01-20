package com.myauto.designer;

/**
 * Created by Designer2 on 18.09.2018.
 */

public class DataModelMiniRabOper {
    String minCena;
    String maxCena;
    String name;

    public DataModelMiniRabOper(String minCena, String maxCena, String name) {
        this.minCena = minCena;
        this.maxCena = maxCena;
        this.name = name;
    }

    public String getMinCena() {
        return minCena;
    }

    public String getMaxCena() {
        return maxCena;
    }

    public String getName() {
        return name;
    }
}
