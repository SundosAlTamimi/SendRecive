package com.example.sendrecive.Models;

public class ReciveDetail {
    private String ORDERNUMBER;
    private String VHFNO_DETAIL;
    private String VHFDATE_DETAIL;
    private String VSERIAL;
    private String ITEMOCODE;
    private String ORDER_QTY;
    private String ORDER_BONUS;
    private String RECEIVED_QTY;
    private String BONUS;
    private String PRICE;
    private String TOTAL;
    private String TAXDETAIL;
    private String INDATE;
    private String DISCL;
    private String EXPDATE;
    private  String ITEM_NAME;

    public String getITEM_NAME() {
        return ITEM_NAME;
    }

    public void setITEM_NAME(String ITEM_NAME) {
        this.ITEM_NAME = ITEM_NAME;
    }

    public ReciveDetail(String ORDERNUMBER, String VHFNO_DETAIL, String VHFDATE_DETAIL, String VSERIAL, String ITEMOCODE, String ORDER_QTY, String ORDER_BONUS,
                        String RECEIVED_QTY, String BONUS, String PRICE, String TOTAL, String TAXDETAIL, String INDATE, String DISCL, String EXPDATE) {
        this.ORDERNUMBER = ORDERNUMBER;
        this.VHFNO_DETAIL = VHFNO_DETAIL;
        this.VHFDATE_DETAIL = VHFDATE_DETAIL;
        this.VSERIAL = VSERIAL;
        this.ITEMOCODE = ITEMOCODE;
        this.ORDER_QTY = ORDER_QTY;
        this.ORDER_BONUS = ORDER_BONUS;
        this.RECEIVED_QTY = RECEIVED_QTY;
        this.BONUS = BONUS;
        this.PRICE = PRICE;
        this.TOTAL = TOTAL;
        this.TAXDETAIL = TAXDETAIL;
        this.INDATE = INDATE;
        this.DISCL = DISCL;
        this.EXPDATE = EXPDATE;
    }

    public ReciveDetail() {
    }

    public String getORDERNUMBER() {
        return ORDERNUMBER;
    }

    public void setORDERNUMBER(String ORDERNUMBER) {
        this.ORDERNUMBER = ORDERNUMBER;
    }

    public String getVHFNO_DETAIL() {
        return VHFNO_DETAIL;
    }

    public void setVHFNO_DETAIL(String VHFNO_DETAIL) {
        this.VHFNO_DETAIL = VHFNO_DETAIL;
    }

    public String getVHFDATE_DETAIL() {
        return VHFDATE_DETAIL;
    }

    public void setVHFDATE_DETAIL(String VHFDATE_DETAIL) {
        this.VHFDATE_DETAIL = VHFDATE_DETAIL;
    }

    public String getVSERIAL() {
        return VSERIAL;
    }

    public void setVSERIAL(String VSERIAL) {
        this.VSERIAL = VSERIAL;
    }

    public String getITEMOCODE() {
        return ITEMOCODE;
    }

    public void setITEMOCODE(String ITEMOCODE) {
        this.ITEMOCODE = ITEMOCODE;
    }

    public String getORDER_QTY() {
        return ORDER_QTY;
    }

    public void setORDER_QTY(String ORDER_QTY) {
        this.ORDER_QTY = ORDER_QTY;
    }

    public String getORDER_BONUS() {
        return ORDER_BONUS;
    }

    public void setORDER_BONUS(String ORDER_BONUS) {
        this.ORDER_BONUS = ORDER_BONUS;
    }

    public String getRECEIVED_QTY() {
        return RECEIVED_QTY;
    }

    public void setRECEIVED_QTY(String RECEIVED_QTY) {
        this.RECEIVED_QTY = RECEIVED_QTY;
    }

    public String getBONUS() {
        return BONUS;
    }

    public void setBONUS(String BONUS) {
        this.BONUS = BONUS;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(String TOTAL) {
        this.TOTAL = TOTAL;
    }

    public String getTAXDETAIL() {
        return TAXDETAIL;
    }

    public void setTAXDETAIL(String TAXDETAIL) {
        this.TAXDETAIL = TAXDETAIL;
    }

    public String getINDATE() {
        return INDATE;
    }

    public void setINDATE(String INDATE) {
        this.INDATE = INDATE;
    }

    public String getDISCL() {
        return DISCL;
    }

    public void setDISCL(String DISCL) {
        this.DISCL = DISCL;
    }

    public String getEXPDATE() {
        return EXPDATE;
    }

    public void setEXPDATE(String EXPDATE) {
        this.EXPDATE = EXPDATE;
    }
}
