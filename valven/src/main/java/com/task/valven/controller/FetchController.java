package com.task.valven.controller;

import com.task.valven.service.FetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fetch")
@RequiredArgsConstructor
public class FetchController {
    private final FetchService fetchService;


    @GetMapping("/fetchPage")
    public String getFetchPage() {
        return "fetchPage";
    }

    @PostMapping("/getGithubCommits")
    public String fetchGitHubCommits(@RequestParam String username, @RequestParam String repoName,
                                     @RequestParam String fromDateTime, RedirectAttributes redirectAttributes) {
        try {
            fetchService.getGitHubCommits(username, repoName, fromDateTime);
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Github Username veya RepoName yanlış");
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } finally {
            return "redirect:/fetch/fetchPage";
        }
    }

    @PostMapping("/getGitLabCommits")
    public String getGitLabCommits(@RequestParam String projectId,RedirectAttributes redirectAttributes) {
        try {
            fetchService.getGitLabCommits(projectId);
        }catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "GitLab Project ID Hatalı");
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } finally {
            return "redirect:/fetch/fetchPage";
        }
    }

}
