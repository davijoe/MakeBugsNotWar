package com.taskgrid.makebugsnotwar.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

public class Board {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private int boardId;
    private int projectId;
    private String boardName;
    private Date startDate;
    private Date endDate;

    private int storyPointsPerBoard;

    private int taskTimePerBoard;

    private int totalTasksPerBoard;


    private double averageTimeBoard;

    private double averageStoryPointsBoard;
    public Board(){
    }

    public Board(int boardId, int projectId, String boardName, Date startDate, Date endDate){
        this.boardId = boardId;
        this.projectId = projectId;
        this.boardName = boardName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Board(int boardId, String boardName, Date startDate, Date endDate, int storyPointsPerBoard, int taskTimePerBoard, int
                 totalTasksPerBoard, double averageTimeBoard, double averageStoryPointsBoard) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.storyPointsPerBoard = storyPointsPerBoard;
        this.taskTimePerBoard = taskTimePerBoard;
        this.totalTasksPerBoard = totalTasksPerBoard;
        this.averageTimeBoard = averageTimeBoard;
        this.averageStoryPointsBoard = averageStoryPointsBoard;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getStoryPointsPerBoard() {
        return storyPointsPerBoard;
    }

    public void setStoryPointsPerBoard(int storyPointsPerBoard) {
        this.storyPointsPerBoard = storyPointsPerBoard;
    }

    public int getTaskTimePerBoard() {
        return taskTimePerBoard;
    }

    public void setTaskTimePerBoard(int taskTimePerBoard) {
        this.taskTimePerBoard = taskTimePerBoard;
    }

    public int getTotalTasksPerBoard() {
        return totalTasksPerBoard;
    }

    public void setTotalTasksPerBoard(int totalTasksPerBoard) {
        this.totalTasksPerBoard = totalTasksPerBoard;
    }
    public double getAverageTimeBoard() {
        return averageTimeBoard;
    }

    public void setAverageTimeBoard(double averageTimeBoard) {
        this.averageTimeBoard = averageTimeBoard;
    }

    public double getAverageStoryPointsBoard() {
        return averageStoryPointsBoard;
    }

    public void setAverageStoryPointsBoard(double averageStoryPointsBoard) {
        this.averageStoryPointsBoard = averageStoryPointsBoard;
    }

}
