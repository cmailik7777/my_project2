package com.myauto.designer;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataModelLabelListManag {

    String LabelListManagerNZ,LabelListManagerMarka,LabelListManagerGos,LabelListManagerTel,LabelListManagerClient;
    String LabelListManagerDescription;



    public DataModelLabelListManag(String LabelListManagerNZ, String LabelListManagerMarka, String LabelListManagerGos, String LabelListManagerTel, String LabelListManagerClient, String LabelListManagerDescription ) {

        this.LabelListManagerNZ=LabelListManagerNZ;
        this.LabelListManagerMarka=LabelListManagerMarka;
        this.LabelListManagerGos=LabelListManagerGos;
        this.LabelListManagerTel=LabelListManagerTel;
        this.LabelListManagerClient=LabelListManagerClient;
        this.LabelListManagerDescription=LabelListManagerDescription;

    }

    public String getLabelListManagerNZ() {
        return LabelListManagerNZ;
    }

    public String getLabelListManagerMarka() {
        return LabelListManagerMarka;
    }

    public String getLabelListManagerGos() {
        return LabelListManagerGos;
    }

    public String getLabelListManagerTel() {
        return LabelListManagerTel;
    }

    public String getLabelListManagerClient() {
        return LabelListManagerClient;
    }

    public String getLabelListManagerDescription() {
        return LabelListManagerDescription;
    }
}
