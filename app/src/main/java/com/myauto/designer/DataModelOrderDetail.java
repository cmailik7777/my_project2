package com.myauto.designer;

/**
 * Created by Designer2 on 01.08.2018.
 *
 * [{
 "num": "ALM000000001",
 "start_org": "Р¤СѓСЂРјР°РЅРѕРІР°",
 "start_adress": "Р—РµР»РµРЅС‹Р№ Р±Р°Р·Р°СЂ",
 "start_cl": "РћР»РµРі",
 "start_tel": "911",
 "stop_org": "РљСѓРЅР°РµРІР°",
 "stop_adress": "1,21",
 "stop_cl": "Р’Р°Р»РµСЂР°",
 "stop_tel": "119",
 "info": "Р’Р·СЏС‚СЊ С‚Р°РїРѕС‡РєРё",
 "owner": "Mega Motors Astana"
 }, {
 */

public class DataModelOrderDetail {
    String num;
    String start_org;
    String start_adress;
    String start_cl;
    String start_tel;

    String stop_org;
    String stop_adress;
    String stop_cl;
    String stop_tel;

    String info;
    String owner;

    String who;
    String barcode;
    String statys;

    public DataModelOrderDetail(String num, String start_org, String start_adress, String start_cl, String start_tel, String stop_org, String stop_adress, String stop_cl, String stop_tel, String info, String owner, String who,String barcode,String statys) {
        this.num = num;
        this.start_org = start_org;
        this.start_adress = start_adress;
        this.start_cl = start_cl;
        this.start_tel = start_tel;
        this.stop_org = stop_org;
        this.stop_adress = stop_adress;
        this.stop_cl = stop_cl;
        this.stop_tel = stop_tel;
        this.info = info;
        this.owner = owner;
        this.who = who;
        this.barcode = barcode;
        this.statys = statys;
    }

    public String getStatys() {
        return statys;
    }

    public String getNum() {
        return num;
    }

    public String getStart_org() {
        return start_org;
    }

    public String getStart_adress() {
        return start_adress;
    }

    public String getStart_cl() {
        return start_cl;
    }

    public String getStart_tel() {
        return start_tel;
    }

    public String getStop_org() {
        return stop_org;
    }

    public String getStop_adress() {
        return stop_adress;
    }

    public String getStop_cl() {
        return stop_cl;
    }

    public String getStop_tel() {
        return stop_tel;
    }

    public String getInfo() {
        return info;
    }

    public String getOwner() {
        return owner;
    }

    public String getWho() {
        return who;
    }

    public String getBarcode() {
        return barcode;
    }
}
