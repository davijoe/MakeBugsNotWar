package com.taskgrid.makebugsnotwar.repository;

import com.taskgrid.makebugsnotwar.model.Board;
import com.taskgrid.makebugsnotwar.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class BoardRepository {
    @Value("${spring.datasource.url}")
    private String DB_URL;
    @Value("${spring.datasource.username}")
    private String DB_UID;
    @Value("${spring.datasource.password}")
    private String DB_PWD;

    public void createBoard(Board board){
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
}
