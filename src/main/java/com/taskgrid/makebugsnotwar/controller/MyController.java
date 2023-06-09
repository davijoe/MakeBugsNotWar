package com.taskgrid.makebugsnotwar.controller;


import com.taskgrid.makebugsnotwar.model.Board;
import com.taskgrid.makebugsnotwar.model.Project;
import com.taskgrid.makebugsnotwar.model.Task;
import com.taskgrid.makebugsnotwar.model.User;
import com.taskgrid.makebugsnotwar.repository.BoardRepository;
import com.taskgrid.makebugsnotwar.repository.ProjectRepository;
import com.taskgrid.makebugsnotwar.repository.TaskRepository;
import com.taskgrid.makebugsnotwar.repository.UserRepository;
import com.taskgrid.makebugsnotwar.service.ControllerServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.List;

@Controller
public class MyController {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ControllerServices controllerServices;
    private final BoardRepository boardRepository;

    public MyController(UserRepository userRepository, ProjectRepository projectRepository,
                        TaskRepository taskRepository, BoardRepository boardRepository,
                        ControllerServices controllerServices){
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.boardRepository = boardRepository;
        this.controllerServices = controllerServices;
    }

    @GetMapping("/")
    public String login(){
        return "login";
    }
    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }
    @PostMapping("/signup")  //Postmapping for signup that also checks if the inputs are correct given parameters defined by us.
    public String signupData(@RequestParam("user-first-name") String firstName,
                         @RequestParam("user-last-name") String lastName,
                         @RequestParam("user-username") String username,
                         @RequestParam("user-password") String password,
                         @RequestParam("user-password-repeat") String passwordRepeat,
                         @RequestParam("user-email") String email,
                         @RequestParam("user-job-title") String jobTitle,
                         Model model){

        String testedUsername;
        String testedPassword;
        String testedEmail;

        String checkedFirstName = controllerServices.checkFirstName(firstName);
        String checkedLastName = controllerServices.checkLastName(lastName);

        if(controllerServices.checkUsername(username)){
            testedUsername = username;
        }
        else{
            model.addAttribute("errorMessageUserName", "Illegal characters in username");
            return "signup";
        }
        if(controllerServices.checkPassword(password)){
            testedPassword = userRepository.encodePassword(password);
        }
        else{
            model.addAttribute("errorMessagePassword", "Password is not within the length limit");
            return "signup";
        }
        if(!password.equals(passwordRepeat)){
            model.addAttribute("errorMessagePasswordRepeat", "Passwords is not the same");
        }
        if(controllerServices.checkEmail(email)){
            testedEmail = email;
        }
        else{
            model.addAttribute("errorMessageEmail", "Invalid email adress");
            return "signup";
        }
        if(!userRepository.checkUserEmail(username, email)){ //Last step in the signup where the data gets added to the database
            User user = new User(checkedFirstName, checkedLastName, testedUsername, testedPassword, testedEmail, jobTitle);
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
        int userId = userRepository.checkLogin(username, password);
        if (userId!=0) {
            session.setAttribute("user_id", userId);
            return "redirect:/profilePage";
        }
        else {
            model.addAttribute("loginErrorMessage",
                    "Could not login. Username does not exist or password was incorrect.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        if(session != null){
            session.removeAttribute("user_id");
        }
        return "redirect:/";
    }

    @GetMapping("/profilePage") //Profile page checks whether the user is logged in or not by adding userID to the session
    public String profilePage(HttpSession session, Model model) {
        if (session.getAttribute("user_id") == null) {
            System.out.println("user_id null");
            return "redirect:/";
        }
        int userId = (int) session.getAttribute("user_id");
        model.addAttribute("user_id", session.getAttribute("user_id"));
        model.addAttribute("users", userRepository.getUserInfo(userId));
        model.addAttribute("projects", projectRepository.getProjectsForUser(userId));
        return "profilePage";
    }

    @GetMapping("/updateProfile/{user_id}")
    public String updateProfilePage(@PathVariable("user_id") int findUser, Model model, HttpSession session){
        int userId = (int) session.getAttribute("user_id");
        model.addAttribute("user_id", session.getAttribute("user_id"));
        model.addAttribute("user", userRepository.getUserInfo(userId));
        return "updateProfile";
    }

    @PostMapping("/updateProfile")
    public String updateProfilePost(@RequestParam("user-id") int userId,
                                    @RequestParam("username") String username,
                                    @RequestParam("email") String email,
                                    @RequestParam("first-name") String firstName,
                                    @RequestParam("last-name") String lastName,
                                    @RequestParam("job-title") String jobTitle){
        User user = new User(firstName,lastName,username,email,jobTitle);
        userRepository.updateUser(user, userId);
        return "redirect:/profilePage";
    }

    @PostMapping("/deleteProfile/{user_id}") //Delete function where you have to specifically type DELETE to delete your profile
    public String deleteProfile(@RequestParam("delete") String deleteRequest,
                                @PathVariable("user_id") int userId,
                                HttpSession session){
        if(controllerServices.confirmDelete(deleteRequest)){
        userRepository.deleteProfile(userId);
        session.removeAttribute("user_id");
        return "redirect:/";
        } else {
            return "profilePage";
        }
    }

    @GetMapping("/create-project")
    public String showCreateProject(){
        return "create-project";
    }

    @PostMapping("/create-project") //Takes input parameters for both a project and the first board on said project and adds them to the database.
    public String createProject(@RequestParam("project-name") String projectName,
                                @RequestParam("project-description") String projectDescription,
                                @RequestParam("board-name") String boardName,
                                @RequestParam("start-date") Date startDate,
                                @RequestParam("end-date") Date endDate,
                                HttpSession session){

        if (session.getAttribute("user_id") != null) {
            int userId = (int) session.getAttribute("user_id");
            Project project = new Project();
            project.setProjectName(projectName);
            project.setProjectDescription(projectDescription);
            int projectId = projectRepository.addProject(project);
            Board board = new Board();
            board.setBoardName(boardName);
            board.setStartDate(startDate);
            board.setEndDate(endDate);
            board.setProjectId(projectId);
            projectRepository.addProjectRole(userId, projectId, "project creator");
            boardRepository.createBoard(board);
            return "redirect:/profilePage";
        } else {
            return "redirect:/";
        }

    }

    @GetMapping("/create-board/{project-id}") //Creates additional boards for a specific project
    public String showCreateBoard(@PathVariable("project-id") int projectId, Model model) {
        model.addAttribute("projectId", projectId);
        return "create-board";
    }

    @PostMapping("/create-board")
    public String createBoard(@RequestParam("project-id") int projectId,
                              @RequestParam("board-name") String boardName,
                              @RequestParam("start-date") Date startDate,
                              @RequestParam("end-date") Date endDate) {
        Board board = new Board();
        board.setBoardName(boardName);
        board.setStartDate(startDate);
        board.setEndDate(endDate);
        board.setProjectId(projectId);
        boardRepository.createBoard(board);
        return "redirect:/project/" + projectId;
    }

    @PostMapping("{project-id}/delete-board/{board-id}")
    public String deleteBoard(@RequestParam("delete") String deleteRequest,
                              @PathVariable("board-id") int boardId,
                              @PathVariable("project-id") int projectId) {
        if(controllerServices.confirmDelete(deleteRequest)){
            boardRepository.deleteBoard(boardId);
        }

        return "redirect:/project/"+projectId;
    }

    @GetMapping("/project/{project-id}") //Main page for each project, where boards and tasks are visible
    public String viewProject(@PathVariable("project-id") int projectId, Model model){
        Project project = projectRepository.findProjectById(projectId);
        model.addAttribute("project", project);
        model.addAttribute("boards", boardRepository.getProjectBoards(projectId));
        model.addAttribute("tasks", taskRepository.retrieveProjectTasks(projectId));


        return "project";
    }

    @GetMapping("/project-details/{project-id}") //Detailed view over a project with info about each board and which users are added to the project
    public String viewProjectDetails(@PathVariable("project-id") int projectId, Model model){
        model.addAttribute("project", projectRepository.findProjectById(projectId));
        model.addAttribute("users", userRepository.retrieveProjectUsers(projectId));
        model.addAttribute("boards", boardRepository.getProjectBoardsWithInfo(projectId));

        return "project-details";
    }

    @GetMapping("/edit-project-details/{project-id}")
    public String showEditProjectDetails(@PathVariable("project-id") int projectId, Model model){
        model.addAttribute("project", projectRepository.findProjectById(projectId));
        return "edit-project-details";
    }

    @PostMapping("/edit-project-details/{project-id}")
    public String editProject(@PathVariable("project-id") int projectId,
                              @RequestParam("project-name") String projectName,
                              @RequestParam("project-description") String projectDescription){
        projectRepository.updateProjectDetails(projectId, projectName,projectDescription);

        return "redirect:/project-details/{project-id}";
    }

    @GetMapping("/view-project-tasks/{project-id}")
    public String viewProjectTasks(@PathVariable("project-id") int projectId, Model model) {
        model.addAttribute("project", projectRepository.findProjectById(projectId));
        model.addAttribute("tasks", taskRepository.retrieveProjectTasks(projectId));
        return "view-project-tasks";
    }

    @GetMapping("/project-users/{project-id}") //Page where you can add users to a project. "foundUsers" is a flash attribute that gets added from the searchUsers method
    public String viewProjectUsers(@PathVariable("project-id") int projectId, Model model, @ModelAttribute("foundUsers") User foundUsers) {
        model.addAttribute("project", projectRepository.findProjectById(projectId));
        model.addAttribute("users", userRepository.retrieveProjectUsers(projectId));
        model.addAttribute("foundUsers");
        return "project-users";
    }

    @GetMapping("/edit-task/{task-id}")
    public String showEditTask(@PathVariable("task-id") int taskId, Model model) {
        model.addAttribute("tasks", taskRepository.findById(taskId));
        return "edit-task";
    }

    @PostMapping("/edit-task")
    public String editTask(@RequestParam("task-id") int taskId,
                           @RequestParam("task-name") String taskName,
                           @RequestParam("task-description") String taskDescription,
                           @RequestParam("story-points") int storyPoints,
                           @RequestParam("board-id") int boardId,
                           @RequestParam("task-time") int taskTime) {

        Task task = new Task(taskId, taskName, boardId, taskTime, storyPoints, taskDescription);
        taskRepository.editTask(task);

        int projectId = taskRepository.getProjectId(boardId);

        return "redirect:/project/" + projectId;

    }

    @GetMapping("/create-task/{projectId}/{boardId}")
    public String showCreateTask(@PathVariable("boardId") int boardId, @PathVariable("projectId") int projectId, Model model){
        model.addAttribute("board-id", boardId);
        model.addAttribute("project-id", projectId);
        return "create-task";
    }

    @PostMapping("/create-task")
    public String createTask(@RequestParam("task-name") String taskName,
                             @RequestParam("story-points") int storyPoints,
                             @RequestParam("task-description") String taskDescription,
                             @RequestParam("board_id") int boardId,
                             @RequestParam("project_id") int projectId,
                             HttpSession session){

        if (session.getAttribute("user_id") != null) {
            Task task = new Task();
            task.setTaskName(taskName);
            task.setTaskStatus(0); //Sets task status to 0 so every task starts in the backlog column of a board
            task.setStoryPoints(storyPoints);
            task.setTaskDescription(taskDescription);
            task.setBoardId(boardId);
            taskRepository.addTask(task);

            return "redirect:/project/" + projectId;
        } else {
            return "redirect:/";
        }

    }

    @GetMapping("/project/{project-id}/move-task-right/{task-id}") //Moves a task to the next column to the right
    public String moveTaskRight(@PathVariable("task-id") int taskId,
                                @PathVariable("project-id") int projectId){
        taskRepository.updateTaskStatus(taskId, 1);
        return "redirect:/project/"+projectId;
    }

    @GetMapping("/project/{project-id}/move-task-left/{task-id}") //Moves a task to the left
    public String moveTaskLeft(@PathVariable("task-id") int taskId,
                                @PathVariable("project-id") int projectId){
        taskRepository.updateTaskStatus(taskId, -1);
        return "redirect:/project/" + projectId;
    }


    @GetMapping("/{project-id}/search-users") //Searches for users to add to a project. RedirectAttributes makes it possible to carry the list of users over to another controller
                                                //method. FlashAttribute saves it in the session and removes it from session after it is used.
    public String searchUsers(@PathVariable("project-id") int projectId, @RequestParam("query") String query, RedirectAttributes attributes) {
        List<User> foundUsers = userRepository.searchUsers(query);
        attributes.addFlashAttribute("foundUsers", foundUsers);
        return "redirect:/project-users/" + projectId;
    }

    @GetMapping("/project/{project-id}/delete-task/{task-id}")
    public String deleteTask(@PathVariable("project-id") int projectId, @PathVariable("task-id") int taskId) {
        taskRepository.deleteTask(taskId);
        return "redirect:/project/" + projectId;

    }

    @GetMapping("project-users/{project-id}/add-user-to-project/{user-id}")
    public String showAddUserToProject(@PathVariable("project-id") int projectId,
                                       @PathVariable("user-id") int userId,
                                       Model model){
        model.addAttribute(projectRepository.findProjectById(projectId));
        model.addAttribute("userId", userId);
        return "add-user-to-project";
    }

    @PostMapping("/project-users/{project-id}/add-user-to-project/{user-id}")
    public String addUserToProject(@RequestParam("position") String position,
                                   @PathVariable("project-id") int projectId,
                                   @PathVariable("user-id") int userId){
        projectRepository.addProjectRole(userId, projectId, position);

        return "redirect:/project-users/{project-id}";
    }

    @GetMapping("/project-users/{project-id}/remove-user/{user-id}")
    public String removeUserFromProject(@PathVariable("project-id") int projectId,
                                        @PathVariable("user-id") int userId){
        userRepository.removeFromProject(userId, projectId);

        return "redirect:/project-users/{project-id}";
    }

    @PostMapping("/edit-project-details/{project-id}/delete-project")
    public String deleteProject(@PathVariable("project-id") int projectId,
                                @RequestParam("delete") String deleteRequest){
        if (controllerServices.confirmDelete(deleteRequest)){
            projectRepository.deleteProjectById(projectId);
        return "redirect:/profilePage";}
        else {
            return "redirect:/edit-project-details/{project-id}";
        }
    }
}