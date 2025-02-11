package com.jdbc.practice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jdbc.practice.model.File;
import com.jdbc.practice.service.FileService;

@RestController
@RequestMapping("/files")
public class Files {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam String fileName, @RequestParam MultipartFile file) {
        return fileService.uploadFile(file, fileName);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable int id) {
        File file = fileService.downloadFile(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.getFileName())
                .header("Content-Type", "image/png")
                .body(file.getFile());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateFile(@PathVariable int id, @RequestParam(required = false) String fileName,
            @RequestParam(required = false) MultipartFile file) {

        if (fileName == null && file == null) {
            return new ResponseEntity<>("Please provide a file or a file name to update", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(fileService.updateFile(id, file, fileName), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable int id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }

}
