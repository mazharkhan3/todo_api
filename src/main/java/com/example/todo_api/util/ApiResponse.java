package com.example.todo_api.util;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse {
    private String message;
    private Object data;
    private LocalDateTime timestamp;

    public ApiResponse(String message, Object data) {
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}
