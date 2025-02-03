package com.task.valven.service.ımpl;

import com.task.valven.dto.CommitDetailsDto;
import com.task.valven.dto.CommitDto;
import com.task.valven.model.Commit;
import com.task.valven.model.User;
import com.task.valven.repository.CommitRepository;
import com.task.valven.service.CommitService;
import com.task.valven.service.mapper.CommitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommitServiceImpl implements CommitService {
    private final CommitRepository commitRepository;
    private final CommitMapper commitMapper;
    private final UserServiceImpl userServiceImpl;


    @Override
    public CommitDetailsDto getById(Long id) {
        Commit commit = commitRepository.findById(id).orElse(null);
        return commitMapper.toCommitDetailsDto(commit);
    }

    @Override
    public Commit save(Commit commit) {
        return commitRepository.save(commit);
    }

    @Override
    public boolean checkIsSameCommit(String hash) {
        return commitRepository.existsByHash(hash);
    }

    @Override
    public List<CommitDto> findAllByUser(Long userId) {
        User user = userServiceImpl.findEntityById(userId);
        return commitRepository.findCommitsByUser(user)
                .stream()
                .map(commit -> commitMapper.toDto(commit))
                .collect(Collectors.toList());
    }



}
