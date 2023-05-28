package com.taskgrid.makebugsnotwar.repository;

import com.taskgrid.makebugsnotwar.model.User;
import com.taskgrid.makebugsnotwar.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    @Value("${spring.datasource.url}")
    private String DB_URL;
    @Value("${spring.datasource.username}")
    private String DB_UID;
    @Value("${spring.datasource.password}")
    private String DB_PWD;

    public boolean checkUserEmail(String username, String email) {
        final String SIGNUP_QUERY = "SELECT * FROM taskgrid.users WHERE user_email = ? OR username = ?";
        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(SIGNUP_QUERY);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String u_email = resultSet.getString(3);
                String u_username = resultSet.getString(4);
                if(u_email != null || u_username != null){
                    return true;
                }
            }

        }catch(SQLException sqle){
            System.out.println("Could not query the database");
            sqle.printStackTrace();
        }
        return false;
    }

    public String encodePassword(String password)
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10); //The strength affects how long it takes to encrypt and decrypt the password. 10 takes about a second for us, so we will leave it there.
        return encoder.encode(password);
    }

    public int addUserLogin(User user) {
        final String ADDUSER1_QUERY = "INSERT INTO taskgrid.users " +
                "(username, user_password, user_email) VALUES (?, ?, ?)";
        final String LASTINSERTED_QUERY = "SELECT LAST_INSERT_ID()";

        int user_id = 0;
        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(ADDUSER1_QUERY);
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2,user.getPassword());
            preparedStatement.setString(3,user.getEmail());

            preparedStatement.executeUpdate();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(LASTINSERTED_QUERY);
            resultSet.next();
            user_id = resultSet.getInt(1);

        }catch(SQLException sqle){
            System.out.println("Could not create user login");
            sqle.printStackTrace();
        }
        return user_id;
    }

    public void addUserInfo(User user, int user_id) {
        final String ADDUSER2_QUERY = "INSERT INTO taskgrid.user_info" +
                "(user_id, first_name, last_name, job_title) VALUES (?, ?, ?, ?)";

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(ADDUSER2_QUERY);

            preparedStatement.setInt(1, user_id);
            preparedStatement.setString(2, user.getFirstname());
            preparedStatement.setString(3, user.getLastname());
            preparedStatement.setString(4, user.getJobtitle());

            preparedStatement.executeUpdate();
        }catch(SQLException sqle){
            System.out.println("Could not create userinfo");
            sqle.printStackTrace();
        }
    }


    public int checkLogin(String username, String password) {
        int user_id = 0;
        final String CHECKLOGIN_QUERY = "SELECT user_id, user_password FROM taskgrid.users WHERE username = ?";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        try {
            Connection connection = ConnectionManager.getConnection(DB_URL,DB_UID,DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(CHECKLOGIN_QUERY);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String storedPassword = resultSet.getString(2);
            if (encoder.matches(password,storedPassword)) {
                user_id = resultSet.getInt(1);
            }
        } catch (SQLException sqle) {
            System.out.println("Could not login");
            sqle.printStackTrace();
        }
        return user_id;
    }
    public List<User> getUserInfo(int user_id) {
        List<User> userList = new ArrayList<>();
        final String GETUSER_INFO = "SELECT username, user_email, first_name, last_name, job_title " +
                "FROM taskgrid.users JOIN taskgrid.user_info ON users.user_id = user_info.user_id\n" +
                "WHERE user_info.user_id = ?;";
        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(GETUSER_INFO);
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String username = resultSet.getString(1);
                System.out.println(username);
                String email = resultSet.getString(2);
                String firstname = resultSet.getString(3);
                String lastname = resultSet.getString(4);
                System.out.println(lastname);
                String job_title = resultSet.getString(5);
                User user = new User(username,email,firstname,lastname,job_title);
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Could not get user info");
            e.printStackTrace();
        }
        return userList;
    }

    public List<User> retrieveProjectUsers(int projectId){
        List<User> projectUsers = new ArrayList<>();
        final String PROJECT_USERS_QUERY = "SELECT * FROM taskgrid.users "+
        "JOIN taskgrid.user_info ON users.user_id = user_info.user_id "+
        "JOIN taskgrid.user_project ON users.user_id = user_project.user_id "+
        "WHERE user_project.project_id = ?";

        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(PROJECT_USERS_QUERY);
            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int userId = resultSet.getInt("user_id");
                String userName = resultSet.getString("username");
                String lastName = resultSet.getString("last_name");
                String firstName = resultSet.getString("first_name");
                String projectRole = resultSet.getString("user_position");
                String jobTitle = resultSet.getString("job_title");
                String email = resultSet.getString("user_email");

                User user = new User(firstName, lastName, userName, email, jobTitle);
                user.setUserId(userId);
                user.setProjectRole(projectRole);
                projectUsers.add(user);
            }

        }catch(SQLException e){
            System.out.println("Could not retrieve the users associated with this project");
            e.printStackTrace();
        }

        return projectUsers;
    }


    public void updateUser(User user, int user_id) {
        final String UPDATEPROFILE_QUERY = "UPDATE taskgrid.users, taskgrid.user_info SET username = ?, user_email = ?, " +
                "first_name = ?, last_name = ?, job_title = ? WHERE users.user_id = ? AND user_info.user_id = ?;";
        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATEPROFILE_QUERY);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getFirstname());
            preparedStatement.setString(4, user.getLastname());
            preparedStatement.setString(5, user.getJobtitle());
            preparedStatement.setInt(6, user_id);
            preparedStatement.setInt(7, user_id);

            preparedStatement.executeUpdate();
        }catch(SQLException sqle){
            System.out.println("Could not update user");
            sqle.printStackTrace();
        }
    }

    public List<User> searchUsers(String searchText) {
        System.out.println("test");
        searchText = "%" + searchText + "%";
        List<User> resultUsers = new ArrayList<>();
        final String SEARCH_QUERY = "SELECT *" +
                "FROM taskgrid.users " +
                "JOIN taskgrid.user_info ON users.user_id = user_info.user_id " +
                "WHERE users.username LIKE ?" +
                "OR users.user_email LIKE ?" +
                "OR user_info.first_name LIKE ?" +
                "OR user_info.last_name LIKE ?" +
                "OR user_info.job_title LIKE ?" +
                "ORDER BY last_name";
        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_QUERY);
            preparedStatement.setString(1, searchText);
            preparedStatement.setString(2, searchText);
            preparedStatement.setString(3, searchText);
            preparedStatement.setString(4, searchText);
            preparedStatement.setString(5, searchText);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                System.out.println(userId);
                String username = resultSet.getString("username");
                System.out.println(username);
                String email = resultSet.getString("user_email");
                System.out.println(email);
                String firstname = resultSet.getString("first_name");
                System.out.println(firstname);
                String lastname = resultSet.getString("last_name");
                System.out.println(lastname);
                String job_title = resultSet.getString("job_title");
                System.out.println(job_title);
                System.out.println("break");

                User user = new User(firstname, lastname, username, email, job_title);
                user.setUserId(userId);
                resultUsers.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Failed to search database for user(s)");
            e.printStackTrace();
        }
        return resultUsers;
    }

    public void deleteProfile(int user_id) {
        final String DELETE_PROFILE_QUERY = "DELETE FROM users WHERE users.user_id = ?";
        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PROFILE_QUERY);
            preparedStatement.setInt(1, user_id);
            preparedStatement.executeUpdate();

        }catch(SQLException sqle){
            System.out.println("Could not delete profile.");
            sqle.printStackTrace();
        }
    }

    public void removeFromProject(int userId, int projectId) {
        final String DELETE_QUERY = "DELETE FROM taskgrid.user_project "+
                                    "WHERE user_id = ? AND project_id = ?";
        try{
            Connection connection = ConnectionManager.getConnection(DB_URL, DB_UID, DB_PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, projectId);
            preparedStatement.executeUpdate();

        }catch(SQLException e){
            System.out.println("Could not unassign user.");
            e.printStackTrace();
        }
    }

}

