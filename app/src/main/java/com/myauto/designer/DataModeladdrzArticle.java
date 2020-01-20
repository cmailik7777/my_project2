package com.myauto.designer;

/**
 * Created by Designer2 on 20.09.2018.
 */

public class DataModeladdrzArticle {
    String count;
    String nomen;
    String price;
    String org;
    String col;
    String needS;
    String needI;

    public DataModeladdrzArticle(String count, String nomen, String price, String org, String col, String needS, String needI) {
        this.count = count;
        this.nomen = nomen;
        this.price = price;
        this.org = org;
        this.col = col;
        this.needS = needS;
        this.needI = needI;
    }

    public String getCount() {
        return count;
    }

    public String getNomen() {
        return nomen;
    }

    public String getPrice() {
        return price;
    }

    public String getOrg() {
        return org;
    }

    public String getCol() {
        return col;
    }

    public String getNeedS() {
        return needS;
    }

    public String getNeedI() {
        return needI;
    }
}
