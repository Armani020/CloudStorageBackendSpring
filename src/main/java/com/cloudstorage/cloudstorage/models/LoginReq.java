package com.cloudstorage.cloudstorage.models;

import lombok.Data;

@Data
public class LoginReq {
    private String email;
    private String password;
}
