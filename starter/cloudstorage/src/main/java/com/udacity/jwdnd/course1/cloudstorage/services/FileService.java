package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFiles(Integer userId) {
        return fileMapper.getFiles(userId);
    }

    public File getFile(String filename) { return fileMapper.getFile(filename); }

    public int insertFile(File file) {
        return fileMapper.insert(file);
    }

    public int deleteFile(Integer fileId) { return fileMapper.delete(fileId); }

    public File getFileById(Integer fileId) { return fileMapper.getFileById(fileId); }
}
