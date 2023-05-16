package com.taskgrid.makebugsnotwar.repository;

import com.taskgrid.makebugsnotwar.model.Project;
import com.taskgrid.makebugsnotwar.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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

    public Project findProjectById(int projectId) {

        final String SELECT_PROJECT_QUERY = "SELECT * FROM taskgrid.projects WHERE project_id = ?";
        Project project = new Project();
        project.setProjectId(projectId);

        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROJECT_QUERY);
            preparedStatement.setInt(1,projectId);

           ResultSet resultSet = preparedStatement.executeQuery();

           resultSet.next();

           String name = resultSet.getString(2);
           String description = resultSet.getString(3);

           project.setProjectName(name);
           project.setProjectDescription(description);

        } catch (SQLException e){
            System.out.println("Could not retrieve the specified project from the database");
            e.printStackTrace();
        }

        return project;
    }

    public List getProjectsForUser(int userId) {
        final String PROJECTBYUSERID_QUERY = "SELECT * FROM projects JOIN user_project up on projects.project_id = " +
                "up.project_id JOIN users u on u.user_id = up.user_id WHERE u.user_id = ?";
        List<Project> projectList = new ArrayList<>();
        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(PROJECTBYUSERID_QUERY);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            while (resultSet.next()) {
                int projectId = resultSet.getInt(1);
                String projectName = resultSet.getString(2);
                String projectDescription = resultSet.getString(3);
                Project project = new Project(projectId, projectName, projectDescription);
                projectList.add(project);
            }
        } catch (SQLException e) {
            System.out.println("Could not get projects");
            e.printStackTrace();
        }

        return projectList;
    }

    public void updateProjectDetails(int projectId, String newName, String newDescription) {

           final String UPDATE_PROJECT_QUERY = "UPDATE taskgrid.projects "+
                   "SET name = ?, description = ? WHERE id = ?";

            try {
                Connection connection = ConnectionManager.getConnection(DB_UID, DB_UID, DB_PWD);
                PreparedStatement statement = connection.prepareStatement(UPDATE_PROJECT_QUERY);
                statement.setString(1, newName);
                statement.setString(2, newDescription);
                statement.setInt(3, projectId);

                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Could not update project");
            e.printStackTrace();
        }
    }
}
