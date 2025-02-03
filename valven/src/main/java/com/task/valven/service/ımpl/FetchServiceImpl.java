package com.task.valven.service.ımpl;

import com.task.valven.model.Commit;
import com.task.valven.model.User;
import com.task.valven.service.CommitService;
import com.task.valven.service.FetchService;
import com.task.valven.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class FetchServiceImpl implements FetchService {
    private static final String GITHUB_API_URL = "https://api.github.com/repos/{username}/{repoName}/commits";
    private static final String GITLAB_API_URL = "https://gitlab.com/api/v4/projects/{project_id}/repository/commits";

    private final RestTemplate restTemplate;
    private final CommitService commitService;
    private final UserService userService;


    @Override
    public void getGitHubCommits(@RequestParam String username, @RequestParam String repoName, String fromDateTime) {
        LocalDateTime parsedDateTime = getparseLocalDateTime(fromDateTime);
        String url = GITHUB_API_URL.replace("{username}", username).replace("{repoName}", repoName) + "?per_page=100";
        if (parsedDateTime != null) {
            // eğer tarih verilmemişse tüm commitleri çekmiş olacağız
            String formattedFromDate = "&since=" + parsedDateTime.format(DateTimeFormatter.ISO_DATE_TIME) + "Z";
            url += formattedFromDate;
        }
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        parseAllCommitsInGithubRepo(response.getBody(),repoName);
    }


    @Override
    public void getGitLabCommits(String projectId) {
        String url = GITLAB_API_URL.replace("{project_id}", projectId);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        parseAllCommitsInGitLabRepo(response.getBody(),projectId);
    }

    @Override
    public void parseAllCommitsInGitLabRepo(List<Map<String, Object>> listCommits, String repoName) {
        for (Object map : listCommits) {
            Commit commit = new Commit();
            Map<String, Object> commitData = (Map<String, Object>) map;
            commit.setHash((String) commitData.get("id"));
            commit.setRepoName(repoName);
            commit.setPlatformName("GitLab");

            String message = (String) commitData.get("message");
            message = message.length()>255 ? message.substring(0, 255) : message;
            commit.setMessage(message);

            LocalDateTime localDateTime = Instant.parse((String) commitData.get("created_at"))
                    .atZone(ZoneId.of("UTC"))
                    .toLocalDateTime();
            commit.setLocalDateTime(localDateTime);

            String email = (String) commitData.get("author_email");
            String username = (String) commitData.get("author_name");
            User user = getCurrentUserInGitLabCommit(email,username);
            commit.setUser(user);

            if (!commitService.checkIsSameCommit(commit.getHash())) {
                userService.save(user);
                commitService.save(commit);
            }
        }
    }


    @Override
    public void parseAllCommitsInGithubRepo(List<Map<String, Object>> listCommits, String repoName) {
        for (Object map : listCommits) {
            Commit commit = new Commit();
            Map<String, Object> commitAllData = (Map<String, Object>) map;
            Map<String, Object> commitData = (Map<String, Object>) commitAllData.get("commit");

            String message = (String) commitData.get("message");
            message = message.length()>255 ? message.substring(0, 255) : message;
            commit.setMessage(message);

            commit.setHash((String) commitAllData.get("sha"));
            commit.setRepoName(repoName);
            commit.setPlatformName("GitHub");

            User user = getCurrentUserinGithubCommit(commitAllData);
            if (Objects.isNull(user)) {
                continue;
            }
            commit.setUser(user);

            Map<String, Object> authorData = (Map<String, Object>) commitData.get("author");
            LocalDateTime localDateTime = Instant.parse((String) authorData.get("date"))
                    .atZone(ZoneId.of("UTC"))
                    .toLocalDateTime();
            commit.setLocalDateTime(localDateTime);

            // checkIsSameCommit'i ile daha önce kaydedilmiş ise bu commmit tekrardan kaydetmemmek için yapılan kontrol
            if (!commitService.checkIsSameCommit(commit.getHash())) {
                userService.save(user);
                commitService.save(commit);
            }
        }
    }

    // eğer kullanıcı kayıtlı ise o kullanıcıyı döndürür
    // kullanıcı kayıtlı değil ise yeni bir tane kullanıcı oluşturup döndürür
    @Override
    public User getCurrentUserinGithubCommit(Map<String, Object> commitAllData) {
        Map<String, Object> commitData = (Map<String, Object>) commitAllData.get("commit");
        Map<String, Object> committerData = (Map<String, Object>) commitData.get("committer");
        String userName = (String) committerData.get("name");
        String email = (String) committerData.get("email");
        if ("GitHub".equals(userName)){
            Map<String, Object> authorData = (Map<String, Object>) commitAllData.get("author");
            if (Objects.isNull(authorData)){
                return null;
            }
            userName = (String) authorData.get("login");
        }

        User user = userService.findByUserName(userName);
        if (Objects.isNull(user)) {
            User newUser = new User();
            newUser.setUsername(userName);
            newUser.setEmail(email);
            return newUser;
        } else if (user.getEmail().equals("noreply@github.com")) {
            user.setEmail(email);
        }
        return user;
    }

    @Override
    public LocalDateTime getparseLocalDateTime(String localDateTime) {
        LocalDateTime parsedDateTime = null;
        if (localDateTime != null && !localDateTime.isEmpty()) {
            parsedDateTime = LocalDateTime.parse(localDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        return parsedDateTime;
    }

    @Override
    public User getCurrentUserInGitLabCommit(String email, String username){
        User user = userService.findByUserName(username);
        if (Objects.isNull(user)){
            return new User(null,null,username,email);
        }
        return user;
    }



}
