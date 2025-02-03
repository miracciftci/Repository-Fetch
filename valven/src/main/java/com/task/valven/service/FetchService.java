package com.task.valven.service;

import com.task.valven.model.User;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface FetchService {
    void getGitHubCommits(@RequestParam String username, @RequestParam String repoName, String fromDateTime);
    void getGitLabCommits(String projectId);
    void parseAllCommitsInGithubRepo(List<Map<String, Object>> listCommits, String repoName);
    User getCurrentUserinGithubCommit(Map<String, Object> commitAllData);
    LocalDateTime getparseLocalDateTime(String localDateTime);
    User getCurrentUserInGitLabCommit(String email, String username);
    void parseAllCommitsInGitLabRepo(List<Map<String, Object>> listCommits, String repoName);


}
