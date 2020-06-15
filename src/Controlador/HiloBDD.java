package Controlador;

import Modelo.Conector;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.LinkedList;


/**
 * @author Enrique Banea Arrabal
 */
public class HiloBDD extends Thread {

    //  ObjetoCliente cliente;
    Socket socket;
    static Conector jdbc;

    public HiloBDD(Socket socket, Conector jdbc) {
        this.socket = socket;
        this.jdbc = jdbc;
        System.out.println("New user connection: " + getAddress());
    }

    private String getAddress() {
        return socket.getInetAddress().getHostAddress();
    }
    //MensageCode :1

    public static boolean crearUsuario(Usuario u) {
        String sentencia = "INSERT INTO USUARIOS VALUES ( " + "'" + u.getCorreo() + "'" + "," + "'" + u.getNombre() + "'" + "," + "'" + u.getPass() + "')";
        jdbc.setSQL(sentencia);
        return jdbc.ejecutarConsultaActualizable();


    }

    //MensageCode :2
    public static boolean conectarUsuario(Usuario u) throws SQLException {

        ResultSet cursor = null;
        boolean todoOK = false;

        String sentencia = "SELECT * FROM USUARIOS WHERE PASS = '" + u.getPass() + "' AND CORREO = '" + u.getCorreo() + "';";

        jdbc.setSQL(sentencia);
        jdbc.ejecutarConsulta();
        cursor = jdbc.getCursor();

        if (cursor.first()) {
            todoOK = true;
        }

        cursor.close();
        return todoOK;
    }

    //MensageCode :3
    public static boolean registrarChat(Quiniela chat) {
        String sentencia = "INSERT INTO CHAT VALUES (0,"
                + "'" + chat.getCorreo() + "'" + ","
                + "'" + chat.getQuiniela() + "'" + ","
                + "'" + chat.getComentario() + "'" + ","
                + chat.getJornada() + ")";
        jdbc.setSQL(sentencia);
        return jdbc.ejecutarConsultaActualizable();

    }

    //MensageCode :4
    public static LinkedList<Quiniela> enviarChat() throws SQLException {

        ResultSet cursor;
        LinkedList<Quiniela> listChat = new LinkedList<>();
        String cadena = "SELECT * FROM CHAT ORDER BY ID";

        jdbc.setSQL(cadena);
        jdbc.ejecutarConsulta();
        cursor = jdbc.getCursor();

        while (cursor.next()) {
            Quiniela c = new Quiniela(cursor.getString("CHAT.CORREO"),
                    cursor.getString("CHAT.QUINIELA"), cursor.getString("CHAT.COMENTARIO"),
                    cursor.getInt("CHAT.JORNADA"));
            listChat.add(c);

        }


        return listChat;
    }

    public void run() {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            String cadena = ois.readUTF();
            System.out.println("User " + getAddress() + " sended " + cadena);
            switch (Integer.parseInt(cadena)) {
                case 1:
                    oos.writeUTF("1");
                    oos.flush();
                    System.out.println("Sended to " + getAddress() + ":  1");

                    Usuario usuario = new Usuario(ois.readUTF(), ois.readUTF(), ois.readUTF());
                    System.out.println("User " + getAddress() + " sended new user:");
                    System.out.println(usuario.toString());

                    boolean creado = crearUsuario(usuario);
                    String res = creado ? "1" : "0";
                    oos.writeUTF(res);
                    oos.flush();
                    System.out.println("Sended to " + getAddress() + ": " + res);
                    break;
                case 2:
                    oos.writeUTF("2");
                    oos.flush();
                    usuario = new Usuario(ois.readUTF(), ois.readUTF());
                    System.out.println("User " + getAddress() + " want to login with user:");
                    System.out.println(usuario.toString());
                    boolean conectar = conectarUsuario(usuario);
                    res = conectar ? "2" : "0";
                    oos.writeUTF(res);
                    oos.flush();
                    System.out.println("Sended to " + getAddress() + ": " + res);
                    break;
                case 3:
                    oos.writeUTF("3");
                    oos.flush();
                    Quiniela quiniela = new Quiniela(ois.readUTF(), ois.readUTF(), ois.readUTF(), Integer.parseInt(ois.readUTF()));
                    System.out.println("User " + getAddress() + " want to send a quiniela:");
                    System.out.println(quiniela.toString());
                    boolean crear = registrarChat(quiniela);
                    res = crear ? "3" : "0";
                    oos.writeUTF(res);
                    oos.flush();
                    System.out.println("Sended to " + getAddress() + ": " + res);
                    break;
                case 4:
                    oos.writeUTF("4");
                    oos.flush();
                    System.out.println("Sended to " + getAddress() + ": " + "4");
                    LinkedList<Quiniela> chat = enviarChat();
                    for (Quiniela q : chat) {
                        oos.writeUTF("AOM");
                        oos.flush();
                        oos.writeUTF(q.getCorreo());
                        oos.flush();
                        oos.writeUTF(q.getQuiniela());
                        oos.flush();
                        oos.writeUTF(q.getComentario());
                        oos.flush();
                        oos.writeUTF(q.getJornada() + "");
                        oos.flush();
                    }
                    oos.writeUTF("EOF");                    
                    oos.flush();
                    oos.writeObject(chat);
                    oos.flush();
                    System.out.println("Sended to " + getAddress() + ": " + chat.toString());
                    break;
            }

        } catch (Exception ex) {
            System.err.println("Error from user " + getAddress() + ": " + ex.getMessage());
        }



    }
}