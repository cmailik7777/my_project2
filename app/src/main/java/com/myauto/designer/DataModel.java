package com.myauto.designer;

/**
 * Created by Designer2 on 01.12.2017.
 */

public class DataModel {

    String name;
    String type;
    String version_number;
    String feature;
    String model;
    String Image;
    String Oil_Logo;

    public DataModel(String name, String type, String version_number, String feature,String model, String Image, String Oil_Logo ) {
        this.name=name;
        this.type=type;
        this.version_number=version_number;
        this.feature=feature;
        this.model=model;
        this.Image=Image;
        this.Oil_Logo=Oil_Logo;
    }

    public String getOil_Logo() {
        return Oil_Logo;
    }

    public String getImage() {
        return Image;
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getVersion_number() {
        return version_number;
    }

    public String getFeature() {
        return feature;
    }

}