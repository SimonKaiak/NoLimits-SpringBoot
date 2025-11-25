// Ruta: src/main/java/com/example/NoLimits/Multimedia/controller/AuthController.java

package com.example.NoLimits.Multimedia.controller;

import com.example.NoLimits.Multimedia.model.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(
    origins = {
        "http://localhost:5173",
        "https://no-limits-react.vercel.app"
    },
    allowCredentials = "true"
)
public class AuthController {

    // Repositorio para acceder a los datos de usuarios en la base de datos
    @Autowired
    private UsuarioRepository usuarioRepository;

    /*
     LOGIN REAL

     Este método recibe un correo y una contraseña desde el frontend.
     Verifica si existe un usuario con ese correo y si la contraseña coincide.
     Si todo es correcto, guarda el ID del usuario en la sesión.
    */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> body,
            HttpSession session) {

        String correo = body.get("correo");
        String password = body.get("password");

        // Validación básica: ambos campos son obligatorios
        if (correo == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Correo y contraseña son obligatorios");
        }

        // Búsqueda del usuario por correo en la base de datos
        UsuarioModel usuario = usuarioRepository
                .findByCorreo(correo.trim().toLowerCase())
                .orElse(null);

        // Si no existe el usuario o la contraseña no coincide, se rechaza el login
        if (usuario == null || !usuario.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Correo o contraseña incorrectos");
        }

        // Si las credenciales son correctas, se guarda el ID del usuario en la sesión
        session.setAttribute("usuarioId", usuario.getId());

        // Se devuelven datos básicos del usuario al frontend
        return ResponseEntity.ok(Map.of(
                "id", usuario.getId(),
                "nombre", usuario.getNombre(),
                "apellidos", usuario.getApellidos(),
                "correo", usuario.getCorreo(),
                "rolId", usuario.getRol().getId(),
                "rolNombre", usuario.getRol().getNombre()
        ));
    }

    /*
     LOGOUT

     Este método elimina la sesión actual del usuario.
     Al invalidar la sesión, el usuario deja de estar autenticado.
    */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}