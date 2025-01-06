package com.jdbc.practice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jdbc.practice.dao.FileDao;
import com.jdbc.practice.model.File;

@Service
public class FileService {

    @Autowired
    private FileDao fileDao;

    public String uploadFile(MultipartFile file, String fileName) {
        try {
            return fileDao.uploadFile(file.getBytes(), fileName);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File downloadFile(int id) {
        return fileDao.downloadFile(id);
    }

    public String updateFile(int id, MultipartFile file, String fileName) {
        try {
            return fileDao.updateUploadedFile(id, file.getBytes(), fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
