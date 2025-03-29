package com.spring.Task6.Repositories;

import com.spring.Task6.model.Role;
import com.spring.Task6.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
}
