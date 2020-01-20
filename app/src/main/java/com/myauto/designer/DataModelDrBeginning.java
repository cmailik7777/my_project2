package com.myauto.designer;

/**
 * Created by Designer2 on 11.07.2018.
 */

public class DataModelDrBeginning {
                /*

                "res": [{
                    "set": "0",
                    "id": "000000002",
                    "nomer": "565CMB02",
                    "marka": "Mitsubishi",
                    "model": "[200] DELICA SPACE GEAR/CARGO (PA-PF# )",
                    "color": "Р·РµР»РµРЅС‹Р№",
                    "pic":

                 */

    String set;
    String id;
    String nomer;
    String marka;
    String model;
    String color;
    String pic;

    public DataModelDrBeginning(String set, String id, String nomer, String marka, String model, String color, String pic) {
        this.set = set;
        this.id = id;
        this.nomer = nomer;
        this.marka = marka;
        this.model = model;
        this.color = color;
        this.pic = pic;
    }

    public String getSet() {
        return set;
    }

    public String getId() {
        return id;
    }

    public String getNomer() {
        return nomer;
    }

    public String getMarka() {
        return marka;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getPic() {
        return pic;
    }
}
