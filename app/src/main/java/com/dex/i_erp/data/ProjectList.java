package com.dex.i_erp.data;

public class ProjectList {

    String ProjectId, ProjectName;

    public ProjectList(String projectId, String projectName) {
        super();
        ProjectId = projectId;
        ProjectName = projectName;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

}
