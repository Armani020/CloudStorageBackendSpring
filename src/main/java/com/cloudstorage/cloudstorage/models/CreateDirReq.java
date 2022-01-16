package com.cloudstorage.cloudstorage.models;

import lombok.Data;

@Data
public class CreateDirReq {
    private String name;
    private String parent;
    private String user;
}
