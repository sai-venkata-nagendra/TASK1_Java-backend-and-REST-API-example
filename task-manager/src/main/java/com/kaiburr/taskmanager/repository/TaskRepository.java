// TaskRepository.java
package com.kaiburr.taskmanager.repository;

import com.kaiburr.taskmanager.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Task> findByNameContaining(String name);
    
    List<Task> findByOwner(String owner);
}
