package com.sabithpkcmnr.pcloudlist.extra;

public class ModelList {

    String name, size, date, icon;

    public ModelList(String name, String size, String date, String icon) {
        this.name = name;
        this.size = size;
        this.date = date;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getDate() {
        return date;
    }

    public String getIcon() {
        return icon;
    }

}
