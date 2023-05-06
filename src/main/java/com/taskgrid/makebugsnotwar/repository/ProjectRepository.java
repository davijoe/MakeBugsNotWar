package com.taskgrid.makebugsnotwar.repository;

import com.taskgrid.makebugsnotwar.model.Project;
import com.taskgrid.makebugsnotwar.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;


@Repository
public class ProjectRepository {
    @Value("${spring.datasource.url}")
    private String DB_URL;
    @Value("${spring.datasource.username}")
    private String DB_UID;
    @Value("${spring.datasource.password}")
    private String DB_PWD;

    public int addProject(Project project) {
        final String ADD_PROJECT_QUERY = "INSERT INTO taskgrid.projects"+
                "(project_name, project_description) VALUES (?,?)";

        final String LAST_INSERT_QUERY = "SELECT LAST_INSERT_ID()";

        int projectId = 0;

        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
        PreparedStatement preparedStatement = connection.prepareStatement(ADD_PROJECT_QUERY);
        preparedStatement.setString(1, project.getProjectName());
        preparedStatement.setString(2,project.getProjectDescription());
        preparedStatement.executeUpdate();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(LAST_INSERT_QUERY);
            resultSet.next();
            projectId = resultSet.getInt(1);

        } catch (SQLException e){
            System.out.println("Could not add project to database");
            e.printStackTrace();
        }

        return projectId;
    }

    public void addProjectRole(int userId, int projectId, String position){
        final String ADD_USER_POSITION_QUERY = "INSERT INTO taskgrid.user_project"+
        "(user_id, project_id, user_position) VALUES (?,?,?)";

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER_POSITION_QUERY);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, projectId);
            preparedStatement.setString(3, position);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            System.out.println("Could not assign position to user");
            e.printStackTrace();
        }
    }

}
