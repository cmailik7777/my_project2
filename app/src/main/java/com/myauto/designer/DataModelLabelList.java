package com.myauto.designer;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataModelLabelList {

    String idList;
    String LabelListMarka;
    String LabelListGos;
    String LabelListStatys;
    String LabelListManager;
    String LabelListTel;



    public DataModelLabelList(String idList, String LabelListMarka, String LabelListGos, String LabelListStatys, String LabelListManager, String LabelListTel ) {
        this.LabelListMarka=LabelListMarka;
        this.LabelListGos=LabelListGos;
        this.LabelListStatys=LabelListStatys;
        this.LabelListManager=LabelListManager;
        this.LabelListTel=LabelListTel;
        this.idList=idList;

    }

    public String getIdList() {
        return idList;
    }

    public String getLabelListMarka() {
        return LabelListMarka;
    }

    public String getLabelListGos() {
        return LabelListGos;
    }

    public String getLabelListStatys() {
        return LabelListStatys;
    }

    public String getLabelListManager() {
        return LabelListManager;
    }

    public String getLabelListTel() {
        return LabelListTel;
    }
}
