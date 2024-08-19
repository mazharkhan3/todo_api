package com.example.todo_api.repository;

import com.example.todo_api.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
