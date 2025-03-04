package com.michaelnguyen;

import com.michaelnguyen.entity.Permission;
import com.michaelnguyen.entity.Role;
import com.michaelnguyen.repository.IRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@SpringBootApplication
public class ApiArchivePhotoApplication {
    private final IRoleRepository roleRepository;

    public ApiArchivePhotoApplication(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiArchivePhotoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initRoles() {
        return args -> {
            // ROLE: USER
            if (roleRepository.findById("USER").isEmpty()) {
                Role userRole = Role.builder()
                        .name("USER")
                        .description("Standard user role")
                        .permissions(Set.of(Permission.READ))
                        .build();
                roleRepository.save(userRole);
            }

            // ROLE: ADMIN
            if (roleRepository.findById("ADMIN").isEmpty()) {
                Role adminRole = Role.builder()
                        .name("ADMIN")
                        .description("Administrator role")
                        .permissions(Set.of(Permission.READ, Permission.WRITE, Permission.DELETE))
                        .build();
                roleRepository.save(adminRole);
            }

        };

    }
}
