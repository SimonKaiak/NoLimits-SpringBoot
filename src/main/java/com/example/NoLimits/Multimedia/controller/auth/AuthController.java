package com.example.NoLimits.Multimedia.controller.auth;

import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.security.JwtUtil;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        String correo = body.get("correo");
        String password = body.get("password");

        if (correo == null || password == null) {
            return ResponseEntity.badRequest().body("Correo y contraseña obligatorios");
        }

        String correoIn = correo.trim();
        String passIn = password.trim();

        UsuarioModel usuario = usuarioRepository
                .findByCorreoIgnoreCase(correoIn)
                .orElse(null);

        if (usuario == null || usuario.getPassword() == null ||
                !passwordEncoder.matches(passIn, usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Correo o contraseña incorrectos");
        }

        String rol = (usuario.getRol() != null) ? usuario.getRol().getNombre() : "ROLE_USER";

        String token = jwtUtil.generateToken(usuario.getCorreo(), rol);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "id", usuario.getId(),
                "nombre", usuario.getNombre(),
                "apellidos", usuario.getApellidos(),
                "correo", usuario.getCorreo(),
                "rolId", usuario.getRol() != null ? usuario.getRol().getId() : null,
                "rolNombre", rol
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body("Logout OK");
    }
}