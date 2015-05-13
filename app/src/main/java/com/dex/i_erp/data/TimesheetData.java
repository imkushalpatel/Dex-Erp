package com.dex.i_erp.data;

public class TimesheetData {
    public String keyrowid;
    public String projectid;
    public String projectname;
    public String taskid;
    public String taskname;
    public String subtaskid;
    public String subtaskname;
    public String comments;
    public String hours;
    public String timesheetweek;
    public String timesheetmonth;
    public String datetime;

    public TimesheetData(String keyrowid, String projectid, String projectname,
                         String taskid, String taskname, String subtaskid,
                         String subtaskname, String comments, String hours,
                         String timesheetweek, String timesheetmonth, String datetime) {
        super();
        this.keyrowid = keyrowid;
        this.projectid = projectid;
        this.projectname = projectname;
        this.taskid = taskid;
        this.taskname = taskname;
        this.subtaskid = subtaskid;
        this.subtaskname = subtaskname;
        this.comments = comments;
        this.hours = hours;
        this.timesheetweek = timesheetweek;
        this.timesheetmonth = timesheetmonth;
        this.datetime = datetime;
    }

    public String getKeyrowid() {
        return keyrowid;
    }

    public void setKeyrowid(String keyrowid) {
        this.keyrowid = keyrowid;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getSubtaskid() {
        return subtaskid;
    }

    public void setSubtaskid(String subtaskid) {
        this.subtaskid = subtaskid;
    }

    public String getSubtaskname() {
        return subtaskname;
    }

    public void setSubtaskname(String subtaskname) {
        this.subtaskname = subtaskname;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getTimesheetweek() {
        return timesheetweek;
    }

    public void setTimesheetweek(String timesheetweek) {
        this.timesheetweek = timesheetweek;
    }

    public String getTimesheetmonth() {
        return timesheetmonth;
    }

    public void setTimesheetmonth(String timesheetmonth) {
        this.timesheetmonth = timesheetmonth;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

}
