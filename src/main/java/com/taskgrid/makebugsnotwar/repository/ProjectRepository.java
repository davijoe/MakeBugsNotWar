package com.taskgrid.makebugsnotwar.repository;

import com.taskgrid.makebugsnotwar.model.Project;
import com.taskgrid.makebugsnotwar.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;




@Repository
public class ProjectRepository {
    @Value("${spring.datasource.url}")
    private String DB_URL;
    @Value("${spring.datasource.username}")
    private String DB_UID;
    @Value("${spring.datasource.password}")
    private String DB_PWD;

    public void addProject(Project project) {
        final String ADD_PROJECT__QUERY = "INSERT INTO taskgrid.projects"+
                "(project_name, project_description) VALUES (?,?)";
        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
        PreparedStatement preparedStatement = connection.prepareStatement(ADD_PROJECT__QUERY);
        preparedStatement.setString(1, project.getProjectName());
        preparedStatement.setString(2,project.getProjectDescription());
        preparedStatement.executeUpdate();

        } catch (SQLException e){
            System.out.println("Could not add project to database");
            e.printStackTrace();
        }

    }


}
