package com.myauto.designer;

/**
 * Created by Designer2 on 19.09.2018.
 */

public class DataModelrzArticle {
    String count;
    String date;
    String article;
    String otvet;
    String kol;
    String summa;
    String stock;
    String amount;
    String nomen;

    public DataModelrzArticle(String count, String date, String article, String otvet, String kol, String summa, String stock, String amount, String nomen) {
        this.count = count;
        this.date = date;
        this.article = article;
        this.otvet = otvet;
        this.kol = kol;
        this.summa = summa;
        this.stock = stock;
        this.amount = amount;
        this.nomen = nomen;
    }

    public String getCount() {
        return count;
    }

    public String getDate() {
        return date;
    }

    public String getArticle() {
        return article;
    }

    public String getOtvet() {
        return otvet;
    }

    public String getKol() {
        return kol;
    }

    public String getSumma() {
        return summa;
    }

    public String getStock() {
        return stock;
    }

    public String getAmount() {
        return amount;
    }

    public String getNomen() {
        return nomen;
    }
}
