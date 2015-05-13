package com.dex.i_erp.data;

public class TaskList {

    String TaskId, TaskName;

    public TaskList(String taskId, String taskName) {
        super();
        TaskId = taskId;
        TaskName = taskName;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

}
