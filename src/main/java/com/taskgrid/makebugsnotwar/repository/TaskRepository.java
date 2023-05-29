package com.taskgrid.makebugsnotwar.repository;

import com.taskgrid.makebugsnotwar.model.Task;
import com.taskgrid.makebugsnotwar.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {
    @Value("${spring.datasource.url}")
    private String DB_URL;
    @Value("${spring.datasource.username}")
    private String DB_UID;
    @Value("${spring.datasource.password}")
    private String DB_PWD;

    public int addTask(Task task) {
        final String ADD_TASK_QUERY = "INSERT INTO taskgrid.tasks" +
                "(task_name, task_status, story_points, task_description, board_id) VALUES (?,?,?,?,?)";

        final String LAST_INSERT_QUERY = "SELECT LAST_INSERT_ID()";

        int taskId = 0;

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_TASK_QUERY);
            preparedStatement.setString(1, task.getTaskName());
            preparedStatement.setInt(2, task.getTaskStatus());
            preparedStatement.setInt(3, task.getStoryPoints());
            preparedStatement.setString(4, task.getTaskDescription());
            preparedStatement.setInt(5, task.getBoardId());
            preparedStatement.executeUpdate();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(LAST_INSERT_QUERY);
            resultSet.next();
            taskId = resultSet.getInt(1);

        } catch (SQLException e) {
            System.out.println("Could not add task to database");
            e.printStackTrace();
        }

        return taskId;
    }


    public Task findById(int taskId) {
        Task task = new Task();
        final String TASK_QUERY = "SELECT * FROM taskgrid.tasks WHERE task_id = ?";

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(TASK_QUERY);
            preparedStatement.setInt(1, taskId);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            String taskName = resultSet.getString("task_name");
            String taskDescription = resultSet.getString("task_description");
            int taskStatus = resultSet.getInt("task_status");
            int userId = resultSet.getInt("user_id");
            int projectId = resultSet.getInt("board_id");
            int taskTime = resultSet.getInt("task_time");
            int storyPoints = resultSet.getInt("story_points");

            task = new Task(taskId, taskName, taskDescription, taskStatus, userId, projectId, taskTime, storyPoints);


        } catch (SQLException e) {
            System.out.println("Could not retrieve the specified task from the database");
        }


        return task;
    }

    public List<Task> retrieveProjectTasks(int projectId) {
        List<Task> taskList = new ArrayList<>();
        final String QUERY = "SELECT taskgrid.tasks.* " +
                "FROM tasks " +
                "JOIN boards ON tasks.board_id = boards.board_id " +
                "JOIN projects ON boards.project_id = projects.project_id " +
                "WHERE projects.project_id = ?";

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
            preparedStatement.setInt(1, projectId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int taskId = resultSet.getInt("task_id");
                String taskName = resultSet.getString("task_name");
                String taskDescription = resultSet.getString("task_description");
                int taskStatus = resultSet.getInt("task_status");
                int userId = resultSet.getInt("user_id");
                int taskTime = resultSet.getInt("task_time");
                int storyPoints = resultSet.getInt("story_points");
                int boardId = resultSet.getInt("board_id");

                Task task = new Task(taskId, taskName, taskDescription, taskStatus, userId, boardId, taskTime, storyPoints);

                taskList.add(task);

            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve tasks from the project");
            e.printStackTrace();
        }

        return taskList;
    }

    public void editTask(Task task) {
        final String EDITTASK_QUERY = "UPDATE taskgrid.tasks SET task_name = ?, task_description = ?, task_time = ?, story_points = ? WHERE task_id = ?";
        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(EDITTASK_QUERY);
            preparedStatement.setString(1, task.getTaskName());
            preparedStatement.setString(2, task.getTaskDescription());
            preparedStatement.setInt(3, task.getTaskTime());
            preparedStatement.setInt(4, task.getStoryPoints());
            preparedStatement.setInt(5, task.getTaskId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Could not edit task");
            e.printStackTrace();
        }
    }

    public void updateTaskStatus(int taskId, int delta) {
        final String UPDATE_QUERY = "UPDATE taskgrid.tasks SET task_status = task_status+? WHERE (task_id = ?)";

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setInt(1, delta);
            preparedStatement.setInt(2, taskId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("could not update task status");
            e.printStackTrace();
        }
    }

    public void deleteTask(int taskId) {

        final String DELETETASK_QUERY = "DELETE FROM taskgrid.tasks WHERE task_id = ?";

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETETASK_QUERY);
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Could not delete task");
            e.printStackTrace();
        }
    }

    public int getProjectId(int boardId) {
        int projectId = 0;
        final String GETBOARDID_QUERY = "SELECT project_id FROM taskgrid.boards WHERE board_id = ?";
        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(GETBOARDID_QUERY);
            preparedStatement.setInt(1, boardId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                projectId = resultSet.getInt(1);
            }


        } catch (SQLException e) {
            System.out.println("Could not get task info");
            e.printStackTrace();

        }

        return projectId;
    }
}