package com.cloudstorage.cloudstorage.controllers;

import com.cloudstorage.cloudstorage.configs.filters.CustomAuthorizationFilter;
import com.cloudstorage.cloudstorage.entities.File;
import com.cloudstorage.cloudstorage.models.CreateDirReq;
import com.cloudstorage.cloudstorage.services.FileService;
import com.cloudstorage.cloudstorage.utils.exceptions.CouldNotDeleteFileException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/files")
@AllArgsConstructor
@RequiredArgsConstructor
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping
    public ResponseEntity<?> createDir(@RequestBody CreateDirReq createDirReq, @RequestHeader String authorization) {
        try {
            String accessToken = authorization.substring("Bearer ".length());
            String email = CustomAuthorizationFilter.getUsername(accessToken);
            return ResponseEntity.ok(fileService.createDir(createDirReq, email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Folder creating error");
        }
    }

    @GetMapping
    public ResponseEntity<?> getFiles(@RequestParam String parent, @RequestHeader String authorization) {
        try {
            String accessToken = authorization.substring("Bearer ".length());
            String email =  CustomAuthorizationFilter.getUsername(accessToken);
            return ResponseEntity.ok(fileService.findAll(parent, email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Files fetching error");
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestBody MultipartFile file, String parent, @RequestHeader String authorization) {
        try {
            String accessToken = authorization.substring("Bearer ".length());
            String email = CustomAuthorizationFilter.getUsername(accessToken);
            return ResponseEntity.ok(fileService.uploadFile(file, parent, email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("File uploading error");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam String fileId, @RequestHeader String authorization) {
        try {
            String accessToken = authorization.substring("Bearer ".length());
            String email = CustomAuthorizationFilter.getUsername(accessToken);

            File  fileEntityOptional = fileService.downloadFile(fileId, email);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileEntityOptional.getName())
                    .contentType(MediaType.valueOf(fileEntityOptional.getType()))
                    .body(fileEntityOptional.getData());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("File downloading error");
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestHeader String authorization, @RequestParam String file) {
        try {
            String accessToken = authorization.substring("Bearer ".length());
            String email = CustomAuthorizationFilter.getUsername(accessToken);
            fileService.deleteFile(file, email);
            return ResponseEntity.ok("File deleted successfully");
        } catch (CouldNotDeleteFileException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("File deleting error");
        }
    }
}
