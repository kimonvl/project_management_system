package com.kimon.project_management_system.service;

import com.kimon.project_management_system.model.Chat;
import com.kimon.project_management_system.model.Project;
import com.kimon.project_management_system.model.User;
import com.kimon.project_management_system.repo.ProjectRepo;
import com.kimon.project_management_system.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;

    @Override
    public Project createProject(Project project, String userEmail) throws Exception {
        User user = userService.findByEmail(userEmail);
        if(user == null){
            throw new Exception("User not found");
        }

        Project newProject = new Project();
        newProject.setName(project.getName());
        newProject.setCategory(project.getCategory());
        newProject.setTags(project.getTags());
        newProject.setOwner(user);
        newProject.getTeam().add(user);
        Project createdProject = projectRepo.save(newProject);
        //add new chat to project
        Chat chat = new Chat();
        chat.setProject(createdProject);
        Chat createdChat = chatService.createChat(chat);
        createdProject.setChat(createdChat);
        projectRepo.save(createdProject);
        userService.updateUserProjectSize(user, 1);
        return createdProject;
    }

    @Override
    public List<Project> getProjectByTeam(String userEmail, String category, List<String> tags) throws Exception {
        User user = userService.findByEmail(userEmail);
        if(category == null && (tags == null || tags.isEmpty())){
            return projectRepo.findDistinctByOwnerOrTeamContains(user, user);
        }
        if(category != null && (tags == null || tags.isEmpty())){
            return projectRepo.findDistinctByOwnerOrTeamWithCategory(user, category);
        }
        if(category == null){
            return projectRepo.findDistinctByOwnerOrTeamWithTags(user, tags, tags.size());
        }

        return projectRepo.findDistinctByOwnerOrTeamWithTagsAndCategory(user, tags, tags.size(), category);

    }

    @Override
    public Project getProjectById(long projectId) throws Exception {
        Optional<Project> project = projectRepo.findById(projectId);
        if(project.isEmpty()){
            throw new Exception("Project not found");
        }
        return project.get();
    }

    @Override
    public void deleteProject(long projectId, String userEmail) throws Exception {
        Project project = getProjectById(projectId);

        if(project != null){
            User user = userService.findByEmail(userEmail);
            if(project.getOwner().getId() == user.getId()){
                projectRepo.deleteById(projectId);
                userService.updateUserProjectSize(user, 1);
                return;
            }
            throw new Exception("Unauthorized user to delete project");
        }
        throw new Exception("Project not found");
    }

    @Override
    public Project updateProject(Project updatedProject, long id, String userEmail) throws Exception {
        User user = userService.findByEmail(userEmail);
        if(user == null){
            throw new Exception("User not found");
        }
        Project project = getProjectById(id);
        if(project == null){
            throw new Exception("Project not found");
        }
        if(!project.getOwner().getEmail().equals(userEmail)){
            throw new Exception("User have no access to this project");
        }
        project.setName(updatedProject.getName());
        project.setDescription(updatedProject.getDescription());
        project.setCategory(updatedProject.getCategory());
        project.setTags(updatedProject.getTags());
        return projectRepo.save(project);
    }

    @Override
    public void addUserToProject(long projectId, String userEmail) throws Exception {
        User user = userService.findByEmail(userEmail);
        Project project = getProjectById(projectId);
        if (user == null){
            throw new Exception("User not found");
        }
        if (project == null){
            throw new Exception("Project not found");
        }
        if(!project.getTeam().contains(user) && !project.getChat().getUsers().contains(user)){
            project.getTeam().add(user);
            project.getChat().getUsers().add(user);
            projectRepo.save(project);
        }
    }

    @Override
    public void removeUserFromProject(long projectId, String userEmail) throws Exception {
        User user = userService.findByEmail(userEmail);
        Project project = getProjectById(projectId);
        if (user == null){
            throw new Exception("User not found");
        }
        if (project == null){
            throw new Exception("Project not found");
        }
        if(project.getTeam().contains(user) && project.getChat().getUsers().contains(user)){
            project.getTeam().remove(user);
            project.getChat().getUsers().remove(user);
            projectRepo.save(project);
        }
    }

    @Override
    public List<Project> searchProjectByName(String keyword, String userEmail) throws Exception {
        User user = userService.findByEmail(userEmail);
        if(user == null){
            throw new Exception("User not found");
        }

        return projectRepo.findByNameContainingIgnoreCaseAndTeamContaining(keyword, user);
    }
}
