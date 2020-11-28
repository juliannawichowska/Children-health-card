package com.example.children_health_card;

public class MedicinesListModel {

    String medicineName, dosage, reason, nameChild, note;

    public MedicinesListModel(String medicineName, String dosage, String reason, String nameChild, String note) {
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.reason = reason;
        this.nameChild = nameChild;
        this.note = note;
    }

    public MedicinesListModel() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNameChild() {
        return nameChild;
    }

    public void setNameChild(String nameChild) {
        this.nameChild = nameChild;
    }
}
