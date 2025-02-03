package com.task.valven.service.mapper;

import com.task.valven.dto.CommitDetailsDto;
import com.task.valven.dto.CommitDto;
import com.task.valven.model.Commit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CommitMapper {

    CommitDto toDto(Commit commit);

    @Mapping(source = "user",target = "userDto")
    CommitDetailsDto toCommitDetailsDto(Commit commit);



}
