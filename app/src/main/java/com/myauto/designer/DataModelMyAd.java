package com.myauto.designer;

/**
 * Created by Designer2 on 19.06.2018.
 */

public class DataModelMyAd {

    String id;
    String date,header,text,cost,city,brend,article,statys,pic1,pic2,pic3;


//{"my_ad":[
// {"id":"ALM00000000000000002",
// "date":"24.05.2018",
// "header":"0001802609",
// "text":"Р¤РёР»СЊС‚СЂ С‚РѕРїР»РёРІРЅС‹Р№",
// "cost":"111",
// "city":"РђСЃС‚Р°РЅР°",
// "brend":"BOSCH",
// "article":"0001802609",
// "cancel":"РќРµС‚",
// "checkup":"Р”Р°",
// "pic1":"",
// "pic2":"",
// "pic3":""}


    public DataModelMyAd(String id, String date, String header, String text, String cost, String city, String brend, String article, String statys, String pic1, String pic2, String pic3) {
        this.id = id;
        this.date = date;
        this.header = header;
        this.text = text;
        this.cost = cost;
        this.city = city;
        this.brend = brend;
        this.article = article;
        this.statys = statys;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getHeader() {
        return header;
    }

    public String getText() {
        return text;
    }

    public String getCost() {
        return cost;
    }

    public String getCity() {
        return city;
    }

    public String getBrend() {
        return brend;
    }

    public String getArticle() {
        return article;
    }

    public String getStatys() {
        return statys;
    }

    public String getPic1() {
        return pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public String getPic3() {
        return pic3;
    }
}
