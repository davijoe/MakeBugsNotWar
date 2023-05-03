package com.taskgrid.makebugsnotwar.controller;

import com.taskgrid.makebugsnotwar.model.Project;
import com.taskgrid.makebugsnotwar.model.User;
import com.taskgrid.makebugsnotwar.repository.ProjectRepository;
import com.taskgrid.makebugsnotwar.repository.TaskRepository;
import com.taskgrid.makebugsnotwar.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/create-project")
    public String showCreateProject(){
        return "create-project";
    }

    @PostMapping("/create-project")
    public String createProject(@RequestParam("project-name") String name,
                                @RequestParam("project-description") String description){
        Project project = new Project();
        project.setProjectName(name);
        project.setProjectDescription(description);
        projectRepository.addProject(project);

        return "redirect:/";
    }

}
