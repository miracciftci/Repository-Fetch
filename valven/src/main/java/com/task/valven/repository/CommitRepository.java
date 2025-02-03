package com.task.valven.repository;

import com.task.valven.model.Commit;
import com.task.valven.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommitRepository extends JpaRepository<Commit, Long> {
    List<Commit> findCommitsByUser(User user);
    boolean existsByHash(String hash);

}
