package com.example.android.wir_tecrepo;

public class LockerDataModel {
    private int number;

    public LockerDataModel(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public String getNumberString() {
        return Integer.toString(this.number);
    }

}
