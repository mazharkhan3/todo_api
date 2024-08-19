package com.example.todo_api.repository;

import com.example.todo_api.data.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
    Iterable<Task> findByCompleted(boolean status);
}
