package com.kimon.project_management_system.service;

import com.kimon.project_management_system.model.Project;
import com.kimon.project_management_system.model.User;

import java.util.List;

public interface ProjectService {

    Project createProject(Project project, String userEmail) throws Exception;
    /*
    Returns the projects where user belongs to team filtered by tags and category
     */
    List<Project> getProjectByTeam(String userEmail, String category, List<String> tags) throws Exception;

    Project getProjectById(long projectId) throws Exception;

    void deleteProject(long projectId, String userEmail) throws Exception;

    Project updateProject(Project updatedProject, long id, String userEmail) throws Exception;

    void addUserToProject(long projectId, String userEmail) throws Exception;

    void removeUserFromProject(long projectId, String userEmail) throws Exception;

    List<Project> searchProjectByName(String keyword, String userEmail) throws Exception;
}
