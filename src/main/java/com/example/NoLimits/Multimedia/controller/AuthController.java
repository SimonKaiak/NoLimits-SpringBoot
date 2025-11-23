// Ruta: src/main/java/com/example/NoLimits/Multimedia/controller/AuthController.java

package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(
    origins = {
        "http://localhost:5173",
    },
    allowCredentials = "true"
)
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // LOGIN REAL
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> body,
            HttpSession session) {

        String correo = body.get("correo");
        String password = body.get("password");

        if (correo == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Correo y contraseña son obligatorios");
        }

        UsuarioModel usuario = usuarioRepository
                .findByCorreo(correo.trim().toLowerCase())
                .orElse(null);

        if (usuario == null || !usuario.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Correo o contraseña incorrectos");
        }

        // Guardamos usuario en sesión
        session.setAttribute("usuarioId", usuario.getId());

        return ResponseEntity.ok(Map.of(
                "id", usuario.getId(),
                "nombre", usuario.getNombre(),
                "apellidos", usuario.getApellidos(),
                "correo", usuario.getCorreo(),
                "rolId", usuario.getRol().getId(),
                "rolNombre", usuario.getRol().getNombre()
        ));
    }

    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}