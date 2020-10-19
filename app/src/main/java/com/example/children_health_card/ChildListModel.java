package com.example.children_health_card;

public class ChildListModel {

    String name, pesel;

    public ChildListModel() {
    }

    public ChildListModel(String name, String pesel) {
        this.name = name;
        this.pesel = pesel;
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
