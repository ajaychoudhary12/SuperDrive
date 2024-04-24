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

        return "redirect:/home";
    }

    @PostMapping("/deleteCredential")
    public String deleteCredential(@RequestParam("credentialId") Integer credentialId, Model model) {
        User user = getUser();

        int rowsAffected = credentialService.deleteCredential(credentialId);

        if (rowsAffected < 0) {
            model.addAttribute("infoMessage", "Credential deletion failed");
        } else {
            model.addAttribute("infoMessage", "Credential deleted successfully");
        }

        model.addAttribute("credentials", credentialService.getCredentialList(user.getUserId()));
        return "redirect:/home";
    }

    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUser(auth.getName());
    }
}
