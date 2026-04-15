package com.example.NoLimits.Multimedia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;

@Component
@Order(2)
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        final String correo = "nolimits@gmail.com";
        final String password = "53176ben10";

        RolModel adminRol = rolRepository.findByNombreIgnoreCase("ROLE_ADMIN")
                .orElseGet(() -> rolRepository.findByNombreIgnoreCase("ADMIN").orElse(null));

        if (adminRol == null) {
            System.out.println("❌ No existe ROLE_ADMIN/ADMIN. Revisa el seeder de roles.");
            return;
        }

        UsuarioModel admin = usuarioRepository.findByCorreoIgnoreCase(correo).orElse(null);

        if (admin == null) {
            admin = new UsuarioModel();
            System.out.println("🆕 Creando admin predeterminado...");
        } else {
            System.out.println("♻️ Admin ya existe, actualizando datos...");
        }

        admin.setNombre("Admin");
        admin.setApellidos("NoLimits");
        admin.setCorreo(correo.trim().toLowerCase());
        admin.setTelefono(911111111L);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRol(adminRol);

        usuarioRepository.save(admin);

        System.out.println("✅ Admin listo: " + admin.getCorreo());
    }
}