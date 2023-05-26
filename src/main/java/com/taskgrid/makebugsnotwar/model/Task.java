package com.taskgrid.makebugsnotwar.model;

public class Task {

    private int userId;
    private int boardId;
    private int taskId;
    private String taskName;
    private int taskStatus;
    private int taskTime;
    private String taskDescription;

    private int storyPoints;

    public Task(){} // default constructor

    public Task(int taskStatus, int taskTime) {
        this.taskStatus = taskStatus;
        this.taskTime = taskTime;
    }

    public Task(int taskId, String taskName, int taskStatus, int taskTime, String taskDescription) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.taskTime = taskTime;
        this.taskDescription = taskDescription;
    }
    public Task(int id, String name, String description, int taskStatus, int userId, int boardId, int taskTime, int storyPoints) {

        this.taskId = id;
        this.taskName = name;
        this.taskDescription = description;
        this.taskStatus = taskStatus;
        this.userId = userId;
        this.boardId = boardId;
        this.taskTime = taskTime;
        this.storyPoints = storyPoints;
    }
    public Task(int id, String name, int boardId, int taskTime, int storyPoints, String description) {

        this.taskId = id;
        this.taskName = name;
        this.taskDescription = description;
        this.userId = userId;
        this.boardId = boardId;
        this.taskTime = taskTime;
        this.storyPoints = storyPoints;
    }
    public Task(int id, String name, String description, int taskStatus, int userId, int boardId, int storyPoints) {

        this.taskId = id;
        this.taskName = name;
        this.taskDescription = description;
        this.taskStatus = taskStatus;
        this.userId = userId;
        this.boardId = boardId;
        this.storyPoints = storyPoints;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
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
    public int getTaskStatus() {
        return taskStatus;
    }
    public void setTaskStatus(int taskStatus) {
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
    public int getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(int storyPoints) {
        this.storyPoints = storyPoints;
    }


//
//    @Override
//    public String toString() {
//        return "Task{" +
//                "id=" + taskId +
//                ", name='" + taskName + '\'' +
//                ", description='" + taskDescription + '\'' +
//                ", taskStatus=" + taskStatus +
//                ", userId=" + userId +
//                ", projectId=" + boardId +
//                ", taskTime=" + taskTime +
//                '}';
//    }

}
