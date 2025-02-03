package com.task.valven.service;

import com.task.valven.dto.UserDto;
import com.task.valven.model.User;

import java.util.List;

public interface UserService {
    User save(User user);
    UserDto getById(Long id);
    void deleteById(Long id);
    User findEntityById(Long id);
    User findByUserName(String userName);
    List<UserDto> findAll();
    User findByEmail(String email);
}
