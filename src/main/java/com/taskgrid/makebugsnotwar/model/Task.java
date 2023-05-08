package com.taskgrid.makebugsnotwar.model;

public class Task {
    private int taskId;
    private String taskName;
    private String taskStatus;
    private int taskTime;
    private String taskDescription;

    public Task(){} // default constructor

    public Task(int taskId, String taskName, String taskStatus, int taskTime, String taskDescription) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.taskTime = taskTime;
        this.taskDescription = taskDescription;
    }

    public int getTaskId() {
        return taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getTaskStatus(String taskStatus) {
        return taskStatus;
    }
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
    public int getTaskTime(){
        return taskTime;
    }
    public void setTaskTime(int taskTime){
        this.taskTime = taskTime;
    }
    public String getTaskDescription(){
        return taskDescription;
    }
    public void setTaskDescription(String taskDescription){
        this.taskDescription = taskDescription;
    }
}
