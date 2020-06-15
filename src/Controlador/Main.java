package Controlador;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import Modelo.Conector;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;

/**
 * @author Enrique Baena Arrabal
 */
public class Main {

    private static final int puerto = 6000;

    public static void main(String[] args) {
        File archivoProperties = new File("ficheros/properties.txt");
        Conector conector = null;
        ServerSocket socketServidor = null;
        try {
            conector = new Conector(archivoProperties);
            if (!archivoProperties.exists()) {
                System.out.print("No se ha podido conectar con la base de datos.\n"
                        + "Asegurese de que el archivo 'ficheros/properties.txt' es correcto.");
            } else {

                socketServidor = new ServerSocket(puerto);
                System.out.println(" Servidor conectado ESCUCHANDO ");
                while (true) {
                    Socket socketCliente = socketServidor.accept();
                    new HiloBDD(socketCliente, conector).start();
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);


        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            if (socketServidor != null) {
                try {
                    socketServidor.close();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
