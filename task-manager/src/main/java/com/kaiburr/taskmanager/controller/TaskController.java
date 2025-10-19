package com.kaiburr.taskmanager.controller;

import com.kaiburr.taskmanager.model.Task;
import com.kaiburr.taskmanager.model.TaskExecution;
import com.kaiburr.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    // GET all tasks or single task by ID
    @GetMapping
    public ResponseEntity<?> getTasks(@RequestParam(required = false) String id) {
        if (id != null) {
            Optional<Task> task = taskService.getTaskById(id);
            if (task.isPresent()) {
                return ResponseEntity.ok(task.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Task not found with id: " + id);
            }
        } else {
            List<Task> tasks = taskService.getAllTasks();
            return ResponseEntity.ok(tasks);
        }
    }
    
    // GET tasks by name
    @GetMapping("/search")
    public ResponseEntity<?> getTasksByName(@RequestParam String name) {
        List<Task> tasks = taskService.getTasksByName(name);
        if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No tasks found with name containing: " + name);
        }
        return ResponseEntity.ok(tasks);
    }
    
    // PUT create or update task
    @PutMapping
    public ResponseEntity<?> createOrUpdateTask(@RequestBody Task task) {
        try {
            Task savedTask = taskService.createOrUpdateTask(task);
            return ResponseEntity.ok(savedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error saving task: " + e.getMessage());
        }
    }
    
    // DELETE task by ID
    @DeleteMapping
    public ResponseEntity<?> deleteTask(@RequestParam String id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok("Task deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Task not found with id: " + id);
        }
    }
    
    // PUT execute task
    @PutMapping("/execute")
    public ResponseEntity<?> executeTask(@RequestParam String id) {
        try {
            TaskExecution execution = taskService.executeTask(id);
            return ResponseEntity.ok(execution);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error executing task: " + e.getMessage());
        }
    }
}
