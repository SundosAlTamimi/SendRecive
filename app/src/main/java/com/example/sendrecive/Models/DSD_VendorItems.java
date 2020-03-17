package com.example.sendrecive.Models;

public class DSD_VendorItems {
    private String    AccCode;
    private String        ItemOCode;
    private String      ItemNameA;
    private String          PRICE;
    private String       TAXPERC;

    public DSD_VendorItems() {
    }

    public String getAccCode() {
        return AccCode;
    }

    public void setAccCode(String accCode) {
        AccCode = accCode;
    }

    public String getItemOCode() {
        return ItemOCode;
    }

    public void setItemOCode(String itemOCode) {
        ItemOCode = itemOCode;
    }

    public String getItemNameA() {
        return ItemNameA;
    }

    public void setItemNameA(String itemNameA) {
        ItemNameA = itemNameA;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getTAXPERC() {
        return TAXPERC;
    }

    public void setTAXPERC(String TAXPERC) {
        this.TAXPERC = TAXPERC;
    }
}
