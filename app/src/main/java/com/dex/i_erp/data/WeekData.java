package com.dex.i_erp.data;

public class WeekData {
    private String name;
    private String hours;

    public WeekData(String name, String hours) {
        super();
        this.name = name;
        this.hours = hours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }


}