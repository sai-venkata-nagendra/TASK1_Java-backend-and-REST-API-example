package com.kaiburr.taskmanager.service;

import com.kaiburr.taskmanager.model.Task;
import com.kaiburr.taskmanager.model.TaskExecution;
import com.kaiburr.taskmanager.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @PostConstruct
    public void initSampleData() {
        try {
            long count = taskRepository.count();
            logger.info("Current number of tasks in database: {}", count);
            
            if (count == 0) {
                logger.info("Creating sample tasks...");
                
                Task task1 = new Task();
                task1.setId(UUID.randomUUID().toString());
                task1.setName("Print Welcome Message");
                task1.setOwner("System Admin");
                task1.setCommand("echo 'Welcome to Kaiburr Task Manager'");
                task1.setTaskExecutions(new ArrayList<>());

                Task task2 = new Task();
                task2.setId(UUID.randomUUID().toString());
                task2.setName("List Directory Contents");
                task2.setOwner("Test User");
                task2.setCommand("ls -la");
                task2.setTaskExecutions(new ArrayList<>());

                Task task3 = new Task();
                task3.setId(UUID.randomUUID().toString());
                task3.setName("Show Current Date");
                task3.setOwner("Demo User");
                task3.setCommand("date");
                task3.setTaskExecutions(new ArrayList<>());

                taskRepository.saveAll(List.of(task1, task2, task3));
                logger.info("Sample tasks created successfully");
            } else {
                logger.info("Database already contains {} tasks", count);
            }
        } catch (Exception e) {
            logger.error("Error initializing sample data: {}", e.getMessage());
        }
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        logger.info("Retrieved {} tasks from MongoDB", tasks.size());
        return tasks;
    }

    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    public Task createOrUpdateTask(Task task) {
        try {
            // Generate ID if not provided
            if (task.getId() == null || task.getId().isEmpty()) {
                task.setId(UUID.randomUUID().toString());
            }
            
            // Initialize taskExecutions if null
            if (task.getTaskExecutions() == null) {
                task.setTaskExecutions(new ArrayList<>());
            }
            
            Task savedTask = taskRepository.save(task);
            logger.info("Task saved to MongoDB with ID: {}", savedTask.getId());
            return savedTask;
        } catch (Exception e) {
            logger.error("Error saving task to MongoDB: {}", e.getMessage());
            throw new RuntimeException("Failed to save task: " + e.getMessage());
        }
    }

    public void deleteTask(String id) {
        try {
            if (taskRepository.existsById(id)) {
                taskRepository.deleteById(id);
                logger.info("Task deleted from MongoDB: {}", id);
            } else {
                throw new RuntimeException("Task not found with id: " + id);
            }
        } catch (Exception e) {
            logger.error("Error deleting task from MongoDB: {}", e.getMessage());
            throw new RuntimeException("Failed to delete task: " + e.getMessage());
        }
    }

    public List<Task> getTasksByName(String name) {
        return taskRepository.findByNameContainingIgnoreCase(name);
    }

    public Task executeTask(String id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            logger.info("Executing task: {}", task.getCommand());
            
            try {
                // Create a new TaskExecution
                TaskExecution execution = new TaskExecution();
                execution.setStartTime(new Date());
                
                // Execute the command
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.command("sh", "-c", task.getCommand());
                processBuilder.redirectErrorStream(true);
                
                Process process = processBuilder.start();
                
                // Read the output
                StringBuilder output = new StringBuilder();
                try (java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                }
                
                // Wait for the process to complete
                int exitCode = process.waitFor();
                execution.setEndTime(new Date());
                
                // Set the output (include exit code for debugging)
                String commandOutput = output.toString();
                if (commandOutput.isEmpty()) {
                    commandOutput = "Command executed successfully (exit code: " + exitCode + ")";
                } else {
                    commandOutput += "\nExit code: " + exitCode;
                }
                execution.setOutput(commandOutput);
                
                // Add the execution to the task
                task.getTaskExecutions().add(execution);
                
                // Save the updated task
                Task savedTask = taskRepository.save(task);
                logger.info("Task execution completed for task: {}", id);
                
                return savedTask;
            } catch (Exception e) {
                logger.error("Error executing command for task {}: {}", id, e.getMessage());
                
                // Create an error execution record
                TaskExecution errorExecution = new TaskExecution();
                errorExecution.setStartTime(new Date());
                errorExecution.setEndTime(new Date());
                errorExecution.setOutput("Error executing command: " + e.getMessage());
                
                task.getTaskExecutions().add(errorExecution);
                Task savedTask = taskRepository.save(task);
                
                return savedTask;
            }
        } else {
            throw new RuntimeException("Task not found with id: " + id);
        }
    }
}
