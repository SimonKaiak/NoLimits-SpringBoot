// Ruta: src/main/java/com/example/NoLimits/Multimedia/config/AdminInitializer.java
package com.example.NoLimits.Multimedia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired UsuarioRepository usuarioRepository;
    @Autowired RolRepository rolRepository;

    @Override
    public void run(String... args) {

        RolModel adminRol = rolRepository.findByNombreIgnoreCase("ADMIN")
                .orElse(null);

        if (adminRol == null) {
            System.out.println("❌ No existe el rol ADMIN. Crea el rol primero.");
            return;
        }

        if (!usuarioRepository.existsByCorreoIgnoreCase("nolimits@gmail.com")) {
            UsuarioModel admin = new UsuarioModel();
            admin.setNombre("Admin");
            admin.setApellidos("NoLimits");
            admin.setCorreo("nolimits@gmail.com");
            admin.setTelefono(911111111L);
            admin.setPassword("53176ben10");
            admin.setRol(adminRol);

            usuarioRepository.save(admin);
            System.out.println("✅ ADMIN PERMANENTE CREADO: nolimits@gmail.com");
        }
    }
}