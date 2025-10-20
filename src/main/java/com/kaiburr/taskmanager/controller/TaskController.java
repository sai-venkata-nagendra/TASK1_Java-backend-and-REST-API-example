package com.kaiburr.taskmanager.controller;

import com.kaiburr.taskmanager.model.Task;
import com.kaiburr.taskmanager.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        logger.info("GET /api/tasks - Fetching all tasks");
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        logger.info("POST /api/tasks - Creating task: name={}, owner={}, command={}", 
                   task.getName(), task.getOwner(), task.getCommand());
        
        try {
            // Generate ID if not provided
            if (task.getId() == null || task.getId().isEmpty()) {
                task.setId(UUID.randomUUID().toString());
            }
            
            Task savedTask = taskService.createOrUpdateTask(task);
            logger.info("Task created successfully with ID: {}", savedTask.getId());
            return ResponseEntity.ok(savedTask);
        } catch (Exception e) {
            logger.error("Error creating task: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasksByName(@RequestParam String name) {
        List<Task> tasks = taskService.getTasksByName(name);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}/execute")
    public ResponseEntity<Task> executeTask(@PathVariable String id) {
        try {
            logger.info("Executing task: {}", id);
            Task task = taskService.executeTask(id);
            logger.info("Task executed successfully: {}", id);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            logger.error("Error executing task {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
