package com.cloudstorage.cloudstorage.services;

import com.cloudstorage.cloudstorage.entities.File;
import com.cloudstorage.cloudstorage.entities.User;
import com.cloudstorage.cloudstorage.models.CreateDirReq;
import com.cloudstorage.cloudstorage.models.FileRes;
import com.cloudstorage.cloudstorage.repositories.FileRepo;
import com.cloudstorage.cloudstorage.repositories.UserRepo;
import com.cloudstorage.cloudstorage.utils.exceptions.CouldNotDeleteFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileRepo fileRepo;
    @Autowired
    private UserRepo userRepo;

    public FileRes createDir(CreateDirReq createDirReq, String email) {
        User user = userRepo.findByEmail(email);
        File parentFile = fileRepo.findById(createDirReq.getParent());
        File file = new File(createDirReq.getName(), "dir");

        if (parentFile == null) {
            file.setPath(file.getName());
        } else {
            file.setParent(parentFile);
            file.setPath(parentFile.getPath() + '\\' + file.getName());
        }
        file.setUser(user);

        return FileRes.toModel(fileRepo.save(file));
    }

    public FileRes uploadFile(MultipartFile file, String parentId, String email) throws IOException {
        File parentFile = fileRepo.findById(parentId);
        User user = userRepo.findByEmail(email);
        File fileDb = new File(file.getOriginalFilename(), file.getContentType(), file.getSize(), file.getBytes());
        if (parentFile == null) {
            fileDb.setPath(file.getOriginalFilename());
        } else {
            fileDb.setParent(parentFile);
            fileDb.setPath(parentFile.getPath() + '\\' + file.getOriginalFilename());
        }
        fileDb.setUser(user);
        user.setUsedSpace(user.getUsedSpace() + fileDb.getSize());
        userRepo.save(user);
        return FileRes.toModel(fileRepo.save(fileDb));
    }

    public File downloadFile(String fileId, String email) throws Exception {
        User user = userRepo.findByEmail(email);
        File file = fileRepo.findById(fileId);

        if (user == null) {
            throw new Exception("File downloading error");
        }
        if (file.getUser() != user) {
            throw new Exception("Access error");
        }
        return file;
    }

    public void deleteFile(String fileReq, String email) throws CouldNotDeleteFileException {
        User user = userRepo.findByEmail(email);
        File file = fileRepo.findById(fileReq);
        List<FileRes> childFiles = findAll(fileReq, email);

        if (!childFiles.isEmpty()) {
            for (FileRes fileRes: childFiles) {
                File file1 = fileRepo.findById(fileRes.getId());
                System.out.println(fileRes.getSize() + "1");
                user.setUsedSpace(user.getUsedSpace() - fileRes.getSize());
                userRepo.save(user);
                fileRepo.delete(file1);
            }
        }

        if (user != null && file.getUser() == user) {
            user.setUsedSpace(user.getUsedSpace() - file.getSize());
            System.out.println(file.getSize() + "2");
            userRepo.save(user);
            fileRepo.delete(fileRepo.findById(fileReq));
            return;
        }

        throw new CouldNotDeleteFileException("File deleting error");
    }

//    public File findById(String fileId) {
//        return fileRepo.findById(fileId);
//    }

    public List<FileRes> findAll(String parentId, String email) {
        File parentFile = fileRepo.findById(parentId);
        User user = userRepo.findByEmail(email);
        return FileRes.toModels(fileRepo.findByParentAndUser(parentFile, user));
    }
}
