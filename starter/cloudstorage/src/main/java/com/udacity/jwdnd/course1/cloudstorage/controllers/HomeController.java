package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.CredentialModel;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private final NoteService noteService;
    private final UserService userService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String getHome(Model model) {
        User user = getUser();
        if (user == null) {
            return "redirect:/logout";
        }

        model.addAttribute("credentials", credentialService.getCredentialList(user.getUserId()));
        model.addAttribute("notes", noteService.getNotes(user.getUserId()));
        return "home";
    }

    @PostMapping("/credentials")
    public String handleSaveCredentials(CredentialModel credentialModel, Model model) {
        User user = getUser();
        if (user == null) {
            return "redirect:/logout";
        }

        credentialModel.setUserId(user.getUserId());

        if (credentialModel.getCredentialId() == null) {
            // Create new credential
            int rowsAffected = credentialService.createCredentialModel(credentialModel);
            if (rowsAffected < 0) {
                model.addAttribute("infoMessage", "Credential creation failed");
            } else {
                model.addAttribute("infoMessage", "Credential created successfully");
            }
        } else {
            // Update credential
            int rowsAffected = credentialService.updateCredentialModel(credentialModel);
            if (rowsAffected < 0) {
                model.addAttribute("infoMessage", "Credential update failed");
            } else {
                model.addAttribute("infoMessage", "Credential updated successfully");
            }
        }

        model.addAttribute("credentials", credentialService.getCredentialList(user.getUserId()));

        return "home";
    }

    @PostMapping("/deleteCredential")
    public String deleteCredential(@RequestParam("credentialId") Integer credentialId, Model model) {
        User user = getUser();
        if (user == null) {
            return "redirect:/logout";
        }

        int rowsAffected = credentialService.deleteCredential(credentialId);

        if (rowsAffected < 0) {
            model.addAttribute("infoMessage", "Credential deletion failed");
        } else {
            model.addAttribute("infoMessage", "Credential deleted successfully");
        }

        model.addAttribute("credentials", credentialService.getCredentialList(user.getUserId()));
        return "home";
    }

    @PostMapping("/notes")
    public String handleSaveNote(Note note, Model model) {
        User user = getUser();
        if (user == null) {
            return "redirect:/logout";
        }

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
        if (user == null) {
            return "redirect:/logout";
        }

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
