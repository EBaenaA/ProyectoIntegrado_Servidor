
package Controlador;

import java.io.Serializable;

/**
 *
 * @author Enrique Banea Arrabal
 */
public class Usuario implements Serializable {

    String nombre;
    String pass;
    String correo;

    public Usuario() {
    }

    public Usuario(String nombre, String pass, String correo) {
        this.nombre = nombre;
        this.pass = pass;
        this.correo = correo;

    }

    public Usuario(String pass, String correo) {
        this.nombre = "Unknown";
        this.pass = pass;
        this.correo = correo;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String toString(){
        return "Nombre:      " + nombre + "\n" +
                "Correo:     " + correo + "\n" +
                "Contraseña: " + pass + "";
    }
}
