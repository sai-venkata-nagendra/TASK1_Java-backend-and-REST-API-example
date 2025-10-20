// DataInitializer.java
package com.kaiburr.taskmanager.config;

import com.kaiburr.taskmanager.model.Task;
import com.kaiburr.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Only create sample tasks if database is empty
        long count = taskRepository.count();
        if (count == 0) {
            // Create sample tasks
            Task task1 = new Task("1", "Print Hello", "John Smith", "echo Hello World!");
            Task task2 = new Task("2", "List Files", "Jane Doe", "ls -la");
            Task task3 = new Task("3", "Show Current Directory", "Bob Johnson", "pwd");
            
            taskRepository.save(task1);
            taskRepository.save(task2);
            taskRepository.save(task3);
            
            System.out.println("Sample data initialized");
        } else {
            System.out.println("Database already contains " + count + " tasks, skipping initialization");
        }
    }
}
