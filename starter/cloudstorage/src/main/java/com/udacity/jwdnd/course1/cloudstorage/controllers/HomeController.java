package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@Controller
public class HomeController {
    private final NoteService noteService;
    private final UserService userService;

    public HomeController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String getHome(Model model) {
        User user = getUser();
        if (user == null) {
            return "redirect:/logout";
        }

        model.addAttribute("notes", noteService.getNotes(user.getUserId()));
        return "home";
    }

    @PostMapping("/notes")
    public String addNewNote(Note note, Model model) {
        User user = getUser();
        if (user == null) {
            return "redirect:/logout";
        }

        int rowsAffected = noteService.createNote(note, user.getUserId());
        if (rowsAffected < 0) {
            model.addAttribute("infoMessage", "Note creation failed");
        } else {
            model.addAttribute("infoMessage", "Note created successfully");
        }

        model.addAttribute("notes", noteService.getNotes(user.getUserId()));

        return "home";
    }

    @PostMapping("/deleteNote")
    public String deleteNote(@RequestParam("noteId") Integer noteId, Model model) {
        int rowsAffected = noteService.deleteNote(noteId);

        if (rowsAffected < 0) {
            model.addAttribute("infoMessage", "Note deletion failed");
        } else {
            model.addAttribute("infoMessage", "Note deleted successfully");
        }
        return "home";
    }

    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUser(auth.getName());
    }
}