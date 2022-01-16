package com.cloudstorage.cloudstorage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "files")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String name;
    private String type;
    private String path;
    private Long size = 0L;
    private Date date = new Date();
    @JsonIgnore
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
//    private List<File> childs;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private File parent;

    public File(String name, String type, Long size, byte[] data) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.data = data;
    }

    public File(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
