package com.example.todo_api.data;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    public Task() {
        this.createdOn = LocalDateTime.now();
        this.completed = false;
    }

}
