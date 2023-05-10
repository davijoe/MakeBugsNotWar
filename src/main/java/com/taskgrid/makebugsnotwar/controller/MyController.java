package com.taskgrid.makebugsnotwar.controller;


import com.taskgrid.makebugsnotwar.model.Project;
import com.taskgrid.makebugsnotwar.model.Task;
import com.taskgrid.makebugsnotwar.model.User;
import com.taskgrid.makebugsnotwar.repository.ProjectRepository;
import com.taskgrid.makebugsnotwar.repository.TaskRepository;
import com.taskgrid.makebugsnotwar.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MyController {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public MyController(UserRepository userRepository, ProjectRepository projectRepository, TaskRepository taskRepository){
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/")
    public String landingSite(){
        return "index";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }
    @PostMapping("/signup")
    public String signupData(@RequestParam("u_firstname") String firstname,
                         @RequestParam("u_lastname") String lastname,
                         @RequestParam("u_username") String username,
                         @RequestParam("u_password") String password,
                         @RequestParam("u_passwordRepeat") String passwordRepeat,
                         @RequestParam("u_email") String email,
                         @RequestParam("u_jobtitle") String jobtitle,
                         Model model){
        boolean testUsername = false;
        boolean testPassword = false;
        String username_output = "";

        while(!testUsername){
            char[] username_chars = username.toCharArray();
            if(username.length() < 20){
                for(char aChar : username_chars ) {
                    if(Character.isLetter(aChar) || Character.isDigit(aChar)){
                        username_output = username_output.concat(Character.toString(aChar));
                        if(username_output.length() == username.length()){
                            testUsername = true;
                        }
                    }
                    else{
                        model.addAttribute("errorMessageUserName", "Illegal characters in username");
                        return "signup";
                    }
                }
            }
        }
        while(!testPassword){
            if(password.length() < 345 && password.length() > 16){
              testPassword = true;
            }
            else {
                model.addAttribute("errorMessagePassword", "Password is not within the length limit");
                return "signup";
            }
        }

        if(!password.equals(passwordRepeat)){
            model.addAttribute("errorMessagePasswordRepeat", "Password is not the same");
        }
        if(!userRepository.checkUserEmail(username, email)){
            User user = new User(firstname, lastname, username_output, password, email, jobtitle);
            int user_id = userRepository.addUserLogin(user);
            userRepository.addUserInfo(user, user_id);
        }
        else{
            model.addAttribute("errorMessageEmailUsername", "Email or username already in use");
            return "signup";
        }
        return "redirect:/";
    }


    @PostMapping("/login")
    public String enterLoginInfo(@RequestParam("u_username") String username,
                                 @RequestParam("u_password") String password,
                                 HttpSession session, Model model) {
        int user_id = userRepository.checkLogin(username, password);
        if (user_id!=0) {
            session.setAttribute("user_id", user_id);
            return "redirect:/profilePage";
        }
        else {
            model.addAttribute("loginErrorMessage",
                    "Could not login. Username does not exist or password was incorrect.");
            return "login";
        }
    }
    @GetMapping("/profilePage")
    public String profilePage(HttpSession session, Model model) {
        if (session.getAttribute("user_id") == null) {
            return "redirect:/login";
        }
        int user_id = (int) session.getAttribute("user_id");
        model.addAttribute("user_id", session.getAttribute("user_id"));
        model.addAttribute("users", userRepository.getUserInfo(user_id));
        return "profilePage";
    }
    @GetMapping("/updateProfile/{user_id}")
    public String updateProfilePage(@PathVariable("user_id") int findUser, Model model, HttpSession session){
        int user_id = (int) session.getAttribute("user_id");
        model.addAttribute("user_id", session.getAttribute("user_id"));
        model.addAttribute("user", userRepository.getUserInfo(user_id));
        return "updateProfile";
    }
    @PostMapping("/updateProfile")
    public String updateProfilePost(@RequestParam("user_id") int user_id,
                                    @RequestParam("username") String username,
                                    @RequestParam("email") String email,
                                    @RequestParam("firstname") String firstname,
                                    @RequestParam("lastname") String lastname,
                                    @RequestParam("jobtitle") String jobtitle){
        User user = new User(username, email, firstname, lastname, jobtitle);
        userRepository.updateUser(user, user_id);
        return "redirect:/profilePage";
    }

    @GetMapping("/create-project")
    public String showCreateProject(){
        return "create-project";
    }

    @PostMapping("/create-project")
    public String createProject(@RequestParam("project-name") String name,
                                @RequestParam("project-description") String description,
                                HttpSession session){

        if (session.getAttribute("user_id") != null) {
            int userId = (int) session.getAttribute("user_id");
            Project project = new Project();
            project.setProjectName(name);
            project.setProjectDescription(description);
            int projectId = projectRepository.addProject(project);

            projectRepository.addProjectRole(userId, projectId, "project creator");

            return "redirect:/profilePage";
        } else {
            return "redirect:/login";
        }

    }

    @GetMapping("/create-task")
    public String showCreateTask(){
        return "create-task";
    }

    @PostMapping("/create-task")
    public String createTask(@RequestParam("task-name") String name,
                             @RequestParam("task-status") String status,
                             @RequestParam("task-time") int time,
                             @RequestParam("task-description") String description,
                             HttpSession session){

        if (session.getAttribute("user_id") != null) {
            int userId = (int) session.getAttribute("user_id");
            Task task = new Task();
            task.setTaskName(name);
            task.setTaskStatus(status);
            task.setTaskTime(time);
            task.setTaskDescription(description);
            int taskId = taskRepository.addTask(task);

            return "redirect:/profilePage";
        } else {
            return "redirect:/login";
        }

    }


}
