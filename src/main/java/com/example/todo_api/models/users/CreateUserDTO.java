package com.example.todo_api.models.users;

import com.example.todo_api.data.Task;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CreateUserDTO {
    private String name;
    private String email;
    private String password;
}
