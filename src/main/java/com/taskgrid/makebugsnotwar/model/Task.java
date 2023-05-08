package com.taskgrid.makebugsnotwar.model;

public class Task {
    private  int id;
    private String name;
    private String description;
    private int taskStatus;
    private int userId;
    private int projectId;
    private int taskTime;

    public Task() {

    }
    public Task(int id, String name, String description, int taskStatus, int userId, int projectId, int taskTime) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.userId = userId;
        this.projectId = projectId;
        this.taskTime = taskTime;
    }


    public String getName() {
        return name;
    }
}
