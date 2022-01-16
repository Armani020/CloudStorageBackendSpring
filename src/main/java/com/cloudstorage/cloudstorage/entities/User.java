package com.cloudstorage.cloudstorage.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Long diskSpace = 1024L * 1024L * 1024L * 10L;
    private Long usedSpace = 0L;
    private String avatar;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<File> files;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
}
