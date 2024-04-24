package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
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
        return "redirect:/home";
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
        return "redirect:/home";
    }

    @GetMapping("/download/{fileId}")
    @ResponseBody
    public ResponseEntity<byte[]> downloadFile(@PathVariable Integer fileId) {
        File file = fileService.getFileById(fileId);
        if (file == null || file.getFileData() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(file.getContentType()));
        headers.setContentDispositionFormData(file.getFilename(), file.getFilename());

        return new ResponseEntity<>(file.getFileData(), headers, HttpStatus.OK);
    }

    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUser(auth.getName());
    }
}
