package com.myauto.designer;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataModelSilkWorm {

    String nzakSilk;
    String vidprodSilk;
    String cenaSilk;
    String kolichestvoSilk;
    String summaSilk;
    String datoSilk;
    String datzSilk;

    String feature;


    public DataModelSilkWorm(String nzakSilk, String vidprodSilk, String cenaSilk, String kolichestvoSilk, String summaSilk, String datoSilk, String datzSilk, String feature) {
        this.nzakSilk=nzakSilk;
        this.vidprodSilk=vidprodSilk;
        this.cenaSilk=cenaSilk;
        this.kolichestvoSilk=kolichestvoSilk;
        this.summaSilk=summaSilk;
        this.datoSilk=datoSilk;
        this.datzSilk=datzSilk;

        this.feature=feature;

    }


    public String getNzakSilk() {
        return nzakSilk;
    }


    public String getVidprodSilk() {
        return vidprodSilk;
    }


    public String getCenaSilk() {
        return cenaSilk;
    }


    public String getKolichestvoSilk() {
        return kolichestvoSilk;
    }

    public String getSummaSilk() {
        return summaSilk;
    }


    public String getDatoSilk() {
        return datoSilk;
    }


    public String getDatzSilk() {
        return datzSilk;
    }



    public String getFeature() {
        return feature;
    }

}