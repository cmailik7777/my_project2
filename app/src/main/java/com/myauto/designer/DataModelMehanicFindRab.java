package com.myauto.designer;

/**
 * Created by Designer2 on 07.11.2018.
 */

public class DataModelMehanicFindRab {

    String name,min,max;

    public DataModelMehanicFindRab(String name, String min, String max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }
}
