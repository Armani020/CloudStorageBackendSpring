package com.cloudstorage.cloudstorage;

import com.cloudstorage.cloudstorage.entities.Role;
import com.cloudstorage.cloudstorage.entities.User;
import com.cloudstorage.cloudstorage.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class CloudStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudStorageApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService)  {
		return args -> {
			userService.saveRole(new Role(null, "ROLE_USER"));
			userService.saveRole(new Role(null, "ROLE_MANAGER"));
			userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			userService.registration(new User(null, "ar.amirgaliev.2015@gmail.com", "1234", 1024L * 1024L * 1024L * 10L, 0L, null, new ArrayList<>(), new ArrayList<>()));
			userService.registration(new User(null, "arman", "1234", 1024L * 1024L * 1024L * 10L, 0L, null, new ArrayList<>(), new ArrayList<>()));

			//userService.addRoleToUser("ar.amirgaliev.2015@gmail.com", "ROLE_USER");
			//userService.addRoleToUser("ar.amirgaliev.2015@gmail.com", "ROLE_SUPER_ADMIN");
			//userService.addRoleToUser("arman", "ROLE_USER");
		};
	}
}


