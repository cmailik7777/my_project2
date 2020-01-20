package com.myauto.designer;

/**
 * Created by Designer2 on 11.06.2018.
 */

public class DataModelWheels {

    String date;
    String gos;
    String description;
    String brand;
    String size;
    String count;
    String count_month;
    String sum;

    //{"wheels":[{"date":"10.04.2018", "gos":"602DNA02", "description":"Р—РёєР°", "brand":"Yokohama", "size":"215/60/R16", "count":"4", "count_month":"2", "sum":"1В 000"} ]}

    public DataModelWheels (String date, String gos, String description, String brand, String size, String count, String count_month, String sum) {
        this.date=date;
        this.gos=gos;
        this.description=description;
        this.brand=brand;
        this.size=size;
        this.count=count;
        this.count_month=count_month;
        this.sum=sum;

    }

    public String getDate() {
        return date;
    }

    public String getGos() {
        return gos;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public String getSize() {
        return size;
    }

    public String getCount() {
        return count;
    }

    public String getCount_month() {
        return count_month;
    }

    public String getSum() {
        return sum;
    }
}
