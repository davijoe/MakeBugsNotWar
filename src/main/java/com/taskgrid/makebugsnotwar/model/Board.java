package com.taskgrid.makebugsnotwar.model;

public class Board {
    private int boardId;
    private int projectId;
    private String boardName;
    private String startDate;
    private String endDate;

    public Board(int boardId, int projectId, String boardName, String startDate, String endDate){
        this.boardId = boardId;
        this.projectId = projectId;
        this.boardName = boardName;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
