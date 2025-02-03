package com.task.valven.service.mapper;

import com.task.valven.dto.UserDto;
import com.task.valven.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserDto userDto);

    UserDto toUserDto(User user);
}
