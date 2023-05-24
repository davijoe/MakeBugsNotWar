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
        final String ADD_TASK_QUERY = "INSERT INTO taskgrid.tasks"+
                "(task_name, task_status, task_time, task_description, board_id) VALUES (?,?,?,?,?)";

        final String LAST_INSERT_QUERY = "SELECT LAST_INSERT_ID()";

        int taskId = 0;

        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_TASK_QUERY);
            preparedStatement.setString(1, task.getTaskName());
            preparedStatement.setInt(2,task.getTaskStatus());
            preparedStatement.setInt(3, task.getTaskTime());
            preparedStatement.setString(4, task.getTaskDescription());
            preparedStatement.setInt(5,task.getBoardId());
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

    public List<Task> retrieveProjectTasks(int projectId){
        List<Task> taskList = new ArrayList<>();
        final String QUERY = "SELECT * FROM taskgrid.tasks WHERE project_id = ?";

        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
            preparedStatement.setInt(1, projectId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("task_id");
                String name = resultSet.getString("task_name");
                String description = resultSet.getString("task_description");
                int taskStatus = resultSet.getInt("task_status");
                int userId = resultSet.getInt("user_id");
                int taskTime = resultSet.getInt("task_time");

                Task task = new Task(id, name, description, taskStatus, userId, projectId, taskTime);

                taskList.add(task);
                System.out.println(task);

            }

        }catch(SQLException e){
            System.out.println("Could not retrieve tasks from the project");
            e.printStackTrace();
        }

        return taskList;
    }

    public void editTask(Task task) {
        final String EDITTASK_QUERY = "UPDATE taskgrid.tasks SET task_name = ?, task_description = ?, task_status = ?, user_id = ?, task_time = ? WHERE task_id = ?";
        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(EDITTASK_QUERY);
            preparedStatement.setString(1,task.getTaskName());
            preparedStatement.setString(2, task.getTaskDescription());
            preparedStatement.setInt(3, task.getTaskStatus());
            preparedStatement.setInt(4, task.getUserId());
            preparedStatement.setInt(5, task.getTaskTime());
            preparedStatement.setInt(6, task.getTaskId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Could not edit task");
            e.printStackTrace();
        }
    }

    public void updateTaskStatus(int taskId, int delta){
        final String UPDATE_QUERY = "UPDATE taskgrid.tasks SET task_status = task_status+? WHERE (task_id = ?)";

        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setInt(1, delta);
            preparedStatement.setInt(2, taskId);

            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println("could not update task status");
            e.printStackTrace();
        }
    }

    public void deleteTask(int taskId) {

        final String DELETETASK = "DELETE FROM taskgrid.tasks WHERE task_id = ?";

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL,DB_UID,DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETETASK);
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Could not delete task");
            e.printStackTrace();
        }
    }

    public List<Task> calculateProjectTaskInfo(int projectId) {
        List<Task> taskInfoList = new ArrayList<>();

        final String CALCTASKINFO_QUERY="SELECT IFNULL(task_status, -1), SUM(task_time) AS sum FROM tasks WHERE project_id = ? GROUP BY task_status WITH ROLLUP";
        try {
            Connection connection = ConnectionManager.getConnection(DB_URL,DB_UID,DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(CALCTASKINFO_QUERY);
            preparedStatement.setInt(1,projectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int taskStatus = resultSet.getInt(1);
                System.out.println("tasks incomming");
                System.out.println(taskStatus);
                int sumTaskTime = resultSet.getInt(2);
                Task task = new Task(taskStatus, sumTaskTime);
                taskInfoList.add(task);
            }


        } catch (SQLException e) {
            System.out.println("Could not get task info");
            e.printStackTrace();

        }

        return taskInfoList;
    }
}