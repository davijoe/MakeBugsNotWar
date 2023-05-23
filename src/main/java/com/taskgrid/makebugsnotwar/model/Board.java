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
    public Board(){
    }

    public Board(int boardId, int projectId, String boardName, Date startDate, Date endDate){
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

}
