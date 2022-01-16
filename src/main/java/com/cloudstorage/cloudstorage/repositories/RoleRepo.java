package com.cloudstorage.cloudstorage.repositories;

import com.cloudstorage.cloudstorage.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role getByName(String name);
    Role findByName(String name);
}
