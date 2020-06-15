package Controlador;

import java.io.Serializable;

/**
 *
 * @author Enrique Banea Arrabal
 */
public class Quiniela implements Serializable {

    //int id;
    String correo;
    String quiniela;
    String comentario;
    int jornada;

    public Quiniela() {
    }

    public Quiniela(String correo, String quiniela, String comentario, int jornada) {
        this.correo = correo;
        this.quiniela = quiniela.replaceAll("_", "");
        this.comentario = comentario;
        this.jornada = jornada;
    }

    /* public int getId() {
     return id;
     }

     public void setId(int id) {
     this.id = id;
     }*/
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getQuiniela() {
        return quiniela;
    }

    public void setQuiniela(String quiniela) {
        this.quiniela = quiniela;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getJornada() {
        return jornada;
    }

    public void setJornada(int jornada) {
        this.jornada = jornada;
    }

    @Override
    public String toString() {
        return "Correo: " + correo + "\n"
                + "Comentario:" + comentario + "\n"
                + "Jornada: " + jornada + "\n"
                + "Quiniela: " + quiniela;
    }
}