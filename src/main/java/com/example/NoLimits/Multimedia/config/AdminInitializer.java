package com.example.NoLimits.Multimedia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
@Order(2)
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String correo;

    @Value("${app.admin.password}")
    private String password;

    @Override
    public void run(String... args) {

        if (correo == null || correo.isBlank() || password == null || password.isBlank()) {
            System.out.println("⚠️ Credenciales de admin no configuradas. Se omite creación del admin.");
            return;
        }

        RolModel adminRol = rolRepository.findByNombreIgnoreCase("ROLE_ADMIN")
                .orElseGet(() -> rolRepository.findByNombreIgnoreCase("ADMIN").orElse(null));

        if (adminRol == null) {
            System.out.println("❌ No existe ROLE_ADMIN/ADMIN. Revisa el seeder de roles.");
            return;
        }

        UsuarioModel admin = usuarioRepository.findByCorreoIgnoreCase(correo.trim().toLowerCase()).orElse(null);

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