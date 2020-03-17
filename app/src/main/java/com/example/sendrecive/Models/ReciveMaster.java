package com.example.sendrecive.Models;

public class ReciveMaster {

    private String ORDERNO;
    private String VHFNO;
    private String VHFDATE;
    private String VENDOR_VHFNO;
    private String VENDOR_VHFDATE;
    private String SUBTOTAL;
    private double TAX;
    private String NETTOTAL;
    private String    IS_POSTED;
    private String TAXKIND;
    private double DISC;
    private String AccName;
    private String COUNTX;

    public ReciveMaster(String ORDERNO, String VHFNO, String VHFDATE, String VENDOR_VHFNO, String VENDOR_VHFDATE, String SUBTOTAL,
                        double TAX, String NETTOTAL, String IS_POSTED, String TAXKIND, double DISC, String accName, String COUNTX) {
        this.ORDERNO = ORDERNO;
        this.VHFNO = VHFNO;
        this.VHFDATE = VHFDATE;
        this.VENDOR_VHFNO = VENDOR_VHFNO;
        this.VENDOR_VHFDATE = VENDOR_VHFDATE;
        this.SUBTOTAL = SUBTOTAL;
        this.TAX = TAX;
        this.NETTOTAL = NETTOTAL;
        this.IS_POSTED = IS_POSTED;
        this.TAXKIND = TAXKIND;
        this.DISC = DISC;
        AccName = accName;
        this.COUNTX = COUNTX;
    }

    public ReciveMaster() {
    }

    public String getORDERNO() {
        return ORDERNO;
    }

    public void setORDERNO(String ORDERNO) {
        this.ORDERNO = ORDERNO;
    }

    public String getVHFNO() {
        return VHFNO;
    }

    public void setVHFNO(String VHFNO) {
        this.VHFNO = VHFNO;
    }

    public String getVHFDATE() {
        return VHFDATE;
    }

    public void setVHFDATE(String VHFDATE) {
        this.VHFDATE = VHFDATE;
    }

    public String getVENDOR_VHFNO() {
        return VENDOR_VHFNO;
    }

    public void setVENDOR_VHFNO(String VENDOR_VHFNO) {
        this.VENDOR_VHFNO = VENDOR_VHFNO;
    }

    public String getVENDOR_VHFDATE() {
        return VENDOR_VHFDATE;
    }

    public void setVENDOR_VHFDATE(String VENDOR_VHFDATE) {
        this.VENDOR_VHFDATE = VENDOR_VHFDATE;
    }

    public String getSUBTOTAL() {
        return SUBTOTAL;
    }

    public void setSUBTOTAL(String SUBTOTAL) {
        this.SUBTOTAL = SUBTOTAL;
    }

    public double getTAX() {
        return TAX;
    }

    public void setTAX(double TAX) {
        this.TAX = TAX;
    }

    public String getNETTOTAL() {
        return NETTOTAL;
    }

    public void setNETTOTAL(String NETTOTAL) {
        this.NETTOTAL = NETTOTAL;
    }

    public String getIS_POSTED() {
        return IS_POSTED;
    }

    public void setIS_POSTED(String IS_POSTED) {
        this.IS_POSTED = IS_POSTED;
    }

    public String getTAXKIND() {
        return TAXKIND;
    }

    public void setTAXKIND(String TAXKIND) {
        this.TAXKIND = TAXKIND;
    }

    public double getDISC() {
        return DISC;
    }

    public void setDISC(double DISC) {
        this.DISC = DISC;
    }

    public String getAccName() {
        return AccName;
    }

    public void setAccName(String accName) {
        AccName = accName;
    }

    public String getCOUNTX() {
        return COUNTX;
    }

    public void setCOUNTX(String COUNTX) {
        this.COUNTX = COUNTX;
    }
}
