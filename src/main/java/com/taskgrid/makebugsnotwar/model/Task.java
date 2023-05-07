package com.taskgrid.makebugsnotwar.model;

public class Task {
    private int taskId;
    private String taskName;
    private int taskTime;
    private String taskDescription;

    public Task(){} // default constructor

    public Task(int taskId, String taskName, String taskDescription) {
        this.taskId = taskId;
        this.taskName = taskName;
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
