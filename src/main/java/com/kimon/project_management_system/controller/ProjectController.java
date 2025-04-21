package com.kimon.project_management_system.controller;

import com.kimon.project_management_system.model.Project;
import com.kimon.project_management_system.model.User;
import com.kimon.project_management_system.request.ProjectFilter;
import com.kimon.project_management_system.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody Project project){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            Project createdProject = projectService.createProject(project, userEmail);
            if (createdProject == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/myProjects")
    public ResponseEntity<List<Project>> getProjectsByTeamWithFilters(@RequestBody ProjectFilter projectFilter){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            List<Project> projects = projectService.getProjectByTeam(userEmail, projectFilter.getCategory(), projectFilter.getTags());
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getById/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable long projectId){
        try {
            Project project = projectService.getProjectById(projectId);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("delete/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable long projectId){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            projectService.deleteProject(projectId, userEmail);
            return new ResponseEntity<>("Project deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("update/{projectId}")
    public ResponseEntity<Project> updateProject(@RequestBody Project project, @PathVariable long projectId){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            Project updatedProject = projectService.updateProject(project, projectId, userEmail);
            return new ResponseEntity<>(updatedProject, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}