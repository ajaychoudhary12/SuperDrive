package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NotesController {
    private final NoteService noteService;
    private final UserService userService;

    public NotesController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/notes")
    public String handleSaveNote(Note note, Model model) {
        User user = getUser();

        if (note.getNoteId() == null) {
            // Create new note
            int rowsAffected = noteService.createNote(note, user.getUserId());
            if (rowsAffected < 0) {
                model.addAttribute("infoMessage", "Note creation failed");
            } else {
                model.addAttribute("infoMessage", "Note created successfully");
            }
        } else {
            int rowsAffected = noteService.updateNote(note);
            if (rowsAffected < 0) {
                model.addAttribute("infoMessage", "Note update failed");
            } else {
                model.addAttribute("infoMessage", "Note updated successfully");
            }
        }

        model.addAttribute("notes", noteService.getNotes(user.getUserId()));
        return "home";
    }

    @PostMapping("/deleteNote")
    public String deleteNote(@RequestParam("noteId") Integer noteId, Model model) {
        User user = getUser();

        int rowsAffected = noteService.deleteNote(noteId);

        if (rowsAffected < 0) {
            model.addAttribute("infoMessage", "Note deletion failed");
        } else {
            model.addAttribute("infoMessage", "Note deleted successfully");
        }

        model.addAttribute("notes", noteService.getNotes(user.getUserId()));
        return "home";
    }

    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUser(auth.getName());
    }
}
