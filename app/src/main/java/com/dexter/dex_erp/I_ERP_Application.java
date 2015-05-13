package com.dexter.dex_erp;

import android.app.Application;

public class I_ERP_Application extends Application {

    String monthname;
    String yearno;
    boolean refresh_flag;

    public boolean isRefresh_flag() {
        return refresh_flag;
    }

    public void setRefresh_flag(boolean refresh_flag) {
        this.refresh_flag = refresh_flag;
    }

    public String getMonthname() {
        return monthname;
    }

    public void setMonthname(String monthname) {
        this.monthname = monthname;
    }

    public String getYearno() {
        return yearno;
    }

    public void setYearno(String yearno) {
        this.yearno = yearno;
    }

}
