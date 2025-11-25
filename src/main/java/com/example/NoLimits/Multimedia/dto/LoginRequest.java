// Ruta: src/main/java/com/example/NoLimits/Multimedia/dto/LoginRequest.java
package com.example.NoLimits.Multimedia.dto;

/*
 Esta clase es un DTO (Data Transfer Object) utilizado para recibir
 los datos de inicio de sesión que envía el frontend.

 No representa una entidad de la base de datos, sino que actúa como
 un contenedor simple para transportar el correo y la contraseña
 hacia el backend cuando el usuario intenta iniciar sesión.
*/
public class LoginRequest {

    // Correo electrónico ingresado por el usuario en el formulario de login.
    private String correo;

    // Contraseña ingresada por el usuario en el formulario de login.
    private String password;

    /*
     Devuelve el correo almacenado en este DTO.
     Se utiliza para que el backend pueda acceder al valor enviado.
    */
    public String getCorreo() {
        return correo;
    }

    /*
     Permite asignar el correo recibido desde el frontend.
     Este método se usa cuando se construye el objeto a partir del JSON enviado.
    */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /*
     Devuelve la contraseña almacenada en este DTO.
     Será utilizada para validar las credenciales del usuario.
    */
    public String getPassword() {
        return password;
    }

    /*
     Permite asignar la contraseña recibida desde el frontend.
     Este valor se compara luego con la contraseña registrada en el sistema.
    */
    public void setPassword(String password) {
        this.password = password;
    }
}