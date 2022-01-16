package com.cloudstorage.cloudstorage.models;

import com.cloudstorage.cloudstorage.entities.File;
import com.cloudstorage.cloudstorage.entities.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserRes {
    private Long id;
    private String email;
    private Long diskSpace;
    private Long usedSpace;
    //private List<FileRes> files;

    public static UserRes toModel(User entity) {
        UserRes model = new UserRes();
        model.setId(entity.getId());
        model.setEmail(entity.getEmail());
        model.setDiskSpace(entity.getDiskSpace());
        model.setUsedSpace(entity.getUsedSpace());
        //model.setFiles(entity.getFiles().stream().map(FileRes::toModel).collect(Collectors.toList()));
        return model;
    }
}
