package com.taskgrid.makebugsnotwar.repository;

import com.taskgrid.makebugsnotwar.model.Task;
import com.taskgrid.makebugsnotwar.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class TaskRepository {
    @Value("${spring.datasource.url}")
    private String DB_URL;
    @Value("${spring.datasource.username}")
    private String DB_UID;
    @Value("${spring.datasource.password}")
    private String DB_PWD;

    public int addTask(Task task) {
        final String ADD_TASK_QUERY = "INSERT INTO taskgrid.tasks"+
                "(task_name, task_time, task_description) VALUES (?,?,?)";

        final String LAST_INSERT_QUERY = "SELECT LAST_INSERT_ID()";

        int taskId = 0;

        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
        PreparedStatement preparedStatement = connection.prepareStatement(ADD_TASK_QUERY);
        preparedStatement.setString(1, task.getTaskName());
        preparedStatement.setInt(2, task.getTaskTime());
        preparedStatement.setString(3, task.getTaskDescription());
        preparedStatement.executeUpdate();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(LAST_INSERT_QUERY);
            resultSet.next();
            taskId = resultSet.getInt(1);

        } catch (SQLException e){
            System.out.println("Could not add task to database");
            e.printStackTrace();
        }

        return taskId;
    }
}
