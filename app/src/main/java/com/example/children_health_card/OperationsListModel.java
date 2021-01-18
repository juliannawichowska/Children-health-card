package com.example.children_health_card;

public class OperationsListModel {

    String date, doctor, note, nameChild;

    public OperationsListModel(String date, String doctor, String note, String nameChild) {
        this.date = date;
        this.doctor = doctor;
        this.note = note;
        this.nameChild = nameChild;
    }

    public OperationsListModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNameChild() {
        return nameChild;
    }

    public void setNameChild(String nameChild) {
        this.nameChild = nameChild;
    }
}
