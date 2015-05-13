package com.dexter.timesheetfill.adapter;

public class TimeSheetFillData {

    String TIMESHEET_ID = "";
    String TIMESHEET_PROJECTNAME = "";
    String TIMESHEET_TASKNAME = "";
    String TIMESHEET_SUBTASKNAME = "";
    String TIMESHEET_COMMENT = "";
    String TIMESHEET_HOURS = "";

    public TimeSheetFillData(String tIMESHEET_ID, String tIMESHEET_PROJECTNAME,
                             String tIMESHEET_TASKNAME, String tIMESHEET_SUBTASKNAME,
                             String tIMESHEET_COMMENT, String tIMESHEET_HOURS) {
        super();
        TIMESHEET_ID = tIMESHEET_ID;
        TIMESHEET_PROJECTNAME = tIMESHEET_PROJECTNAME;
        TIMESHEET_TASKNAME = tIMESHEET_TASKNAME;
        TIMESHEET_SUBTASKNAME = tIMESHEET_SUBTASKNAME;
        TIMESHEET_COMMENT = tIMESHEET_COMMENT;
        TIMESHEET_HOURS = tIMESHEET_HOURS;
    }

    public String getTIMESHEET_ID() {
        return TIMESHEET_ID;
    }

    public void setTIMESHEET_ID(String tIMESHEET_ID) {
        TIMESHEET_ID = tIMESHEET_ID;
    }

    public String getTIMESHEET_PROJECTNAME() {
        return TIMESHEET_PROJECTNAME;
    }

    public void setTIMESHEET_PROJECTNAME(String tIMESHEET_PROJECTNAME) {
        TIMESHEET_PROJECTNAME = tIMESHEET_PROJECTNAME;
    }

    public String getTIMESHEET_TASKNAME() {
        return TIMESHEET_TASKNAME;
    }

    public void setTIMESHEET_TASKNAME(String tIMESHEET_TASKNAME) {
        TIMESHEET_TASKNAME = tIMESHEET_TASKNAME;
    }

    public String getTIMESHEET_SUBTASKNAME() {
        return TIMESHEET_SUBTASKNAME;
    }

    public void setTIMESHEET_SUBTASKNAME(String tIMESHEET_SUBTASKNAME) {
        TIMESHEET_SUBTASKNAME = tIMESHEET_SUBTASKNAME;
    }

    public String getTIMESHEET_COMMENT() {
        return TIMESHEET_COMMENT;
    }

    public void setTIMESHEET_COMMENT(String tIMESHEET_COMMENT) {
        TIMESHEET_COMMENT = tIMESHEET_COMMENT;
    }

    public String getTIMESHEET_HOURS() {
        return TIMESHEET_HOURS;
    }

    public void setTIMESHEET_HOURS(String tIMESHEET_HOURS) {
        TIMESHEET_HOURS = tIMESHEET_HOURS;
    }

}
