package com.myauto.designer;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataModelLabelListDetail {

    String LablesListDetailDescription;
    String LablesListDetailAuthor;

    String LablesListDetailChecked;


    public DataModelLabelListDetail(String LablesListDetailDescription, String LablesListDetailAuthor, String LablesListDetailChecked ) {
        this.LablesListDetailDescription=LablesListDetailDescription;
        this.LablesListDetailAuthor=LablesListDetailAuthor;
        this.LablesListDetailChecked=LablesListDetailChecked;
    }

    public String getLablesListDetailDescription() {
        return LablesListDetailDescription;
    }

    public String getLablesListDetailAuthor() {
        return LablesListDetailAuthor;
    }

    public String getLablesListDetailChecked() {
        return LablesListDetailChecked;
    }
}
