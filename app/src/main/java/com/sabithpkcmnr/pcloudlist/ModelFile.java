package com.sabithpkcmnr.pcloudlist;

public class ModelFile {

    String name, size, date, icon;

    public ModelFile() {
    }

    public ModelFile(String name, String size, String date, String icon) {
        this.name = name;
        this.size = size;
        this.date = date;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
