package com.example.children_health_card;

public class VisitsListModel {

    String date, name, nameChild, note, type;

    public VisitsListModel() {
    }

    public VisitsListModel(String date, String name, String nameChild, String note, String type) {
        this.date = date;
        this.name = name;
        this.nameChild = nameChild;
        this.note = note;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameChild() {
        return nameChild;
    }

    public void setNameChild(String nameChild) {
        this.nameChild = nameChild;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
