package com.taskgrid.makebugsnotwar.repository;

import com.taskgrid.makebugsnotwar.model.Task;
import com.taskgrid.makebugsnotwar.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class TaskRepository {
    @Value("${spring.datasource.url}")
    private String DB_URL;
    @Value("${spring.datasource.username}")
    private String DB_UID;
    @Value("${spring.datasource.password}")
    private String DB_PWD;

    public Task findById(int id) {
        Task task = new Task();
        final String TASK_QUERY = "SELECT * FROM taskgrid.tasks WHERE task_id = ?";

        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(TASK_QUERY);
            preparedStatement.setInt(1,id);

            ResultSet resultSet= preparedStatement.executeQuery();
            resultSet.next();

            String name = resultSet.getString("task_name");
            String description = resultSet.getString("task_description");
            int taskStatus = resultSet.getInt("task_status");
            int userId = resultSet.getInt("user_id");
            int projectId = resultSet.getInt("project_id");
            int taskTime = resultSet.getInt("task_time");

            task = new Task(id, name, description, taskStatus, userId, projectId, taskTime);


        } catch(SQLException e){
            System.out.println("Could not retrieve the specified task from the database");
        }


        return task;
    }

}


