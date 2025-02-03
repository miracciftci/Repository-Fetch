package com.task.valven.service;

import com.task.valven.dto.CommitDetailsDto;
import com.task.valven.dto.CommitDto;
import com.task.valven.model.Commit;

import java.util.List;

public interface CommitService {
    CommitDetailsDto getById(Long id);
    Commit save(Commit commit);
    boolean checkIsSameCommit(String hash);
    List<CommitDto> findAllByUser(Long userId);

}
