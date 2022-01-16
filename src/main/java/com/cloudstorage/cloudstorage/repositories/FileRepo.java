package com.cloudstorage.cloudstorage.repositories;

import com.cloudstorage.cloudstorage.entities.File;
import com.cloudstorage.cloudstorage.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepo extends JpaRepository<File, Long> {
    File findById(String id);
    void deleteFileById(String id);
    void delete(File file);
    List<File> findByUser(User user);
    List<File> findByParentAndUser(File file, User user);

    List<File> findByUserId(Long id);
}
