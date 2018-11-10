package com.google.domain;

import com.google.gson.annotations.SerializedName;

public class Phone {

    private String number;

    private String prefix;

    @SerializedName("national_name")
    private String nationalNumber;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNationalNumber() {
        return nationalNumber;
    }

    public void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
    }
}

