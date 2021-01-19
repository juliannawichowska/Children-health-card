package com.example.children_health_card;

public class ContactsModel {

    String doctorType, doctorAddress, doctorName, doctorNumber;

    public ContactsModel() {
    }

    public ContactsModel(String doctorType, String doctorAddress, String doctorName, String doctorNumber) {
        this.doctorType = doctorType;
        this.doctorAddress = doctorAddress;
        this.doctorName = doctorName;
        this.doctorNumber = doctorNumber;
    }

    public String getDoctorType() {
        return doctorType;
    }

    public void setDoctorType(String doctorType) {
        this.doctorType = doctorType;
    }

    public String getDoctorAddress() {
        return doctorAddress;
    }

    public void setDoctorAddress(String doctorAddress) {
        this.doctorAddress = doctorAddress;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorNumber() {
        return doctorNumber;
    }

    public void setDoctorNumber(String doctorNumber) {
        this.doctorNumber = doctorNumber;
    }
}
