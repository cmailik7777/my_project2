package com.myauto.designer;

/**
 * Created by Designer2 on 31.10.2018.
 */

public class Data {

    public String name;
    boolean checked;

    Data(String name, boolean checked) {
        this.name = name;
        this.checked = checked;

    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return checked;
    }
}