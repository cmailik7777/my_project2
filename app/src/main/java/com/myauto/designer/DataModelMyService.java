package com.myauto.designer;

/**
 * Created by Designer2 on 11.06.2018.
 */

public class DataModelMyService {

    String photo;
    String name;
    String type;
    String tel;



    public DataModelMyService (String photo, String name, String type, String tel) {
        this.photo=photo;
        this.name=name;
        this.type=type;
        this.tel=tel;

    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getTel() {
        return tel;
    }
}
