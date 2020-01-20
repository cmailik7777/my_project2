package com.myauto.designer;

/**
 * Created by Designer2 on 18.09.2018.
 */

public class DataModelRabOper {

    String brigada;
    String otvet;
    String category;
    String amount;
    String oper;
    String data;
    String count;


    public DataModelRabOper(String brigada, String otvet, String category, String amount, String oper, String data, String count) {
        this.brigada = brigada;
        this.otvet = otvet;
        this.category = category;
        this.amount = amount;
        this.oper = oper;
        this.data = data;
        this.count = count;
    }

    public String getBrigada() {
        return brigada;
    }

    public String getOtvet() {
        return otvet;
    }

    public String getCategory() {
        return category;
    }

    public String getAmount() {
        return amount;
    }

    public String getOper() {
        return oper;
    }

    public String getData() {
        return data;
    }

    public String getCount() {
        return count;
    }
}
