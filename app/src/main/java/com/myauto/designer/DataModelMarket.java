package com.myauto.designer;

/**
 * Created by Designer2 on 18.06.2018.
 */

public class DataModelMarket {

    String imgOne;
    String imgTwo;
    String imgThree;
    String city;
    String summa;
    String article;
    String date;
    String brend;
    String head;
    String desc;


    public DataModelMarket (String imgOne, String imgTwo, String imgThree, String city, String summa, String article, String date, String brend, String head, String desc) {
        this.imgOne=imgOne;
        this.imgTwo=imgTwo;
        this.imgThree=imgThree;
        this.city=city;
        this.summa=summa;
        this.article=article;
        this.date=date;
        this.brend=brend;
        this.head=head;
        this.desc=desc;
    }

    public String getArticle() {
        return article;
    }

    public String getImgOne() {
        return imgOne;
    }

    public String getImgTwo() {
        return imgTwo;
    }

    public String getImgThree() {
        return imgThree;
    }

    public String getCity() {
        return city;
    }

    public String getSumma() {
        return summa;
    }

    public String getDate() {
        return date;
    }

    public String getBrend() {
        return brend;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }
}
