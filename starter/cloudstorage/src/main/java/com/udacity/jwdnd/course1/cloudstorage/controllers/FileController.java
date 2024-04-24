package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class FileController {
    private final UserService userService;
    private final FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/file")
    public String handleSaveFile(@RequestParam("fileUpload") MultipartFile file, Model model) {
        User user = getUser();

        File existingFile = fileService.getFile(file.getOriginalFilename());

        if (existingFile != null) {
            model.addAttribute("infoMessage", "File with this name already exists");
            model.addAttribute("files", fileService.getFiles(user.getUserId()));
            return "home";
        }

        try {
            File newFile = new File();
            newFile.setFilename(file.getOriginalFilename());
            newFile.setContentType(file.getContentType());
            newFile.setFileSize(String.valueOf(file.getSize()));
            newFile.setFileData(file.getBytes());
            newFile.setUserId(user.getUserId());

            int rowsAffected = fileService.insertFile(newFile);

            if (rowsAffected < 0) {
                model.addAttribute("infoMessage", "File upload failed");
            } else {
                model.addAttribute("infoMessage", "File uploaded successfully");
            }
        } catch (IOException exception) {
            model.addAttribute("infoMessage", "File upload failed");
        }

        model.addAttribute("files", fileService.getFiles(user.getUserId()));
        return "home";
    }

    @PostMapping("/deleteFile")
    public String deleteFile(@RequestParam("fileId") Integer fileId, Model model) {
        User user = getUser();

        int rowsAffected = fileService.deleteFile(fileId);

        if (rowsAffected < 0) {
            model.addAttribute("infoMessage", "File deletion failed");
        } else {
            model.addAttribute("infoMessage", "File deletion successfully");
        }

        model.addAttribute("files", fileService.getFiles(user.getUserId()));
        return "home";
    }

    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUser(auth.getName());
    }
}
