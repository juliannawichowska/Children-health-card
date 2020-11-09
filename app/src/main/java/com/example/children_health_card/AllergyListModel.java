package com.example.children_health_card;

public class AllergyListModel {

    String allergens, symptoms, note, nameChild;

    public AllergyListModel() {

    }

    public AllergyListModel(String allergens, String symptoms, String note, String nameChild) {
        this.allergens = allergens;
        this.symptoms = symptoms;
        this.note = note;
        this.nameChild = nameChild;
    }

    public String getNameChild() {
        return nameChild;
    }

    public void setName(String nameChild) {
        this.nameChild = nameChild;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
