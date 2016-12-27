package com.humanplus.test;

/**
 * Created by smartear on 2016. 12. 27..
 */

public class PhoneBook {
    public String Name, Digit;

    public PhoneBook() {
        this.Name = "";
        this.Digit = "";
    }

    public String getName() {
        return Name;
    }

    public String getDigit() {
        return Digit;
    }

    public void setDigit(String digit) {
        this.Digit = digit;
    }

    public void setName(String name) {
        this.Name = name;
    }
}