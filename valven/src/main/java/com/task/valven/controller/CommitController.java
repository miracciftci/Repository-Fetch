package com.task.valven.controller;

import com.task.valven.dto.CommitDetailsDto;
import com.task.valven.dto.CommitDto;
import com.task.valven.dto.UserDto;
import com.task.valven.service.CommitService;
import com.task.valven.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/commits")
@RequiredArgsConstructor
public class CommitController {
    private final CommitService commitService;
    private final UserService userService;

    @GetMapping("/getByUser/{userId}")
    public String getAllCommitsByUser(@PathVariable long userId, Model model) {
        List<CommitDto> commits = commitService.findAllByUser(userId);
        UserDto user = userService.getById(userId);
        model.addAttribute("user",user);
        model.addAttribute("commits", commits);
        return "commitsPage";
    }

    @GetMapping("/getById/{id}")
    public String getAllCommits(@PathVariable long id, Model model) {
        CommitDetailsDto commit = commitService.getById(id);
        model.addAttribute("commit", commit);
        return "commitDetailPage";
    }

}
