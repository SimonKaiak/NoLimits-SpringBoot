// Ruta: src/main/java/com/example/NoLimits/Multimedia/controller/AuthController.java

package com.example.NoLimits.Multimedia.controller.auth;

import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.security.JwtUtil;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/*
 Controlador encargado de la autenticación de usuarios.

 Aquí se procesa el inicio de sesión (login) y el cierre de sesión (logout).
 Utiliza HttpSession para mantener al usuario autenticado mientras navega
 por la aplicación.
*/
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    // Repositorio para acceder a los datos de usuarios en la base de datos
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /*
     LOGIN REAL

     Este método recibe un correo y una contraseña desde el frontend.
     Verifica si existe un usuario con ese correo y si la contraseña coincide.
     Si todo es correcto, guarda el ID del usuario en la sesión.
    */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        String correo = body.get("correo");
        String password = body.get("password");

        if (correo == null || password == null) {
            return ResponseEntity.badRequest().body("Correo y contraseña obligatorios");
        }

        UsuarioModel usuario = usuarioRepository
                .findByCorreo(correo.trim().toLowerCase())
                .orElse(null);

        if (usuario == null || !usuario.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Correo o contraseña incorrectos");
        }

        String rol = usuario.getRol().getNombre();

        String token = jwtUtil.generateToken(usuario.getCorreo(), rol);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "id", usuario.getId(),
                "nombre", usuario.getNombre(),
                "apellidos", usuario.getApellidos(),
                "correo", usuario.getCorreo(),
                "rolId", usuario.getRol().getId(),
                "rolNombre", rol
        ));
    }

    /*
     LOGOUT
    */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body("Logout OK");
    }
}