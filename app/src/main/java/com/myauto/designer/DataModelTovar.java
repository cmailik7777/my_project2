package com.myauto.designer;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataModelTovar {

    String naim;
    String kol;
    String vid;
    String summatovara;
    String id;

    String feature;


    public DataModelTovar(String naim, String kol, String vid, String summatovara, String feature, String id) {
        this.naim=naim;
        this.kol=kol;
        this.vid=vid;
        this.summatovara=summatovara;
        this.id = id;

        this.feature=feature;

    }

    public String getId() {
        return id;
    }

    public String getNaim() {
        return naim;
    }


    public String getKol() {
        return kol;
    }


    public String getVid() {
        return vid;
    }


    public String getSummatovara() {
        return summatovara;
    }



    public String getFeature() {
        return feature;
    }

}