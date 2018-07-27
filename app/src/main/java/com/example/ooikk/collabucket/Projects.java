package com.example.ooikk.collabucket;

public class Projects {

    private String Title;
    private String ProjectSummary;
    private String Owner;

    public Projects(){

    }

    public Projects(String title, String projectSummary, String owner){
        this.Title = title;
        this.ProjectSummary = projectSummary;
        this.Owner=owner;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getProjectSummary() {
        return ProjectSummary;
    }

    public void setProjectSummary(String projectSummary) {
        ProjectSummary = projectSummary;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }
}

