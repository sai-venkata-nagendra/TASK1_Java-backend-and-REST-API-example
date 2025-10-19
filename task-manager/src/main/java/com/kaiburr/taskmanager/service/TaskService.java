// TaskService.java
package com.kaiburr.taskmanager.service;

import com.kaiburr.taskmanager.model.Task;
import com.kaiburr.taskmanager.model.TaskExecution;
import com.kaiburr.taskmanager.repository.TaskRepository;
import com.kaiburr.taskmanager.security.CommandValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private CommandValidator commandValidator;
    
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    
    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }
    
    public List<Task> getTasksByName(String name) {
        return taskRepository.findByNameContaining(name);
    }
    
    public Task createOrUpdateTask(Task task) {
        if (!commandValidator.isSafeCommand(task.getCommand())) {
            throw new IllegalArgumentException("Unsafe command detected: " + task.getCommand());
        }
        return taskRepository.save(task);
    }
    
    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }
    
    public TaskExecution executeTask(String taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isEmpty()) {
            throw new RuntimeException("Task not found with id: " + taskId);
        }
        
        Task task = taskOpt.get();
        TaskExecution execution = new TaskExecution();
        execution.setStartTime(new Date());
        
        try {
            // Execute the command (simulated - in real scenario, this would run in k8s pod)
            ProcessBuilder processBuilder = new ProcessBuilder();
            
            // For security, we'll simulate execution rather than actually running commands
            // In production, this would interface with Kubernetes API
            String output = simulateCommandExecution(task.getCommand());
            
            execution.setEndTime(new Date());
            execution.setOutput(output);
            
        } catch (Exception e) {
            execution.setEndTime(new Date());
            execution.setOutput("Error executing command: " + e.getMessage());
        }
        
        task.addTaskExecution(execution);
        taskRepository.save(task);
        
        return execution;
    }
    
    private String simulateCommandExecution(String command) {
        // Simulate command execution for demo purposes
        // In real implementation, this would execute in a Kubernetes pod
        
        if (command.startsWith("echo ")) {
            return command.substring(5);
        } else if (command.startsWith("ls")) {
            return "file1.txt\nfile2.txt\nREADME.md";
        } else if (command.startsWith("pwd")) {
            return "/home/user/workspace";
        } else if (command.startsWith("date")) {
            return new Date().toString();
        } else if (command.startsWith("whoami")) {
            return "demo-user";
        } else {
            return "Command executed successfully: " + command;
        }
    }
}
