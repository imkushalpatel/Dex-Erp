package com.dex.i_erp.data;

public class SubTaskList {
    String SubTaskId, SubTaskName;


    public SubTaskList(String subTaskId, String subTaskName) {
        super();
        SubTaskId = subTaskId;
        SubTaskName = subTaskName;
    }

    public String getSubTaskId() {
        return SubTaskId;
    }

    public void setSubTaskId(String subTaskId) {
        SubTaskId = subTaskId;
    }

    public String getSubTaskName() {
        return SubTaskName;
    }

    public void setSubTaskName(String subTaskName) {
        SubTaskName = subTaskName;
    }


}
