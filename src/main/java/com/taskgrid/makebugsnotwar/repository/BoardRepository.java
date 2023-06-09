package com.taskgrid.makebugsnotwar.repository;

import com.taskgrid.makebugsnotwar.model.Board;
import com.taskgrid.makebugsnotwar.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BoardRepository {
    @Value("${spring.datasource.url}")
    private String DB_URL;
    @Value("${spring.datasource.username}")
    private String DB_UID;
    @Value("${spring.datasource.password}")
    private String DB_PWD;

    public void createBoard(Board board){ //Creates the initial board when a new project is created
        final String ADD_BOARD_ONE_QUERY = "INSERT INTO taskgrid.boards"+
                "(project_id, board_name, start_date, end_date) VALUES (?,?,?,?)";
        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_BOARD_ONE_QUERY);
            preparedStatement.setInt(1, board.getProjectId());
            preparedStatement.setString(2, board.getBoardName());
            preparedStatement.setDate(3, (Date) board.getStartDate());
            preparedStatement.setDate(4, (Date) board.getEndDate());
            preparedStatement.executeUpdate();

        } catch (SQLException e){
            System.out.println("Could not add project to database");
            e.printStackTrace();
        }
    }

    public List<Board> getProjectBoards(int projectId){
        final String BOARD_LIST_QUERY = "SELECT * FROM taskgrid.boards WHERE project_id = ?";
        List<Board> boards = new ArrayList<>();

        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(BOARD_LIST_QUERY);
            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int boardId = resultSet.getInt("board_id");
                String boardName = resultSet.getString("board_name");
                Date startDate = resultSet.getDate("start_date");
                Date endDate = resultSet.getDate("end_date");
                Board newBoard = new Board(boardId, projectId, boardName, startDate, endDate);
                boards.add(newBoard);
            }


        }catch(SQLException e){
            System.out.println("Could not retrieve any boards for this project");
            e.printStackTrace();
        }

        return boards;

    }

    public List<Board> getProjectBoardsWithInfo(int projectId) { //Gets the record data for each board and sums up the story-points, task_time and number of tasks for each board
        final String BOARD_LIST_QUERY = "SELECT board_id, board_name, start_date, end_date, SUM(story_points), SUM(task_time)," +
                " COUNT(task_id) AS total_tasks FROM taskgrid.boards JOIN taskgrid.tasks USING (board_id) WHERE project_id = ? GROUP BY board_id";
        List<Board> boards = new ArrayList<>();

        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(BOARD_LIST_QUERY);
            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int boardId = resultSet.getInt(1);
                String boardName = resultSet.getString(2);
                Date startDate = resultSet.getDate(3);
                Date endDate = resultSet.getDate(4);
                int storyPoints = resultSet.getInt(5);
                int taskTime = resultSet.getInt(6);
                int totalTasks = resultSet.getInt(7);
                Board newBoard = new Board(boardId, boardName, startDate, endDate, storyPoints, taskTime, totalTasks);
                boards.add(newBoard);
            }


        }catch(SQLException e){
            System.out.println("Could not retrieve any boards for this project");
            e.printStackTrace();
        }

        return boards;

    }

    public void deleteBoard(int boardId) {
        final String DELETEBOARD_QUERY = "DELETE FROM taskgrid.boards WHERE board_id = ?";

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL,DB_UID,DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETEBOARD_QUERY);
            preparedStatement.setInt(1,boardId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not delete board");
        }
    }
}
