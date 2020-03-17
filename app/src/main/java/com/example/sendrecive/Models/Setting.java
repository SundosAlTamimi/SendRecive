package com.example.sendrecive.Models;

import android.graphics.Bitmap;

public class Setting {
    private String IP ;
    private String COMPANEY_NAME;
    private Bitmap LOGO ;
    private String TEL ;
    private String  SAME_QUANTITY;

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getCOMPANEY_NAME() {
        return COMPANEY_NAME;
    }

    public void setCOMPANEY_NAME(String COMPANEY_NAME) {
        this.COMPANEY_NAME = COMPANEY_NAME;
    }

    public Bitmap getLOGO() {
        return LOGO;
    }

    public void setLOGO(Bitmap LOGO) {
        this.LOGO = LOGO;
    }

    public String getTEL() {
        return TEL;
    }

    public void setTEL(String TEL) {
        this.TEL = TEL;
    }

    public String getSAME_QUANTITY() {
        return SAME_QUANTITY;
    }

    public void setSAME_QUANTITY(String SAME_QUANTITY) {
        this.SAME_QUANTITY = SAME_QUANTITY;
    }

    public Setting() {
    }
}
