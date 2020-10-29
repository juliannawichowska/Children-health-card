package com.example.children_health_card;

public class ChildListModel {

    String name, pesel, surname, imageURL;

    public ChildListModel() {
    }

    public ChildListModel(String name, String pesel, String surname, String imageURL) {
        this.name = name;
        this.pesel = pesel;
        this.surname = surname;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }
}
