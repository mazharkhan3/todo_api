package com.example.todo_api.controllers;

import com.example.todo_api.data.Task;
import com.example.todo_api.data.User;
import com.example.todo_api.models.tasks.CreateTaskDTO;
import com.example.todo_api.models.tasks.UpdateTaskDTO;
import com.example.todo_api.repository.TaskRepository;
import com.example.todo_api.repository.UserRepository;
import com.example.todo_api.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public ResponseEntity<ApiResponse> addNewTask(@RequestBody CreateTaskDTO taskDTO) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();

            Optional<User> user = userRepository.findByEmail(username);
            var task = new Task();

            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setUser(user.get());

            taskRepository.save(task);
            return ResponseEntity.ok(new ApiResponse("Task created", null));
        } else {
            throw new Exception(new ApiResponse("User not found", null).toString());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTask(@RequestBody UpdateTaskDTO taskDTO, @PathVariable Long id) {
        var task = taskRepository.findById(id);

        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        task.get().setTitle(taskDTO.getTitle());
        task.get().setDescription(taskDTO.getDescription());

        taskRepository.save(task.get());

        return ResponseEntity.ok(new ApiResponse("Task updated", null));
    }

    @PatchMapping("/{id}/change-status")
    public ResponseEntity<ApiResponse> changeStatus(@PathVariable Long id) {
        var task = taskRepository.findById(id);

        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Task not found", null));
        }

        task.get().setCompleted(!task.get().isCompleted());

        taskRepository.save(task.get());

        return ResponseEntity.ok(new ApiResponse("Status changed", null));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> getAllTask(@RequestParam(required = false) boolean status) {

        Iterable<Task> tasks;
        if (status) {
            tasks = taskRepository.findByCompleted(status);
        } else {
            tasks = taskRepository.findAll();
        }

        return ResponseEntity.ok(new ApiResponse("Tasks found", tasks));
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
