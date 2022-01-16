package com.cloudstorage.cloudstorage.models;

import com.cloudstorage.cloudstorage.entities.File;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class FileRes {
    private String id;
    private String name;
    private String type;
    private String path;
    private Long size;
    private Date date;
    private String parent;

    public static FileRes toModel(File entity) {
        FileRes model = new FileRes();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setType(entity.getType());
        model.setPath(entity.getPath());
        model.setSize(entity.getSize());
        model.setDate(entity.getDate());
        if (entity.getParent() == null) {
            model.setParent(null);
        } else {
            model.setParent(entity.getParent().getId());
        }
        return model;
    }

    public static List<FileRes> toModels(List<File> entities) {
        List<FileRes> models = new ArrayList<>();
        for (File file: entities) {
            FileRes model = toModel(file);
            models.add(model);
        }
        return models;
    }

    public static File toEntity(FileRes model) {
        File entity = new File();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setType(model.getType());
        entity.setPath(model.getPath());
        entity.setSize(model.getSize());
        entity.setDate(model.getDate());
        //entity.setParent(FileRes.toEntity(model.getParent()));
        return entity;
    }
}
