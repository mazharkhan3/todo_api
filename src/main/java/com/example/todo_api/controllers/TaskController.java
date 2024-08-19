package com.example.todo_api.controllers;

import com.example.todo_api.data.Task;
import com.example.todo_api.models.tasks.CreateTaskDTO;
import com.example.todo_api.models.tasks.UpdateTaskDTO;
import com.example.todo_api.repository.TaskRepository;
import com.example.todo_api.repository.UserRepository;
import com.example.todo_api.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
@RequestMapping("/todos")
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }


    @PostMapping()
    public ResponseEntity<String> addNewTask(@RequestBody CreateTaskDTO taskDTO) {
        var user = userRepository.findById(taskDTO.getUserId());

        var task = new Task();

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setUser(user.get());

        taskRepository.save(task);
        return ResponseEntity.ok("Saved");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@RequestBody UpdateTaskDTO taskDTO, @PathVariable Long id) {
        var task = taskRepository.findById(id);

        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        task.get().setTitle(taskDTO.getTitle());
        task.get().setDescription(taskDTO.getDescription());

        taskRepository.save(task.get());

        return ResponseEntity.ok("Updated");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable Long id) {
        var task = taskRepository.findById(id);

        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        task.get().setCompleted(!task.get().isCompleted());

        taskRepository.save(task.get());

        return ResponseEntity.ok("Updated");
    }

    @GetMapping()
//    public ResponseEntity<Iterable<Task>> getAllTask(@RequestParam(required = false) String tags, @RequestParam(required = false) LocalDate date) {
    public ResponseEntity<Iterable<Task>> getAllTask(@RequestParam(required = false) boolean status) {

        Iterable<Task> tasks = null;
        if(status) {
            tasks = taskRepository.findByCompleted(status);
        } else {
            tasks = taskRepository.findAll();
        }

        return ResponseEntity.ok(tasks) ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSingleTask(@PathVariable Long id) {
        var task = taskRepository.findById(id);

        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Task not found", null));
        }

        return ResponseEntity.ok(new ApiResponse("Task found", task.get()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable Long id) {
        var task = taskRepository.findById(id);

        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Task not found", null));
        }

        taskRepository.delete(task.get());

        return ResponseEntity.ok(new ApiResponse("Task deleted successfully", null));
    }

}
