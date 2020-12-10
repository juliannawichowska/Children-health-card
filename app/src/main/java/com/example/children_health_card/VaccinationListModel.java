package com.example.children_health_card;

public class VaccinationListModel {

    String VaccinationType, VaccinationDate, VaccinationNote, VaccinationName, nameChild;

    public VaccinationListModel() {
    }

    public VaccinationListModel(String vaccinationType, String vaccinationDate, String vaccinationNote, String vaccinationName, String nameChild) {
        VaccinationType = vaccinationType;
        VaccinationDate = vaccinationDate;
        VaccinationNote = vaccinationNote;
        VaccinationName = vaccinationName;
        this.nameChild = nameChild;
    }

    public String getVaccinationType() {
        return VaccinationType;
    }

    public void setVaccinationType(String vaccinationType) {
        VaccinationType = vaccinationType;
    }

    public String getVaccinationDate() {
        return VaccinationDate;
    }

    public void setVaccinationDate(String vaccinationDate) {
        VaccinationDate = vaccinationDate;
    }

    public String getVaccinationNote() {
        return VaccinationNote;
    }

    public void setVaccinationNote(String vaccinationNote) {
        VaccinationNote = vaccinationNote;
    }

    public String getVaccinationName() {
        return VaccinationName;
    }

    public void setVaccinationName(String vaccinationName) {
        VaccinationName = vaccinationName;
    }

    public String getNameChild() {
        return nameChild;
    }

    public void setNameChild(String nameChild) {
        this.nameChild = nameChild;
    }
}
