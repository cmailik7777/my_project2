package com.myauto.designer;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataModelPrint {

    String nzak;
    String vidprod;
    String cena;
    String kolichestvo;
    String summaprint;
    String datoprint;
    String datzprint;

    String feature;


    public DataModelPrint(String nzak, String vidprod, String cena, String kolichestvo, String summaprint, String datoprint, String datzprint, String feature) {
        this.nzak=nzak;
        this.vidprod=vidprod;
        this.cena=cena;
        this.kolichestvo=kolichestvo;
        this.summaprint=summaprint;
        this.datoprint=datoprint;
        this.datzprint=datzprint;

        this.feature=feature;

    }


    public String getNzak() {
        return nzak;
    }


    public String getVidprod() {
        return vidprod;
    }


    public String getCena() {
        return cena;
    }


    public String getKolichestvo() {
        return kolichestvo;
    }

    public String getSummaprint() {
        return summaprint;
    }


    public String getDatoprint() {
        return datoprint;
    }


    public String getDatzprint() {
        return datzprint;
    }



    public String getFeature() {
        return feature;
    }

}