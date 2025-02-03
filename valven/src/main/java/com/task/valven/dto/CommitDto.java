package com.task.valven.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommitDto {
    private Long id;
    private String hash;
    private String repoName;
    private LocalDateTime localDateTime;

}
