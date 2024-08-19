package com.example.todo_api.models.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UpdateTaskDTO {
    private String title;
    private String description;
}
