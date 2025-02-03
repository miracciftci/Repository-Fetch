package com.task.valven.service.ımpl;

import com.task.valven.dto.UserDto;
import com.task.valven.model.User;
import com.task.valven.repository.UserRepository;
import com.task.valven.service.UserService;
import com.task.valven.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDto getById(Long id) {
        User user = findEntityById(id);
        return userMapper.toUserDto(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findEntityById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> userMapper.toUserDto(user))
                .collect(Collectors.toList());
    }


}
