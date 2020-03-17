package com.example.sendrecive.Models;

import android.util.Log;

public class ItemInfo {

    private String DELIVERY_ItemName;
    private String Qty;
    private String Bonus;
    private String PriceL;
    private String NetTL;
    private String TaxLV;
    private String DiscLV;



    public ItemInfo() {
    }

    public String getDELIVERY_ItemName() {
        return DELIVERY_ItemName;
    }

    public void setDELIVERY_ItemName(String DELIVERY_ItemName) {
        this.DELIVERY_ItemName = DELIVERY_ItemName;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getBonus() {
        return Bonus;
    }

    public void setBonus(String bonus) {
        Bonus = bonus;
    }

    public Double getPriceL() {
       double price=0;
        try{
           price=Double.parseDouble(PriceL);
        }
        catch (NumberFormatException e)
        {
            price=0;
            Log.e("NumberFormatException",""+PriceL);
        }
        return price;
    }

    public void setPriceL(String priceL) {
        PriceL = priceL;
    }

    public String getNetTL() {
        return NetTL;
    }

    public void setNetTL(String netTL) {
        NetTL = netTL;
    }

    public String getTaxLV() {
        return TaxLV;
    }

    public void setTaxLV(String taxLV) {
        TaxLV = taxLV;
    }

    public String getDiscLV() {
        return DiscLV;
    }

    public void setDiscLV(String discLV) {
        DiscLV = discLV;
    }
}
