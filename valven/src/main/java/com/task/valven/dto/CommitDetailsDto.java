package com.task.valven.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommitDetailsDto {
    private Long id;
    private UserDto userDto;
    private String hash;
    private String platformName;
    private String repoName;
    private String message;
    private LocalDateTime localDateTime;
}
