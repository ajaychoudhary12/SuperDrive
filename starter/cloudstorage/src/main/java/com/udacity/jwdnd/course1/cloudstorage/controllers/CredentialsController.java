package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.CredentialModel;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CredentialsController {

    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialsController(CredentialService noteService, UserService userService) {
        this.credentialService = noteService;
        this.userService = userService;
    }

    @PostMapping("/credentials")
    public String handleSaveCredentials(CredentialModel credentialModel, Model model) {
        User user = getUser();

        credentialModel.setUserId(user.getUserId());

        int rowsAffected;
        if (credentialModel.getCredentialId() == null) {
            // Create new credential
            rowsAffected = credentialService.createCredentialModel(credentialModel);
        } else {
            // Update credential
            rowsAffected = credentialService.updateCredentialModel(credentialModel);
        }

        if (rowsAffected < 0) {
            return "redirect:/result?error";
        } else {
            return "redirect:/result?success";
        }
    }

    @PostMapping("/deleteCredential")
    public String deleteCredential(@RequestParam("credentialId") Integer credentialId, Model model) {
        User user = getUser();

        int rowsAffected = credentialService.deleteCredential(credentialId);

        if (rowsAffected < 0) {
            return "redirect:/result?error";
        } else {
            return "redirect:/result?success";
        }
    }

    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUser(auth.getName());
    }
}
