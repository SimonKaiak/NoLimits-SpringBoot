package com.example.NoLimits.Multimedia.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;

@Configuration
public class RolesSeeder {

    @Bean
    CommandLineRunner seedRoles(RolRepository rolRepository) {
        return args -> {

            if (rolRepository.count() == 0) {

                RolModel user = new RolModel();
                user.setNombre("ROLE_USER");
                user.setDescripcion("Rol por defecto para usuarios");
                user.setActivo(true);

                RolModel admin = new RolModel();
                admin.setNombre("ROLE_ADMIN");
                admin.setDescripcion("Rol administrador del sistema");
                admin.setActivo(true);

                rolRepository.save(user);
                rolRepository.save(admin);
            }
        };
    }
}