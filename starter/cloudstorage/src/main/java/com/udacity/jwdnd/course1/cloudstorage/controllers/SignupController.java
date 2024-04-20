package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.Console;

@Controller()
@RequestMapping("/signup")
public class SignupController {
    final UserService userService;

    SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getSignupForm() {
        return "signup";
    }

    @PostMapping()
    public String postSignupData(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        String signupError = null;

        if (!userService.isUsernameAvailable(user.getUsername())) {
            signupError = "Username not available";
        }

        if (signupError == null) {
            int rowsAdded = userService.saveUser(user);
            if (rowsAdded < 0) {
                signupError = "There was an error in creating your account";
            }
        }

        if (signupError == null) {
            redirectAttributes.addFlashAttribute("signupSuccess", true);
            return "redirect:/login";
        } else {
            model.addAttribute("signupError", signupError);
        }

        return "signup";
    }
}
