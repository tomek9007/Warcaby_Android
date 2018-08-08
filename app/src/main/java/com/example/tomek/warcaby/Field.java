package com.example.tomek.warcaby;

public class Field {
    public boolean isFree;
    public boolean isBrown;
    public int soldierNumber;

    public Field(boolean isBrown) {
        this.isBrown = isBrown;
        this.soldierNumber = 0;
        this.isFree = true;
    }

    public Field(int soldierNumber) {
        this.isBrown = true;
        this.soldierNumber = soldierNumber;
        this.isFree = false;
    }

    public Field(boolean isFree, boolean isBrown, int soldierNumber) {
        this.isFree = isFree;
        this.isBrown = isBrown;
        this.soldierNumber = soldierNumber;
    }

    /*public Field(boolean isFree, boolean isBrown, int soldierNumber) {
        this.isFree = isFree;
        this.isBrown= isBrown;
        this.soldierNumber=soldierNumber;
    }*/
}
