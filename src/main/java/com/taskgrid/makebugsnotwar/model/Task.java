package com.taskgrid.makebugsnotwar.model;

public class Task {
    private int userId;
    private int projectId;
    private int taskId;
    private String taskName;
    private int taskStatus;
    private int taskTime;
    private String taskDescription;

    public Task(){} // default constructor

    public Task(int taskId, String taskName, int taskStatus, int taskTime, String taskDescription) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.taskTime = taskTime;
        this.taskDescription = taskDescription;
    }

    public Task(int id, String name, String description, int taskStatus, int userId, int projectId, int taskTime) {

        this.taskId = id;
        this.taskName = name;
        this.taskDescription = description;
        this.taskStatus = taskStatus;
        this.userId = userId;
        this.projectId = projectId;
        this.taskTime = taskTime;
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


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                ", userId=" + userId +
                ", projectId=" + projectId +
                ", taskTime=" + taskTime +
                '}';
    }
}
