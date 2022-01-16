package com.cloudstorage.cloudstorage.services;

import com.cloudstorage.cloudstorage.entities.Role;
import com.cloudstorage.cloudstorage.entities.User;
import com.cloudstorage.cloudstorage.models.UserRes;
import com.cloudstorage.cloudstorage.repositories.RoleRepo;
import com.cloudstorage.cloudstorage.repositories.UserRepo;
import com.cloudstorage.cloudstorage.utils.exceptions.UserAlreadyExistException;
import com.cloudstorage.cloudstorage.utils.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor // Для того что бы не писать @Autowired
@Transactional
@Slf4j
public class UserService implements IUserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User exists: {}", email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public User registration(User user) throws UserAlreadyExistException {
        if (userRepo.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistException("User already exists");
        }
        if (user.getEmail().equals("") || user.getPassword().equals("null")) {
            throw new UserAlreadyExistException("Inputs are empty");
        }
        log.info("Saving user {} to the database", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        addRoleToUser(user.getEmail(), "ROLE_USER");
        return user;
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving role {} to the database", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        log.info("Adding role {} to user {}", roleName, email);
        User user = userRepo.findByEmail(email);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public User getUser(String email) throws UserNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        log.info("Fetching user {}", email);
        return user;//UserRes.toModel(user);
    }

    @Override
    public UserRes getUserRes(String email) throws UserNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        log.info("Fetching user {}", email);
        return UserRes.toModel(user);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }
}
